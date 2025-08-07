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
		
		long pageNum = StayInfoDao.getInstance().getPageNumByStayNum(stayNum);
		
		// DB 저장
		boolean result = ReviewDao.getInstance().insert(dto);
		
		
		// 만약 저장이 된다면
		if(result) {
			// 임시로 상세페이지 이동
			resp.sendRedirect(req.getContextPath() + "/page/page-view.jsp?pageNum=" + pageNum);
		}else {
			// 에러 페이지
			resp.sendRedirect(req.getContextPath() + "/index.jsp");
		}
	}
}
