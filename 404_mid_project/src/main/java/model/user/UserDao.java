package model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.util.DBConnector;

public class UserDao {

	private static UserDao dao;
	
	// static 초기화 블럭 (이클래스가 최초로 사용될때 한번 실행되는 블럭)
	static {
		//static 초기화 작업을 여기서 한다 (UserDao 객체를 생성해서 static 필드에 담는다)
		dao=new UserDao();
	}
	
	//외부에서 UserDao 객체를 생성하지 못하도록 생성자를 private 로 막는다.
	private UserDao() {}
	
	//UserDao 객체의 참조값을 리턴해주는 public static 메소드 제공
	public static UserDao getInstance() {
		//static 필드에 저장된 dao 의 참조값을 리턴해 준다. 
		return dao;
	}
	
	// 마이페이지 사용자 정보 조회
	public UserDto getUserById(String usersId) {
		UserDto dto = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBConnector().getConn();
			String sql = """
					SELECT users_num, users_id, users_name, users_email, users_phone, 
					users_birth, users_profile_image, users_updated_at 
					FROM users 
					WHERE users_id = ?

					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setString(1, usersId);
			// select 문 실행하고 결과를 ResultSet으로 받아온
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResulteSet 에 담긴 데이터를 추출해서 리턴 해 줄 객체에 담는다
			while (rs.next()) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		} return dto;
	}
	
	//아이디 중복 체크
	public boolean isIdExist(String usersId) {//아이디 중복이면 true
		
		boolean isIdExist = false;
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
			conn = new DBConnector().getConn();

			String sql = """
				SELECT users_id FROM users WHERE users_id=?
			""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usersId);

			rs = pstmt.executeQuery();

			if(rs.next()) {
				isIdExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return isIdExist;
        
	}
	
	
	//회원가입
	public boolean insert(UserDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = new DBConnector().getConn();
			String sql = """
				INSERT INTO users
				(users_num, users_id, users_name, users_pw, users_email, users_phone, users_birth, users_profile_image, users_role, users_updated_at, users_created_at)
				VALUES(users_seq.NEXTVAL, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, DEFAULT, SYSDATE, SYSDATE)

			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, dto.getUsersId());
			pstmt.setString(2, dto.getUsersName());
			pstmt.setString(3, dto.getUsersPw());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getPhone());
			pstmt.setString(6, dto.getBirth());
			pstmt.setString(7, dto.getBirth());
			
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		//변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아 낼수 있다.
		if (rowCount > 0) {
			return true; //작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; //작업 실패라는 의미에서 false 리턴하기
		}
		
	
	}
	
}
