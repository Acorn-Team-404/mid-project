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
        	    out.write("retry: 30000\n");
        	    out.flush();  // 즉시 브라우저에 전송
        	    retrySent = true;
        	}
        	
        	// Select문 실행
            List<NotificationDto> notiList = NotificationDao.getInstance().notiSelectAfter(usersNum, lastNotiNum);
            
        	
        	if(!notiList.isEmpty() && lastNotiNum < notiList.get(0).getNotiNum()) {
        		// 한번의 SSE값을 담을 Array 선언
                JSONArray jsonArray = new JSONArray();
                
                // notiList의 수만큼 순환하며
            	for(NotificationDto tmp : notiList) {
            		
            		// 오브젝트를 생성하고
                    JSONObject obj = new JSONObject();

                    // 데이터 삽입
                    obj.put("createdAt", tmp.getNotiCreatedAt());
                    obj.put("senderId", tmp.getNotiSenderNum());
                    obj.put("typeGroupId", tmp.getNotiType());
                    obj.put("message", tmp.getNotiMessage());
                    obj.put("daysAgo", tmp.getNotiDaysAgo() + "일 전");

                    // Array에 데이터가 들어간 Object 삽입
                    jsonArray.add(obj);
            	}
            	lastNotiNum = notiList.stream().mapToLong(NotificationDto::getNotiNum).max().orElse(lastNotiNum);

            	
            	// 여러 개의 Object가 들어간 Array 반환
                out.write("data: " + jsonArray.toJSONString() + "\n\n");
                out.flush();
                
             // 🔧 연결 종료 감지: 클라이언트가 연결을 끊었는지 확인
                if (out.checkError()) {
                    System.out.println("클라이언트 연결 끊김");
                    break; // 쓰레드 종료
                }
        	} else {
        	    // 알림 없을 때도 연결 유지 위해 heartbeat
        	    out.write(": heartbeat\n\n");
        	    out.flush();
        	}
        	
            try {
                Thread.sleep(30000); // 30초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 상태만 복구
                System.err.println("SSE 쓰레드 인터럽트 발생. 연결 유지");
                // break 하지 않음 — 계속 연결 유지
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("SSE 연결 종료됨");
                break; // 진짜 예외일 때만 종료
            }
        }
    }
}
