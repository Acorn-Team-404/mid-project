package model.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.util.DBConnector;

public class BookDao {
	// 자신의 참조값을 저장할 static 필드
	private static BookDao dao;
	// static 초기화 블럭에서 객체 생성해서 static 필드에 저장
	static {
		dao = new BookDao();
	}
	// 외부에서 객체 생성하지 못하도록 생성자의 접근 지정자를 private 로 지정
	private BookDao() {}
	// 참조값을 리턴해주는 static 메소드 제공
	public static BookDao getInstance() {
		return dao;
	}
	
	// 예약 상태 업데이트하는 메서드
	public void updateBookStatus(Connection conn, String bookNum) throws SQLException {
	       String sql = """
	       		
	       		UPDATE BOOKING
	       		SET book_status_code = ? 
	       		WHERE BOOK_STATUS_GROUP_ID = "예약상태" 
	       		AND BOOK_NUM = ?
	       		
	       		""";
	       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           pstmt.setInt(1, 11); //예약 확정
	           pstmt.setString(2,bookNum);
	           pstmt.executeUpdate();
	       }
	   }
	// 해당 숙소의 객실 목록
	/*public List<RoomDto> getRoomByStayNum(int stayNum){
		List<RoomDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = """
			SELECT room_num, room_name
			FROM room
			WHERE room_stay_num = ?
			""";
		try {
			conn = DBConnector.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				RoomDto dto = new RoomDto();
				dto.setRoomNum(rs.getInt("room_num"));
				dto.setRoomName(rs.getString("room_name"));
				list.add(dto);
			}
		} catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBConnector.close(rs, pstmt, conn);
	    }
	    return list;
	}*/
	
	// 예약 번호 생성 메소드
	public String generateBookNum() {
		String bookNum = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnector.getConn();
			// 오늘 날짜 값 추출
			String firstNum = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			// 시퀀스에서 번호 추출
			String sql = "SELECT book_seq.NEXTVAL FROM dual";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			// 시퀀스 숫자를 받아와 finalNum 에 저장
			int finalNum = 0;
			if(rs.next()) {
				// 첫번째 행의 값
				finalNum = rs.getInt(1);
			}
			
			bookNum = firstNum + "-" + String.format("%04d", finalNum);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return bookNum;
	}
	
	// 객실 금액 조회
	public int getRoomPrice(int roomNum) {
		int price = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnector.getConn();
			String sql = """
					SELECT room_price FROM room WHERE room_num = ?	
					""";
				
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, roomNum);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				price = rs.getInt("room_price");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}	
		return price;
	}
	
	// 예약하기 메소드
	public boolean insert(BookDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int rowCount = 0;
		
		String sql = """
			INSERT INTO booking(book_num, book_users_num, book_room_num, book_stay_num,
			book_checkin_date, book_checkout_date,
			book_adult, book_children, book_infant, book_total_pax,
			book_extra_bed, book_infant_bed, book_checkin_time, book_request,
			book_total_amount, book_status_code, book_created_at)
			VALUES(?, ?, ?, ?, ?, ? ,? ,? ,?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)
			""";
		try {
			conn = DBConnector.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getBookNum());
			pstmt.setLong(2, dto.getBookUsersNum());
			pstmt.setInt(3, dto.getBookRoomNum());
			pstmt.setInt(4,dto.getBookStayNum());
			pstmt.setString(5,dto.getBookCheckIn());
			pstmt.setString(6,dto.getBookCheckOut());
			pstmt.setInt(7, dto.getBookAdult());
			pstmt.setInt(8, dto.getBookChildren());
			pstmt.setInt(9, dto.getBookInfant());
			pstmt.setInt(10, dto.getBookTotalPax());
			pstmt.setInt(11, dto.getBookExtraBed());
			pstmt.setInt(12, dto.getBookInfantBed());
			pstmt.setString(13, dto.getBookCheckInTime());
			pstmt.setString(14, dto.getBookRequest());
			pstmt.setInt(15, dto.getBookTotalAmount());
			pstmt.setInt(16, dto.getBookStatusCode());
			
			// 디버깅용 출력
	        System.out.println("디버깅: book_users_num = " + dto.getBookUsersNum());
			
			rowCount = pstmt.executeUpdate();		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		
		// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아 낼 수 있다
		if (rowCount > 0) {
			return true; // 작업 성공이라는 의미에서 true return
		} else {
			return false; // 작업 실패라는 의미에서 false return
		}
	}
	
	
}
