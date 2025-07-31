package controller.book;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.book.BookDao;
import model.book.BookDto;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("/booking/submit")
public class BookSaveServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 세션에서 로그인 사용자 정보 가져오기 
		HttpSession session = req.getSession(false);
		 String usersId = (session != null) ? (String) session.getAttribute("usersId") : null;
	        
		 // 세션에서 회원의 아이디 가져오기
		 UserDto user = UserDao.getInstance().getByUserId(usersId);
	        
		 if (user != null) {
			 req.setAttribute("usersNum", user.getUsersNum());
			 req.setAttribute("usersName", user.getUsersName());
			 req.setAttribute("email", user.getUsersEmail());
			 req.setAttribute("phone", user.getUsersPhone());
		 }
		 req.getRequestDispatcher("/booking/booking-form.jsp").forward(req, res);
	 }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		
		String usersId = (String)session.getAttribute("usersId");
		long usersNum = UserDao.getInstance().getByUserId(usersId).getUsersNum();
		
		// DTO 세팅
		BookDto dto = new BookDto();
		
		// 예약 번호
		String bookNum = BookDao.getInstance().generateBookNum();		
		dto.setBookNum(bookNum);
		
		int roomId = Integer.parseInt(req.getParameter("bookRoomId"));
		
		// 숙소 값도 해야함
		
		String checkIn = req.getParameter("checkIn");
		String checkOut =  req.getParameter("checkOut");
		
		int adult = Integer.parseInt(req.getParameter("adult"));
		int children = Integer.parseInt(req.getParameter("children"));
		int infant = Integer.parseInt(req.getParameter("infant"));
		int totalPax = adult + children + infant;
		
		String bedParam = req.getParameter("bed");
		int extraBed = 0;
		int infantBed = 0;
		
		String[] beds = req.getParameterValues("bed");
		if(beds != null) {
			for(String bed : beds) {
				if("extraBed".equals(bed)) extraBed = 1;
				if("infantBed".equals(bed)) infantBed = 1;
			}
		}
		
		String checkInTime = req.getParameter("checkInTime");
		
		String bookRequest = req.getParameter("bookRequest");

		
		int totalAmount = 0;
		
		// 예약 상태 (예약 대기)
		int bookStatusCode = 10;
		
		
	}
}
