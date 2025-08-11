package controller.review;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.admin.StayInfoDao;
import model.noti.NotificationDao;
import model.noti.NotificationDto;
import model.review.ReviewDao;
import model.review.ReviewDto;


@WebServlet("/review/submit")
public class ReviewSubmitServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 파라미터 가져오기
		String bookNum = req.getParameter("bookNum");
		String usersId = req.getParameter("usersId");
		String ratingStr  = req.getParameter("rating");
		String comment = req.getParameter("comment");
		String stayNumStr = req.getParameter("stayNum"); 
		
		// 알림 INSERT에 필요한 데이터
		long usersNum = Long.parseLong(req.getParameter("usersNum")); 
		long stayUsersNum = Long.parseLong(req.getParameter("stayUsersNum")); 
		
		// 만약 예약번호, 사용자, 별점, 숙소번호  중 한 개라도 비어있다면 
		if (bookNum == null || bookNum.isBlank()
			    || usersId == null || usersId.isBlank()
			    || ratingStr == null || ratingStr.isBlank()
			    || stayNumStr == null || stayNumStr.isBlank()) {

			    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 정보 누락");
			    return;
			}
		int rating = Integer.parseInt(ratingStr);
		long stayNum = Long.parseLong(stayNumStr);
		
		ReviewDto dto = new ReviewDto();
		dto.setBookNum(bookNum);
		dto.setUsersId(usersId);
		dto.setRating(rating);
		dto.setComment(comment);
		dto.setReviewStayNum(stayNum);
		
		// DB 저장
		boolean result = ReviewDao.getInstance().insert(dto);
		
		
		// 결제 완료 시 예약 확정 알림 INSERT
		if(result) {
			long notiRecipientNum = stayUsersNum; // 숙소 관리자 num
			long notiSenderNum = usersNum; // 리뷰 남긴 유저 num
			int notiTypeCode = 30;
			int notiTargetTypeCode = 30;
			String notiTargetNum = String.valueOf(stayNum); // 숙소 페이지로 이동하기 위한 숙소 num
			String notiMessage = comment; // 리뷰 메시지
			
			NotificationDto notiDto = new NotificationDto();
			
			notiDto.setNotiRecipientNum(notiRecipientNum);
			notiDto.setNotiSenderNum(notiSenderNum);
			notiDto.setNotiTypeCode(notiTypeCode);
			notiDto.setNotiTargetTypeCode(notiTargetTypeCode);
			notiDto.setNotiTargetNum(notiTargetNum);
			notiDto.setNotiMessage("신규 리뷰");
			notiDto.setNotiImageType("stay");
			
			boolean isNotiSuccess = NotificationDao.getInstance().notiInsert(notiDto);
			
			if(isNotiSuccess) {
				System.out.println("알림 데이터 저장 성공");
			} else {
				System.out.println("알림 데이터 저장 실패");
			}
		}
		
		
		
		// 만약 저장이 된다면
		if(result) {
			// 임시로 상세페이지 이동
			resp.sendRedirect(req.getContextPath() + "/page/page-view.jsp?stayNum=" + stayNum);
		}else {
			// 에러 페이지
			resp.sendRedirect(req.getContextPath() + "/index.jsp");
		}
	}
}
