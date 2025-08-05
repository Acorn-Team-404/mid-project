package controller.room;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.room.RoomDao;
import model.room.RoomDto;

/*
 * enctype = "multipart/form-data" 형식의 폼이 전송되었을 때 처리할 서블릿 만들기
 * */
@WebServlet("/booking/roomtypes")
public class RoomTypesServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // !임시로 숙소번호:100 가정
		int stayNum = 100;
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
		// JSP로 포워딩 (HTML 보여주는 역할만)
		request.getRequestDispatcher("/booking/room-info.jsp").forward(request, response);
		
		// 더미 데이터 (DB 없이 확인용)
		//List<RoomDto> roomTypes = new ArrayList<>();

		RoomDto room = new RoomDto();
		room.setRoomNum(1);
		room.setRoomName("행복한 호텔");
		room.setRoomType("스탠다드 객실");
		room.setRoomAdultMax(6);
		room.setRoomPaxMax(12);
		room.setRoomPrice(440000);
		room.setRoomContent("햇살이 잘 들어오는 모던한 거실과 넓은 주방이 있는 객실입니다.");
		room.setRoomCheckIn(Timestamp.valueOf("2025-08-02 00:00:00"));
		room.setRoomCheckOut(Timestamp.valueOf("2025-08-03 00:00:00"));
		room.setRoomBlockDate(Timestamp.valueOf("2025-08-15 00:00:00"));


		roomTypes.add(room);

		request.setAttribute("roomTypes", roomTypes);
		request.getRequestDispatcher("/booking/room-info.jsp").forward(request, response);
	}
	
}
	

