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

		// 세션 검증
		HttpSession session = request.getSession(false);
		long usersNum = 0;
		if (session != null && session.getAttribute("usersNum") != null) {
		    usersNum = (Long) session.getAttribute("usersNum");
		}

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter out = response.getWriter();
        
        // Select문 실행
        List<NotificationDto> notiList = NotificationDao.getInstance().notiSelectByUsersNum(usersNum);
        

        while (true) {
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = new JSONObject();

            obj.put("createdAt", "2025-07-02 ~ 2025-08-27");
            obj.put("senderId", usersNum);
            obj.put("typeGroupId", "reservation");
            obj.put("message", "예약 확정" + System.currentTimeMillis());

            jsonArray.add(obj);

            out.write("data: " + jsonArray.toJSONString() + "\n\n");
            out.flush();

            try {
                Thread.sleep(3000); // 3초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 상태만 복구
                System.err.println("SSE 쓰레드 인터럽트 발생. 연결 유지.");
                // break 하지 않음 — 계속 연결 유지
            } catch (Exception e) {
                e.printStackTrace();
                break; // 진짜 예외일 때만 종료
            }
        }
    }
}
