package controller.book;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
		if (usersId == null) {
			res.sendRedirect("/login.jsp");
			return;
		}
		// 세션에서 회원의 아이디 가져오기
		UserDto user = UserDao.getInstance().getByUserId(usersId);

		if (user != null) {
			req.setAttribute("usersNum", user.getUsersNum());
			req.setAttribute("usersName", user.getUsersName());
			req.setAttribute("email", user.getUsersEmail());
			req.setAttribute("phone", user.getUsersPhone());
		}
		req.getRequestDispatcher("/booking/booking-form.jsp").forward(req, res);

		// 숙소 정보 가져오기
		/*
		 * int stayNum = 세션이나 파라미터로 받기 List<RoomDto> roomList =
		 * RoomDao.getInstance().getRoomsByStayNum(stayNum);
		 * 
		 * req.setAttribute("roomList", roomList); req.setAttribute("bookStayNum",
		 * stayNum);
		 */
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);

		String usersId = (String) session.getAttribute("usersId");

		if (usersId == null) {
			res.sendRedirect("/login.jsp");
			return;
		}
		
		long usersNum = UserDao.getInstance().getByUserId(usersId).getUsersNum();

		// DTO 세팅
		BookDto dto = new BookDto();

		dto.setBookUsersNum(usersNum);

		// 예약 번호
		String bookNum = BookDao.getInstance().generateBookNum();
		dto.setBookNum(bookNum);

		// 숙소 번호
		int stayNum = Integer.parseInt(req.getParameter("bookStayNum"));
		dto.setBookStayNum(stayNum);
		// 객실 번호
		int roomNum = Integer.parseInt(req.getParameter("bookRoomNum"));
		dto.setBookRoomNum(roomNum);

		String checkIn = req.getParameter("checkIn");
		dto.setBookCheckIn(checkIn);
		String checkOut = req.getParameter("checkOut");
		dto.setBookCheckOut(checkOut);

		LocalDate checkInDate = LocalDate.parse(checkIn);
		LocalDate checkOutDate = LocalDate.parse(checkOut);

		long betweenDay = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
		if (betweenDay <= 0) {
			// 체크인 날짜는 체크아웃 날짜보다 이전이어야 한다
			req.setAttribute("errorMsg", "체크아웃 날짜는 체크인 날짜보다 늦어야 합니다");
			req.getRequestDispatcher("/booking/booking-form.jsp").forward(req, res);
			return;
		}

		int adult = Integer.parseInt(req.getParameter("adult"));
		dto.setBookAdult(adult);
		int children = Integer.parseInt(req.getParameter("children"));
		dto.setBookChildren(children);
		int infant = Integer.parseInt(req.getParameter("infant"));
		dto.setBookInfant(infant);
		// 총 인원
		int totalPax = adult + children + infant;
		dto.setBookTotalPax(totalPax);

		// 추가 침대 옵션 : 기본은 0, 선택하는 경우 1
		String[] beds = req.getParameterValues("bed");
		int extraBed = 0;
		int infantBed = 0;
		if (beds != null) {
			for (String bed : beds) {
				if ("extraBed".equals(bed))
					extraBed = 1;
				if ("infantBed".equals(bed))
					infantBed = 1;
			}
		}

		dto.setBookExtraBed(extraBed);
		dto.setBookInfantBed(infantBed);

		String checkInTime = req.getParameter("checkInTime");
		String bookRequest = req.getParameter("bookRequest");
		dto.setBookCheckInTime(checkInTime);
		dto.setBookRequest(bookRequest);

		// 총 결제 금액 계산 공식 dao 만들어서 넣기 ~
		int roomPrice = BookDao.getInstance().getRoomPrice(roomNum);

		// roomNum 잘못되거나 DB문제 있어서 0원 리턴 방지
		if (roomPrice <= 0) {
			req.setAttribute("errorMsg", "객실 요금 정보를 가져올 수 없습니다.");
			req.getRequestDispatcher("/booking/booking-form.jsp").forward(req, res);
			return;
		}

		int totalAmount = (int) (roomPrice * betweenDay);
		dto.setBookTotalAmount(totalAmount);

		// 예약 상태 (예약 대기)
		dto.setBookStatusCode(10);

		boolean isSuccess = BookDao.getInstance().insert(dto);

		if (!isSuccess) {
			req.setAttribute("errorMsg", "예약 실패했습니다.");
			req.getRequestDispatcher("/booking/booking-form.jsp").forward(req, res);
			return;

		} else {
			// dto전체를 넘길 필요는 없고 결제 페이지에서는 bookNum으로 예약정보를 가져오는 쿼리 사용
			res.sendRedirect(req.getContextPath() + "/pay/payments.jsp?bookNum=" + dto.getBookNum());

		}

	}
}
