package model.room;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.book.BookDto;
import model.page.StayDto;
import model.util.DBConnector;

public class RoomDao {
	//싱글톤 구조
	//자신의 객체를 담을 static 필드
	private static RoomDao dao;
	//static 초기화 블럭 (클래스 로딩 시 최초 1회 실행)
	static {
		dao = new RoomDao(); // RoomDao 객체를 미리 생성해서 static 필드에 저장		
	}
	//외부에서 RoomDao 객체 생성하지 못하도록 생성자를 private 로 막기
	private RoomDao() {}
	//외부에서 dao 객체의 참조값이 필요할 때 리턴해주는 public static 메서드 통해 접근
	public static RoomDao getInstance() {
		return dao;
	}
	
	public List<RoomDto> getListByStayNum(long stayNum) {
		List<RoomDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = new DBConnector().getConn();

			String sql = """
				SELECT r.*, s.STAY_NAME AS room_stay_name, s.STAY_FACILITIES AS room_stay_facilities
				FROM ROOM r
				JOIN STAY s ON r.ROOM_STAY_NUM = s.STAY_NUM
				WHERE r.ROOM_STAY_NUM = ?
				ORDER BY r.ROOM_NUM ASC
			""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, stayNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				RoomDto dto = new RoomDto();
				
				dto.setRoomNum(rs.getLong("ROOM_NUM"));
				dto.setRoomStayNum(rs.getInt("ROOM_STAY_NUM"));
				dto.setRoomName(rs.getString("ROOM_NAME"));
				dto.setRoomType(rs.getString("ROOM_TYPE"));
				dto.setRoomPrice(rs.getInt("ROOM_PRICE"));
				dto.setRoomAdultMax(rs.getInt("ROOM_ADULT_MAX"));
				dto.setRoomChildrenMax(rs.getInt("ROOM_CHILDREN_MAX"));
				dto.setRoomInfantMax(rs.getInt("ROOM_INFANT_MAX"));
				dto.setRoomCheckIn(rs.getTimestamp("ROOM_CHECKIN_DATE"));
				dto.setRoomCheckOut(rs.getTimestamp("ROOM_CHECKOUT_DATE"));
				dto.setRoomBlockDate(rs.getTimestamp("ROOM_BLOCK_DATE"));
				dto.setRoomContent(rs.getString("ROOM_CONTENT"));
				dto.setRoomPaxMax(rs.getInt("ROOM_PAX_MAX"));
				dto.setRoomStayName(rs.getString("ROOM_STAY_NAME"));
				dto.setRoomStayFacilities(rs.getString("ROOM_STAY_FACILITIES"));
				// 리스트에 Dto 추가
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}

		return list;
	}
	

	public RoomDto getByNum(long roomNum) {
	    RoomDto dto = null;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    String sql = """
	        SELECT *
	        FROM ROOM
	        WHERE ROOM_NUM = ?
	    """;

	    try {
	        conn = DBConnector.getConn();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, roomNum);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            dto = new RoomDto();
	            dto.setRoomNum(rs.getLong("ROOM_NUM"));
	            dto.setRoomStayNum(rs.getInt("ROOM_STAY_NUM"));
	            dto.setRoomName(rs.getString("ROOM_NAME"));
	            dto.setRoomType(rs.getString("ROOM_TYPE"));
	            dto.setRoomPrice(rs.getInt("ROOM_PRICE"));
	            dto.setRoomAdultMax(rs.getInt("ROOM_ADULT_MAX"));
	            dto.setRoomChildrenMax(rs.getInt("ROOM_CHILDREN_MAX"));
	            dto.setRoomInfantMax(rs.getInt("ROOM_INFANT_MAX"));
	            dto.setRoomCheckIn(rs.getTimestamp("ROOM_CHECKIN_DATE"));
	            dto.setRoomCheckOut(rs.getTimestamp("ROOM_CHECKOUT_DATE"));
	            dto.setRoomBlockDate(rs.getTimestamp("ROOM_BLOCK_DATE"));
	            dto.setRoomContent(rs.getString("ROOM_CONTENT"));
	            dto.setRoomPaxMax(rs.getInt("ROOM_PAX_MAX"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBConnector.close(rs, pstmt, conn);
	    }

	    return dto;
	}

	// ! STAY 숙소 번호 받아오기
	// 특정 숙소 번호(stayNum)를 받아 해당 숙소의 객실 목록을 반환하는 메서드
	
	// 숙소 번호(stayNum)에 해당하는 모든 객실 정보(객실 리스트 & 숙소명, 숙소위치, 숙소 전화번호 함께) 조회
	public List<RoomDto> getRoomListByStayNum(int stayNum) {
		List<RoomDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = new DBConnector().getConn();

			String sql = """
				SELECT r.*, s.STAY_NAME AS room_stay_name, s.STAY_FACILITIES AS room_stay_facilities
				FROM ROOM r
				JOIN STAY s ON r.ROOM_STAY_NUM = s.STAY_NUM
				WHERE r.ROOM_STAY_NUM = ?
				ORDER BY r.ROOM_NUM ASC
			""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, stayNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				RoomDto dto = new RoomDto();
				
				dto.setRoomNum(rs.getLong("ROOM_NUM"));
				dto.setRoomStayNum(rs.getInt("ROOM_STAY_NUM"));
				dto.setRoomName(rs.getString("ROOM_NAME"));
				dto.setRoomType(rs.getString("ROOM_TYPE"));
				dto.setRoomPrice(rs.getInt("ROOM_PRICE"));
				dto.setRoomAdultMax(rs.getInt("ROOM_ADULT_MAX"));
				dto.setRoomChildrenMax(rs.getInt("ROOM_CHILDREN_MAX"));
				dto.setRoomInfantMax(rs.getInt("ROOM_INFANT_MAX"));
				dto.setRoomCheckIn(rs.getTimestamp("ROOM_CHECKIN_DATE"));
				dto.setRoomCheckOut(rs.getTimestamp("ROOM_CHECKOUT_DATE"));
				dto.setRoomBlockDate(rs.getTimestamp("ROOM_BLOCK_DATE"));
				dto.setRoomContent(rs.getString("ROOM_CONTENT"));
				dto.setRoomPaxMax(rs.getInt("ROOM_PAX_MAX"));
				dto.setRoomStayName(rs.getString("ROOM_STAY_NAME"));
				dto.setRoomStayFacilities(rs.getString("ROOM_STAY_FACILITIES"));
				// 리스트에 Dto 추가
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}

		return list;
	}
	
	
	
	// 선택한 특정 객실의 객실 상세정보 조회
	public List<RoomDto> getRoomListByRoomNum(long roomNum) {
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		List<RoomDto> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
	    try {
	        conn = new DBConnector().getConn();
	        // 실행할 sql 문
	        String sql = """
		            SELECT *
		            FROM ROOM
		            WHERE ROOM_NUM = ?
		        """;
	        pstmt = conn.prepareStatement(sql);
	        // ? 에 바인딩
	        pstmt.setLong(1, roomNum);
	        
	        // select 문 실행하고 결과를 ResultSet 으로 받아온다
	        rs = pstmt.executeQuery();
	        // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해 줄 객체에 담는다
	        while (rs.next()) {
	            RoomDto dto = new RoomDto();
	            dto.setRoomNum(rs.getLong("room_num"));
	            dto.setRoomStayNum(rs.getInt("room_stay_num"));
	            dto.setRoomName(rs.getString("room_name"));
	            dto.setRoomType(rs.getString("room_type"));
	            dto.setRoomPrice(rs.getInt("room_price"));
	            dto.setRoomAdultMax(rs.getInt("room_adult_max"));
	            dto.setRoomChildrenMax(rs.getInt("room_children_max"));
	            dto.setRoomInfantMax(rs.getInt("room_infant_max"));
	            dto.setRoomPaxMax(rs.getInt("room_pax_max"));
	            dto.setRoomCheckIn(rs.getTimestamp("room_checkin_date"));
	            dto.setRoomCheckOut(rs.getTimestamp("room_checkout_date"));
	            dto.setRoomBlockDate(rs.getTimestamp("room_block_date"));
	            dto.setRoomContent(rs.getString("room_content"));
	            
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣기

		return list;
	}
	
}
	



	


