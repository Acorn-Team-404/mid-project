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

        PrintWriter out = response.getWriter();
        long lastNotiNum = 0;
        boolean retrySent = false;

        while (true) {
            System.out.println("SSE 연결 시작됨 - usersNum: " + usersNum + ", 시간: " + new java.util.Date());

            if (!retrySent) {
                out.write("retry: 60000\n");
                out.flush();
                retrySent = true;
            }

            List<NotificationDto> notiList = new ArrayList<>();
            try {
                notiList = NotificationDao.getInstance().notiSelectAfter(usersNum, lastNotiNum);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("🔴 알림 조회 중 예외 발생 - SSE 종료");
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

                    obj.put("bookCheckIn", tmp.getNotiCheckIn());
                    obj.put("bookCheckOut", tmp.getNotiCheckOut());
                    obj.put("stayName", tmp.getNotiStayName());

                    obj.put("commentWriter", tmp.getNotiCommentWriter());
                    obj.put("commentContent", tmp.getNotiCommentContent());

                    jsonArray.add(obj);
                }

                lastNotiNum = notiList.stream()
                        .mapToLong(NotificationDto::getNotiNum)
                        .max()
                        .orElse(lastNotiNum);

                out.write("data: " + jsonArray.toJSONString() + "\n\n");
                out.flush();

                if (out.checkError()) {
                    System.out.println("❌ 클라이언트 연결 끊김");
                    break;
                }
            } else {
                out.write(": heartbeat\n\n");
                out.flush();

                if (out.checkError()) {
                    System.out.println("❌ 클라이언트 연결 끊김 (heartbeat)");
                    break;
                }
            }

            try {
                Thread.sleep(60000); // 60초 간격으로 polling
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("SSE 쓰레드 인터럽트 발생. 연결 유지");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("SSE 예외 발생. 연결 종료");
                break;
            }
        }
    }
}
