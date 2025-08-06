package controller.noti;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.noti.NotificationDao;
import model.noti.NotificationDto;

// 알림의 읽음처리 및 삭제를 실행하는 Servlet
@WebServlet("*.noti")
public class NotiControl extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 사용자 요청 path 추출
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		

		// 읽음 상태 처리
		if(path.equals("/setRead.noti")) {
			try {
				long notiNum = Long.parseLong(request.getParameter("notiNum"));
				boolean setReadIsSuccess = NotificationDao.getInstance().notiSetRead(notiNum);
				System.out.println(setReadIsSuccess ? notiNum + "번 알림 읽음처리 성공" : notiNum + "번 알림 읽음처리 실패");
		        if (setReadIsSuccess) {
		            response.setStatus(200);
		        } else {
		            response.setStatus(500); // 처리 실패
		        }

			} catch (Exception e) {
				System.out.println("notiNum 파라미터: " + request.getParameter("notiNum"));
		        e.printStackTrace();
		        response.setStatus(500); // 서버 에러
			}
		}
		
		
		
		// 삭제 처리
		if(path.equals("/delete.noti")) {
			try {
				long notiNum = Long.parseLong(request.getParameter("notiNum"));
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
		
		
		
		
		// Insert 처리
		if(path.equals("/commentSave.noti")) {
			try {
				boolean isCommentSuccess = true; // 댓글 서블릿과 합치기 전 임시 변수
				
				if(isCommentSuccess) {
					
					long notiRecipientNum = Long.valueOf(request.getParameter("notiRecipientNum"));
					long notiSenderNum = Long.valueOf(request.getParameter("notiSenderNum"));
					int notiTypeCode = Integer.parseInt(request.getParameter("notiTypeCode"));
					int notiTargetTypeCode = Integer.parseInt(request.getParameter("notiTargetTypeCode"));
					String notiTargetNum = request.getParameter("notiTargetNum");
					String notiMessage = request.getParameter("notiMessage");
					
			
					NotificationDto notiDto = new NotificationDto();
					
					notiDto.setNotiRecipientNum(notiRecipientNum);
					notiDto.setNotiSenderNum(notiSenderNum);
					notiDto.setNotiTypeCode(notiTypeCode);
					notiDto.setNotiTargetTypeCode(notiTargetTypeCode);
					notiDto.setNotiTargetNum(notiTargetNum);
					notiDto.setNotiMessage(notiMessage);
					
					boolean isNotiSuccess = NotificationDao.getInstance().notiInsert(notiDto);
					
					if(isNotiSuccess) {
						System.out.println("알림 데이터 저장 성공");
						response.sendRedirect("리디렉션 경로");
					} else {
						System.out.println("알림 데이터 저장 실패");
						response.sendRedirect("리디렉션 경로");
					}
				}
			} catch(Exception e) {
		        e.printStackTrace();
		        response.setStatus(500);
			}
		}
	}
}





