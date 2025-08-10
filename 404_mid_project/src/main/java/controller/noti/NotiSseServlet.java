package controller.noti;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import controller.noti.NotiEventBroker;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.noti.NotificationDao;
import model.noti.NotificationDto;

// json-simple
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(value = "/sse", asyncSupported = true) // 비동기 처리를 지원한다는 선언
public class NotiSseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션 검증
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usersId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Object userNumObj = session.getAttribute("usersNum");
        if (!(userNumObj instanceof Long usersNum) || usersNum <= 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 필수 헤더 정의
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // 캐시 방지
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Connection", "keep-alive");

        // 비동기 시작 + 구독 등록
        AsyncContext asyncContext = request.startAsync(); // 비동기로 전환
        asyncContext.setTimeout(0); // 타임아웃 없이 무제한 유지
        NotiEventBroker.getInstance().subscribe(usersNum, asyncContext); // 유저별 SSE 채널 등록

        PrintWriter out = asyncContext.getResponse().getWriter(); // asyncContext에서 꺼낸 Response 사용
        out.write("retry: 20000\n\n"); // 연결이 끊기면 20초 후 재연결 지시
        out.flush();

        // 초기에 한번 현재 상태 스냅샷을 전송
        long lastNotiNum = 0L; // 기본값은 0으로 초기 연결 시에는 모든 알림 전송 (long 타입 자동 변환)
        String lastEventId = request.getHeader("Last-Event-ID"); // 재연결 시 브라우저가 자동 첨부
        if (lastEventId != null) {
            try { lastNotiNum = Long.parseLong(lastEventId); } catch (NumberFormatException ignore) {}
        }

        // 마지막 알림번호를 기준으로 알림 SELECT 실행
        List<NotificationDto> initList =
                NotificationDao.getInstance().notiSelectAfter(usersNum, lastNotiNum);
        int unreadCount = NotificationDao.getInstance().notiReadCount(usersNum); // 읽지 않은 알림 수 SELECT

        if (!initList.isEmpty()) { // 조회 결과가 하나 이상이면
            JSONArray dataArr = new JSONArray(); // 데이터를 담을 JSON 배열 생성
            for (NotificationDto dto : initList) {
                dataArr.add(dtoToJson(dto, unreadCount)); // 각 DTO를 변환 후 배열에 추가
            }

            // 마지막으로 가져온 notiNum을 기억 (재연결 시 이어받을 기준점)
            long snapshotLastId = initList.get(initList.size() - 1).getNotiNum();

            StringBuilder sb = new StringBuilder(); // 문자열을 효율적으로 이어 붙이기 위한 가변 문자열 객체
            // SSE 이벤트 구성
            sb.append("id: ").append(snapshotLastId).append("\n"); // EventSource에서 재연결 시 Last-Event-ID 헤더로 전송
            sb.append("event: noti\n"); // JS에서 eventSource.addEventListener("noti", handler)로 받을 수 있음
            sb.append("data: ").append(dataArr.toJSONString()).append("\n\n"); // JSON 페이로드 데이터 전송( \n\n은 SSE 이벤트 종료)

            out.write(sb.toString());
            out.flush();
        } else {
        	// 전송할 데이터가 없더라도 SSE 연결 유지용 전송
            out.write(": init\n\n");
            out.flush();
        }
    }

    
    // initList에 쌓일 데이터 (JSON 객체)
    private static JSONObject dtoToJson(NotificationDto dto, int readCount) {
        JSONObject obj = new JSONObject();

        obj.put("notiNum", dto.getNotiNum());
        obj.put("senderNum", dto.getNotiSenderNum());
        obj.put("message", nz(dto.getNotiMessage()));
        obj.put("readCode", dto.getNotiReadCode());
        obj.put("createdAt", nz(dto.getNotiCreatedAt()));
        obj.put("imageType", nz(dto.getNotiImageType()));
        obj.put("typeCode", dto.getNotiTypeCode());
        obj.put("type", nz(dto.getNotiType()));
        obj.put("daysAgo", nz(dto.getNotiDaysAgo()));

        obj.put("bookNum", nz(dto.getNotiBookNum()));
        obj.put("bookCheckIn", nz(dto.getNotiCheckIn()));
        obj.put("bookCheckOut", nz(dto.getNotiCheckOut()));

        obj.put("stayNum", dto.getNotiStayNum());
        obj.put("stayName", nz(dto.getNotiStayName()));

        obj.put("commentUsersNum", dto.getNotiCommentUsersNum());
        obj.put("commentWriter", nz(dto.getNotiCommentWriter()));
        obj.put("commentContent", nz(dto.getNotiCommentContent()));
        obj.put("commentParentNum", nz(dto.getNotiCommentParentNum()));

        obj.put("inqNum", dto.getNotiInqNum());
        obj.put("inqTitle", nz(dto.getNotiInqTitle()));
        obj.put("inqContent", nz(dto.getNotiInqContent()));

        obj.put("imageName", nz(dto.getNotiImageName()));
        obj.put("readCount", readCount);

        return obj;
    }

    // null을 안전하게 처리하고 데이터를 String 타입으로 변환하는 메서드
    private static String nz(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
