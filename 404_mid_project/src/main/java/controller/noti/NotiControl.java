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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 사용자 요청 path 추출
		String uri = req.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		
		long notiNum = Long.parseLong(req.getParameter("notiNum"));

		
		
		if(path.equals("/delete.noti")) {
			boolean deleteIsSuccess = NotificationDao.getInstance().notiDelete(notiNum);
			System.out.println(deleteIsSuccess ? notiNum + "번 알림 삭제 성공" : notiNum + "번 알림 삭제 실패");
		}
		
		if(path.equals("/setRead.noti")) {
			boolean setReadIsSuccess = NotificationDao.getInstance().notiSetRead(notiNum);
			System.out.println(setReadIsSuccess ? notiNum + "번 알림 읽음처리 성공" : notiNum + "번 알림 읽음처리 실패");
		}
	}
}




