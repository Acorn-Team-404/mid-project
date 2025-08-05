package controller.noti;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.noti.NotificationDao;
import model.noti.NotificationDto;

@WebServlet("/sse")
public class NotiSseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션 검증 (usersId와 usersNum 동시에 확인)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usersId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }

        Object userNumObj = session.getAttribute("usersNum");
        if (userNumObj == null || !(userNumObj instanceof Long)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        long usersNum = (Long) userNumObj;
        if (usersNum <= 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // SSE 기본 설정
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Connection", "keep-alive");
        
        
        PrintWriter out = null;
        
        try {
        out = response.getWriter();
        long lastNotiNum = 0; // 마지막 알림의 번호를 기억
        boolean retrySent = false;
        long startTime = System.currentTimeMillis(); // 연결 시간이 길어지는 걸 방지하는 타임아웃 변수

        while (true) {
        	// 10분 초과 시 종료
        	if (System.currentTimeMillis() - startTime > 1000 * 60 * 10) {
                System.out.println("⏳ SSE 연결 시간 초과로 종료");
                break;
            }
        	
            System.out.println("SSE 연결 시작됨 - usersNum: " + usersNum + ", 시간: " + new java.util.Date());

            if (!retrySent) {
                out.write("retry: 60000\n"); // 연결이 끊기면 60초 후 재연결하라는 명령
                out.flush();
                retrySent = true; // 처음 한 번만 명령하도록 설정
            }

            List<NotificationDto> notiList = new ArrayList<>();
            try {
                notiList = NotificationDao.getInstance().notiSelectAfter(usersNum, lastNotiNum);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("🔴 DAO 조회 중 예외 발생 - SSE 종료");
                break;
            }

            if (!notiList.isEmpty() && lastNotiNum < notiList.get(0).getNotiNum()) {
                JSONArray jsonArray = new JSONArray();

                for (NotificationDto tmp : notiList) {
                    JSONObject obj = new JSONObject();

                    obj.put("notiNum", tmp.getNotiNum());
                    obj.put("senderNum", tmp.getNotiSenderNum());
                    obj.put("message", tmp.getNotiMessage());
                    obj.put("readCode", tmp.getNotiReadCode());
                    obj.put("createdAt", tmp.getNotiCreatedAt());
                    obj.put("typeCode", tmp.getNotiTypeCode());

                    obj.put("type", tmp.getNotiType());
                    obj.put("daysAgo", tmp.getNotiDaysAgo() + "일 전");
                    obj.put("readCount", tmp.getNotiReadCount());
                    

                    obj.put("bookCheckIn", tmp.getNotiCheckIn());
                    obj.put("bookCheckOut", tmp.getNotiCheckOut());
                    obj.put("stayName", tmp.getNotiStayName());

                    obj.put("commentWriter", tmp.getNotiCommentWriter());
                    obj.put("commentContent", tmp.getNotiCommentContent());
                    obj.put("commentParentNum", tmp.getNotiCommentParentNum());

                    jsonArray.add(obj);
                }

                lastNotiNum = notiList.stream()
                        .mapToLong(NotificationDto::getNotiNum)
                        .max()
                        .orElse(lastNotiNum);

                out.write("data: " + jsonArray.toJSONString() + "\n\n");
                out.flush();

                if (out.checkError()) { // PrintWriter가 쓰기 실패 상태인지 확인
                    System.out.println("❌ 클라이언트 연결 끊김");
                    break;
                }
                
            } else {
                out.write(": heartbeat\n\n"); // 브라우저와 연결 유지를 위한 write
                out.flush();

                if (out.checkError()) { // heartbeat 쓰기 도중 에러 체크
                    System.out.println("❌ 클라이언트 연결 끊김 (heartbeat)");
                    break;
                }
            }

            try {
                Thread.sleep(60000); // 60초 간격으로 polling
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // sleep() 중 인터럽트 시 쓰레드 복구
                System.err.println("SSE 쓰레드 인터럽트 발생. 연결 유지");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("SSE 예외 발생. 연결 종료");
                break; // 그 외 예상치 못한 예외 발생 시 루프 종료(자원 누수 방지)
            }
        }
        
        } finally {
            if (out != null) {
                out.close(); // 🔹 연결 종료 시 자원 해제
                System.out.println("🔚 PrintWriter 자원 해제 완료");
            }
        }
    }
}
