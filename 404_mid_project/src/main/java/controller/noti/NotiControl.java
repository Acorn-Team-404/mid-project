package controller.noti;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.noti.NotificationDao;

@WebServlet("*.noti")
public class NotiControl extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// 사용자 요청 path 추출
		String uri = req.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		

		
		if(path.equals("/setRead.noti")) {
			try {
				long notiNum = Long.parseLong(req.getParameter("notiNum"));
				boolean setReadIsSuccess = NotificationDao.getInstance().notiSetRead(notiNum);
				System.out.println(setReadIsSuccess ? notiNum + "번 알림 읽음처리 성공" : notiNum + "번 알림 읽음처리 실패");
		        if (setReadIsSuccess) {
		            response.setStatus(200);
		        } else {
		            response.setStatus(500); // 처리 실패
		        }

			} catch (Exception e) {
				System.out.println("notiNum 파라미터: " + req.getParameter("notiNum"));
		        e.printStackTrace();
		        response.setStatus(500); // 서버 에러
			}
		}
		
		
		
		
		if(path.equals("/delete.noti")) {
			try {
				long notiNum = Long.parseLong(req.getParameter("notiNum"));
				boolean deleteIsSuccess = NotificationDao.getInstance().notiDelete(notiNum);
				System.out.println(deleteIsSuccess ? notiNum + "번 알림 삭제 성공" : notiNum + "번 알림 삭제 실패");
		        if (deleteIsSuccess) {
		            response.setStatus(200);
		        } else {
		            response.setStatus(500); // 처리 실패
		        }

			} catch (Exception e) {
		        e.printStackTrace();
		        response.setStatus(500); // 서버 에러
			}
		}
	}
}





