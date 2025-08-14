package controller.book;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.book.BookDao;
import model.book.BookDto;
import model.book.GuidelineDao;
import model.book.GuidelineDto;
import model.page.StayDao;
import model.page.StayDto;
import model.room.RoomDao;
import model.room.RoomDto;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("/booking/submit")
public class BookSaveServlet extends HttpServlet {
	
	//중복데이터 방지 셋팅
	private void setBookingPageAttributes(HttpServletRequest req, int stayNum) {
	    StayDto stay = StayDao.getInstance().getByBookStayNum(stayNum);
	    req.setAttribute("stay", stay);
	    req.setAttribute("stayNum", Long.valueOf(stayNum));
	    
	    List<RoomDto> roomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
	    req.setAttribute("roomList", roomList);
	    
	    GuidelineDto guide = GuidelineDao.getInstance().getByGuideId(1);
	    req.setAttribute("guide", guide);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    HttpSession session = req.getSession();    
	    
	    String usersId = (String) session.getAttribute("usersId");
	    
	    if (usersId != null) {
			UserDto dto = UserDao.getInstance().getByUserId(usersId);
			if (dto != null) {
				req.setAttribute("usersId", dto.getUsersId());
				req.setAttribute("usersName", dto.getUsersName());
				req.setAttribute("email", dto.getUsersEmail());
				req.setAttribute("phone", dto.getUsersPhone());
			}
		}
	    
	    String stayNumStr = req.getParameter("stayNum");
	    System.out.println("bookStayNum parameter: " + stayNumStr);

	    if (stayNumStr != null && !stayNumStr.isEmpty()) {
	        int stayNum = Integer.parseInt(stayNumStr);

	        // 숙소 정보 가져오기
	        StayDto stay = StayDao.getInstance().getByBookStayNum(stayNum);
	        req.setAttribute("stay", stay);
	        req.setAttribute("stayNum", Long.valueOf(stayNum));
	        
	        // 객실 목록 가져오기
	        List<RoomDto> roomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
	        req.setAttribute("roomList", roomList);
	        
	        // 가이드 라인 정보 가져오기
	        GuidelineDto guide = GuidelineDao.getInstance().getByGuideId(1);
	        req.setAttribute("guide", guide);
	    }
	        
		// 최종적으로 booking-page.jsp로 forward
	    req.getRequestDispatcher("/booking/booking-page.jsp").forward(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String payToken = UUID.randomUUID().toString();
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
			setBookingPageAttributes(req, stayNum);
			req.getRequestDispatcher("/booking/booking-page.jsp").forward(req, res);
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

		String selectedBed = req.getParameter("selectedBed");
		dto.setBookExtraBed(selectedBed != null && selectedBed.contains("extraBed") ? 1 : 0);
		dto.setBookInfantBed(selectedBed != null && selectedBed.contains("infantBed") ? 1 : 0);
		
		String bookRequest = req.getParameter("bookRequest");
		dto.setBookRequest(bookRequest);
		
		String selectedCheckInTime = req.getParameter("selectedCheckInTime");
		dto.setBookCheckInTime(selectedCheckInTime);
		
		int totalAmount = Integer.parseInt(req.getParameter("totalAmountValue"));
		dto.setBookTotalAmount(totalAmount);
		
		// 예약 상태 (예약 대기)
		dto.setBookStatusCode(10);
		
		//내가 고른 날짜와 기존 예약되있는 것들 겹치는 거 있는지 서버단에서 검사
		if (BookDao.getInstance().isDateOverlap(dto)) {
		    req.setAttribute("errorMsg", "선택한 날짜에 이미 예약이 존재합니다. 다른 날짜를 선택해주세요.");
		    setBookingPageAttributes(req, stayNum);
		    req.getRequestDispatcher("/booking/booking-page.jsp").forward(req, res);
		    return;
		}
		
		boolean isSuccess = BookDao.getInstance().insert(dto);

		if (!isSuccess) {
			req.setAttribute("errorMsg", "예약 실패했습니다.");
			req.getRequestDispatcher("/booking/booking-form.jsp").forward(req, res);
			return;

		} else {
			// dto전체를 넘길 필요는 없고 결제 페이지에서는 bookNum으로 예약정보를 가져오는 쿼리 사용
			BookDto bookDto = BookDao.getInstance().getByBookNum(dto.getBookNum());
			StayDto stayDto = StayDao.getInstance().getByBookStayNum(bookDto.getBookStayNum());
			
			
			//세션에 UUID토큰 저장해서 요청 간에 유지하면서 결제 검증

			setBookingPageAttributes(req, stayNum);
			
			//결제 페이지에 한 번 보여줄 데이터라 요청 한 번에 사라짐
			session.setAttribute("payToken", payToken);
			session.setAttribute("bookDto", bookDto);
			session.setAttribute("stayDto", stayDto);
			
			res.sendRedirect(req.getContextPath() + "/pay/payments.jsp?bookNum=" + bookDto.getBookNum());

		}

	}
}
