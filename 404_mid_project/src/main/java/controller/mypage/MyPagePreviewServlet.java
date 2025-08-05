package controller.mypage;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.book.BookDao;
import model.book.BookDto;

@WebServlet("/mypage/detail")
public class MyPagePreviewServlet  extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 예약번호 가져옴
		String bookNum = req.getParameter("bookNum");
		
		//만약 예약번호가 없거나, 비었다면 에러 응답
		if(bookNum == null || bookNum.isBlank()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "예약 번호가 누락 됨");
			return;
		}
		// Dao에서 예약정보 조회
		BookDao dao = BookDao.getInstance();
		BookDto dto = dao.getByBookNum(bookNum);
		
		// 예약 정보를 찾지 못한다면 에러 응답
		if(dto == null ) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "예약 정보 없음");
			return;
		}
		// jsp로 포워딩
		req.setAttribute("booking", dto);
		req.getRequestDispatcher("/my-page/preview-detail.jsp").forward(req, resp);
	}
}
