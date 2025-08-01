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
        
        
        
        
        while (true) {
        	// Select문 실행
            //List<NotificationDto> notiList = NotificationDao.getInstance().notiSelectByUsersNum(usersNum);
            
            // 한번의 SSE값을 담을 Array 선언
            JSONArray jsonArray = new JSONArray();
            
            // notiList의 수만큼 순환하며
        	//for(NotificationDto tmp : notiList) {
        		
        		// 오브젝트를 생성하고
                JSONObject obj = new JSONObject();

                // 데이터 삽입
                obj.put("createdAt", "2025-08-01");
                obj.put("senderId", usersNum);
                obj.put("typeGroupId", "예약 완료");
                obj.put("message", "알림 너무 어렵다 ㅠㅠ" + System.currentTimeMillis());

                // Array에 데이터가 들어간 Object 삽입
                jsonArray.add(obj);
        	//}

        	// 여러 개의 Object가 들어간 Array 반환
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
