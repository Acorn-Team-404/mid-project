package controller.room;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.page.StayDao;
import model.page.StayDto;
import model.room.RoomDao;
import model.room.RoomDto;


@WebServlet("/booking/roomtypes")
public class RoomTypesServlet extends HttpServlet {
	
	// 선택한 숙소 번호에 따라 해당 숙소의 객실 리스트 가져오기 (/booking/roomtypes?stayNum=100로 요청만 보내면, 해당 숙소의 객실 목록이 반복 출력 & 카드 선택 기능도 작동) 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // 숙소 번호
		int stayNum = 100; //기본값 임시
		// 1. URL 파라미터에서 숙소 번호 가져오기 (ex: /room/list?stayNum=100)
	    String stayNumParam = request.getParameter("stayNum");
	    // 2. 문자열을 int로 변환 (예외 방지를 위해 null 체크) 
	    if (stayNumParam != null && !stayNumParam.isEmpty()) {
			stayNum = Integer.parseInt(stayNumParam);
	    }
		// 3. RoomDao 객체를 이용해 해당 숙소 번호의 객실 리스트(roomTypes) 가져오기
		List<RoomDto> roomTypes = RoomDao.getInstance().getRoomListByStayNum(stayNum);
		// 4. room-info.jsp 페이지에서 사용할 수 있도록 request 에 담기
		request.setAttribute("roomTypes", roomTypes);
		// view(JSP)로 포워딩 (HTML 보여주는 역할만)
		request.getRequestDispatcher("/booking/room-info.jsp").forward(request, response);
	}
	
	
	
	
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		HttpSession session = req.getSession(false);	
//		// DTO 세팅
//		RoomDto dto = new RoomDto();
//		
//		dto.setRoomNum(roomNum);
//		// 숙소 번호
//		int stayNum = Integer.parseInt(req.getParameter("stayNum"));
//		dto.setRoomStayNum(stayNum);
//		// 객실 번호
//		int roomNum = Integer.parseInt(req.getParameter("roomNum"));
//		dto.setRoomNum(roomNum);
	
	
} //RoomTypesServlet 클래스 ends.
	

