package model.room;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class RoomDao {
	//싱글톤 구조
	//1. 자신의 객체를 담을 static 필드
	private static RoomDao dao;
	//2. static 초기화 블럭 (클래스 로딩 시 최초 1회 실행)
	static {
		dao = new RoomDao(); // RoomDao 객체를 미리 생성해서 static 필드에 저장		
	}
	//3. 외부에서 RoomDao 객체 생성하지 못하도록 생성자를 private 로 막기
	private RoomDao() {}
	//4. 외부에서 dao 객체의 참조값이 필요할 때 리턴해주는 public static 메서드 통해 접근
	public static RoomDao getInstance() {
		return dao;
	}
	
	// ! STAY 숙소 번호 받아오기
	// 특정 숙소 번호(stayNum)를 받아 해당 숙소의 객실 목록을 반환하는 메서드
	public List<RoomDto> getRoomListByStayNum(int stayNum) {
		List<RoomDto> roomTypes = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문 (숙소 번호에 해당하는 객실 타입들 조회)
			String sql = """
					SELECT * FROM ROOM
					WHERE ROOM_STAY_NUM = ?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setInt(1, stayNum);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체(Dto)에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				RoomDto room = new RoomDto();
				
				room.setRoomNum(rs.getLong("ROOM_NUM"));
				room.setRoomStayNum(rs.getInt("ROOM_STAY_NUM"));
				room.setRoomName(rs.getString("ROOM_NAME"));
				room.setRoomType(rs.getString("ROOM_TYPE"));
				room.setRoomPrice(rs.getInt("ROOM_PRICE"));
				room.setRoomAdultMax(rs.getInt("ROOM_ADULT_MAX"));
				room.setRoomChildrenMax(rs.getInt("ROOM_CHILDREN_MAX"));
				room.setRoomInfantMax(rs.getInt("ROOM_INFANT_MAX"));
				room.setRoomPaxMax(rs.getInt("ROOM_PAX_MAX"));
				room.setRoomContent(rs.getString("ROOM_CONTENT"));
				
				Date checkIn = rs.getDate("ROOM_CHECKIN_DATE");
				room.setRoomCheckIn(checkIn !=null ? checkIn.toString() : "");

				Date checkOut = rs.getDate("ROOM_CHECKOUT_DATE");
				room.setRoomCheckOut(checkOut != null ? checkOut.toString() : "");

				Date blockDate = rs.getDate("ROOM_BLOCK_DATE");
				room.setRoomBlockDate(blockDate != null ? blockDate.toString() : "");
				
				// 리스트에 Dto 추가
				roomTypes.add(room);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// DBConnector 유틸을 사용한 자원 해제 (null 체크 포함 가정)
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 변수명의 return 값 넣어야함!
		return roomTypes;
	}//특정 숙소의 객실 목록을 반환하는 메서드 ends
			

	

}
