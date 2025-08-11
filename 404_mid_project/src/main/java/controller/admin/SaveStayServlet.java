package controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;

import model.admin.StayInfoDao;
import model.admin.StayInfoDto;
import model.room.RoomDto;

@WebServlet("/saveStay")
@MultipartConfig // 멀티파트 요청을 파싱하도록 설정한다.
public class SaveStayServlet extends HttpServlet {

    // ----- 유틸 메소드 -----

    // null이면 빈문자열을 리턴한다. 좌우 공백도 제거한다.
    private static String nvl(String s) { return (s == null) ? "" : s.trim(); }

    // 문자열을 int로 안전하게 파싱한다. 실패하면 def를 리턴한다. removeComma가 true면 콤마를 제거한다.
    private static int parseIntSafe(String s, int def, boolean removeComma) {
        if (s == null) return def;
        String x = removeComma ? s.replace(",", "") : s;
        try { return Integer.parseInt(x.trim()); } catch (Exception e) { return def; }
    }

    // 알림 후 이동/뒤로가기를 수행한다.
    private static void alertAndGo(HttpServletResponse resp, String message, String goUrl) throws IOException {
        resp.setContentType("text/html; charset=UTF-8"); // 응답을 UTF-8 HTML로 지정한다.
        PrintWriter out = resp.getWriter();
        // 메시지를 띄우고 지정한 위치로 이동한다.
        out.println("<script>alert('" + escapeJs(message) + "'); location.href='" + goUrl + "';</script>");
        out.flush();
    }

    // 알림 후 뒤로가기 한다.
    private static void alertAndBack(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<script>alert('" + escapeJs(message) + "'); history.back();</script>");
        out.flush();
    }

    // 간단한 JS 문자열 이스케이프를 수행한다.
    private static String escapeJs(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("'", "\\'");
    }

    // include 대상 서블릿이 실수로 응답에 쓰지 못하도록 막는 래퍼이다.
    private static class SilentResponseWrapper extends HttpServletResponseWrapper {
        private static final PrintWriter NOOP_WRITER = new PrintWriter(java.io.OutputStream.nullOutputStream());
        public SilentResponseWrapper(HttpServletResponse response) { super(response); }
        @Override public PrintWriter getWriter() { return NOOP_WRITER; } // 어떤 출력도 무시한다.
        @Override public jakarta.servlet.ServletOutputStream getOutputStream() {
            return new jakarta.servlet.ServletOutputStream() {
                @Override public void write(int b) { /* 무시한다. */ }
                @Override public boolean isReady() { return true; }
                @Override public void setWriteListener(jakarta.servlet.WriteListener writeListener) { }
            };
        }
    }

    // ----- 메인 메소드 -----

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // POST 요청 바디를 UTF-8로 해석한다.
        req.setCharacterEncoding("UTF-8");

        // 0) 세션 검증을 수행한다. 로그인 정보가 없으면 진행하지 않는다.
        HttpSession session = req.getSession(false); // 기존 세션만 얻는다.
        Object userNumObj = (session != null) ? session.getAttribute("usersNum") : null;
        if (userNumObj == null) {
            // 로그인 화면으로 안내한다.
            alertAndGo(resp, "로그인이 필요합니다.", req.getContextPath() + "/user/login.jsp");
            return;
        }

        // 1) 숙소 DTO를 구성한다. 문자열은 nvl로 null을 빈문자로 보정한다.
        StayInfoDto info = new StayInfoDto();
        info.setStayUserNum(Integer.parseInt(userNumObj.toString()));       // 세션에서 사용자 번호를 읽는다.
        info.setStayName(nvl(req.getParameter("stay_name")));               // 숙소 이름을 설정한다.
        info.setStayLoc(nvl(req.getParameter("stay_region")));              // 지역을 설정한다.
        info.setStayAddr(nvl(req.getParameter("stay_address")));            // 상세주소를 설정한다.
        info.setStayLat(nvl(req.getParameter("stay_lat")));                 // 위도를 설정한다.
        info.setStayLong(nvl(req.getParameter("stay_lng")));                // 경도를 설정한다.
        info.setStayPhone(nvl(req.getParameter("stay_contact")));           // 연락처를 설정한다.
        info.setStayFacilities(nvl(req.getParameter("stay_facilities")));   // 편의시설을 설정한다.
        info.setStayContent(nvl(req.getParameter("stay_content")));         // 소개글(HTML)을 설정한다.

        // 1-1) 필수값 간단 검증을 수행한다. 비즈니스 규칙에 맞게 조정 가능하다.
        if (info.getStayName().isEmpty() || info.getStayLoc().isEmpty() || info.getStayAddr().isEmpty()) {
            alertAndBack(resp, "숙소 이름/지역/상세주소는 필수입니다.");
            return;
        }

     // 2) 객실 목록 파싱 (인덱스 틈이 있어도 안전)
        List<RoomDto> rooms = new ArrayList<>();

        java.util.regex.Pattern idxPattern = java.util.regex.Pattern.compile("^rooms\\[(\\d+)]\\.roomName$");
        java.util.Set<Integer> indexSet = new java.util.TreeSet<>();
        for (String name : req.getParameterMap().keySet()) {
            java.util.regex.Matcher m = idxPattern.matcher(name);
            if (m.find()) indexSet.add(Integer.parseInt(m.group(1)));
        }

        for (int i : indexSet) {
            String prefix = "rooms[" + i + "]";
            String roomName = req.getParameter(prefix + ".roomName");
            if (roomName == null || roomName.trim().isEmpty()) continue; // 방어

            RoomDto r = new RoomDto();
            r.setRoomName(nvl(roomName));
            r.setRoomType(nvl(req.getParameter(prefix + ".roomType")));
            r.setRoomPrice(parseIntSafe(req.getParameter(prefix + ".roomPrice"), 0, true));
            r.setRoomAdultMax(parseIntSafe(req.getParameter(prefix + ".roomAdultMax"), 2, false));
            r.setRoomChildrenMax(parseIntSafe(req.getParameter(prefix + ".roomChildrenMax"), 0, false));
            r.setRoomInfantMax(parseIntSafe(req.getParameter(prefix + ".roomInfantMax"), 0, false));
            r.setRoomPaxMax(parseIntSafe(req.getParameter(prefix + ".roomPaxMax"), 2, false));
            r.setRoomContent(nvl(req.getParameter(prefix + ".roomContent")));
            rooms.add(r);
        }

        info.setRooms(rooms);
        if (rooms.isEmpty()) {
            alertAndBack(resp, "객실을 최소 1개 이상 추가하세요.");
            return;
        }
        System.out.println("[saveStay] rooms indexSet=" + indexSet + ", count=" + rooms.size());

        // 3) DB에 저장을 요청한다. 성공 시 stayNum/roomNum이 채워져 돌아온다.
        info = StayInfoDao.getInstance().insertStayWithRooms(info);
        if (info.getStayNum() == 0) {
            alertAndBack(resp, "숙소 저장 실패");
            return;
        }

        // 4) 이미지 업로드를 수행한다. include 대상은 응답에 쓰지 않도록 강제한다.
        //    업로드 서블릿은 partName으로 파일 파트를 식별한다.
        HttpServletResponse silent = new SilentResponseWrapper(resp); // 응답 출력을 차단한다.

        // 4-1) 숙소 대표 이미지 업로드를 include한다.
        String stayPartName = "stayUploadFile"; // 폼 input name과 일치한다.
        req.getRequestDispatcher(
                "/stay.img?target_type=stay"
              + "&target_id=" + info.getStayNum()
              + "&partName="  + stayPartName
        ).include(req, silent); // 출력이 무시되도록 silent로 include한다.

        // 4-2) 각 객실 이미지 업로드를 include한다.
        for (int i = 0; i < rooms.size(); i++) {
            RoomDto r = rooms.get(i);
            String roomPartName = "roomUploadFile_" + i; // 동적 생성된 input name과 일치한다.
            req.getRequestDispatcher(
                    "/room.img?target_type=room"
                  + "&target_id=" + r.getRoomNum()
                  + "&partName="  + roomPartName
            ).include(req, silent); // 출력이 무시되도록 silent로 include한다.
        }

        // 5) 저장 완료 후 목록 페이지로 리다이렉트한다.
        resp.sendRedirect(req.getContextPath() + "/test/stay-list.jsp");
    }
}
