package model.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (Exception e) { }
		}
		return bookNum;
	}
	
	// 로그인한 사용자 정보 가져오기
	
	
	// 선택한 예약 사항들을 추가하는 메소드
	
}
