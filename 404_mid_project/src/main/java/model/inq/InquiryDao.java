package model.inq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class InquiryDao {
	private static InquiryDao dao;
	static {
		dao=new InquiryDao();
	}
	//생성자를 private 로 해서 외부에서 객체 생성하지 못하도록 
	private InquiryDao() {}
	//자신의 참조값을 리턴해주는 static 메소드를 제공한다. 
	public static InquiryDao getInstance() {
		return dao;
	}
	
	//문의글을 DB 에 저장하는 메소드
	public boolean insert(InquiryDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = new DBConnector().getConn();
			String sql = """
				INSERT INTO inquiry
				(inq_num, inq_stay_num, inq_users_num, inq_title, inq_content, inq_type)
				VALUES(inquiry_seq.NextVAL, ?, ?, ?, ?, ?)
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, dto.getStayNum());
			pstmt.setLong(2, dto.getUsersNum());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getType());
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
	
	//usersNum 으로 전체 목록 리턴하는 메소드
	public List<InquiryDto> selectAll(Long userNum){
		List<InquiryDto> list=new ArrayList<>();
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBConnector().getConn();
			//실행할 sql문
			String sql = """
				SELECT inq_num, inq_title, inq_content, inq_type, stay_name, inq_created_at, inq_is_answered, inq_answer, inq_answered_at
				FROM inquiry
				LEFT JOIN stay ON inq_stay_num=stay_num
				WHERE users_num = ?
				ORDER BY inq_num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setLong(1, userNum);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			while (rs.next()) {
				InquiryDto dto=new InquiryDto();
				dto.setNum(rs.getLong("inq_num"));
				dto.setTitle(rs.getString("inq_title"));
				dto.setContent(rs.getString("inq_content"));
				dto.setType(rs.getString("inq_type"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setCreatedAt(rs.getString("inq_created_at"));
				dto.setIsAnswered(rs.getLong("inq_is_answered"));
				dto.setAnswer(rs.getString("inq_answer"));
				dto.setAnsweredAt(rs.getString("inq_answered_at"));
				
				list.add(dto);
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
		return list;
	}
	
	//특정 기간에 해당하는 row 만 select 해서 리턴하는 메소드
	public List<InquiryDto> selectByCreatedAt(String startDate, String endDate){
		List<InquiryDto> list=new ArrayList<>();
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBConnector().getConn();
			//실행할 sql문
			String sql = """
				SELECT inq_num, inq_title, inq_content, inq_type, stay_name, inq_created_at, inq_is_answered, inq_answer, inq_answered_at
				FROM inquiry
				LEFT JOIN stay ON inq_stay_num=stay_num
				WHERE inq_created_at >= TO_DATE(?, 'YYYY-MM-DD') AND inq_created_at < TO_DATE(?, 'YYYY-MM-DD')+1
				ORDER BY inq_num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			while (rs.next()) {
				InquiryDto dto=new InquiryDto();
				dto.setNum(rs.getLong("inq_num"));
				dto.setTitle(rs.getString("inq_title"));
				dto.setContent(rs.getString("inq_content"));
				dto.setType(rs.getString("inq_type"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setCreatedAt(rs.getString("inq_created_at"));
				dto.setIsAnswered(rs.getLong("inq_is_answered"));
				dto.setAnswer(rs.getString("inq_answer"));
				dto.setAnsweredAt(rs.getString("inq_answered_at"));
				
				list.add(dto);
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
		return list;
	}
}
