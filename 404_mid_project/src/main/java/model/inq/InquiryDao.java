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
	
	
	// 알림 INSERT를 위해 미리 sequence 값을 가져오는 메서드
	public long getSequence() {
		long sequence=0;
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT inquiry_seq.NEXTVAL AS sequence
				FROM DUAL
			""";
			pstmt = conn.prepareStatement(sql);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			if (rs.next()) {
				sequence = rs.getLong("sequence");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return sequence;
	}
	
	
	//문의글을 DB 에 저장하는 메소드
	public boolean insert(InquiryDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
				INSERT INTO inquiry
				(inq_num, inq_stay_num, inq_users_num, inq_title, inq_content, inq_type)
				VALUES(?, ?, ?, ?, ?, ?)
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, dto.getNum());
			pstmt.setObject(2, dto.getStayNum());
			pstmt.setLong(3, dto.getUsersNum());
			pstmt.setString(4, dto.getTitle());
			pstmt.setString(5, dto.getContent());
			pstmt.setString(6, dto.getType());
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}

		//변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아 낼수 있다.
		if (rowCount > 0) {
			return true; //작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; //작업 실패라는 의미에서 false 리턴하기
		}
	}
	
	//usersNum 으로 전체 목록 리턴하는 메소드
	public List<InquiryDto> selectByUser(Long usersNum){
		List<InquiryDto> list=new ArrayList<>();
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT inq_num, inq_title, inq_content, inq_type, stay_name, inq_created_at, inq_is_answered, inq_answer, inq_answered_at
				FROM inquiry
				LEFT JOIN stay ON inq_stay_num=stay_num
				WHERE inq_users_num = ?
				ORDER BY inq_num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setLong(1, usersNum);
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
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	// 특정 회원이 문의한 전체 글의 개수 리턴하는 메소드
	public long getCountByUser(Long usersNum) {
		long count=0;
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT COUNT(*) AS count
				FROM inquiry
				WHERE inq_users_num = ?				
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setLong(1, usersNum);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			if (rs.next()) {
				count=rs.getLong("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return count;
	}
	
	// 특정 page 에 해당하는 row 만 select 해서 리턴하는 메소드
	// InquiryDto 객체에 startRowNum 과 endRowNum 을 담아와서 select
	public List<InquiryDto> selectPageByUser(InquiryDto dto){
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		List<InquiryDto> list=new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT *
				FROM
					(SELECT result1.*, ROWNUM AS rnum
					FROM	
						(SELECT inq_num, inq_title, inq_content, inq_type, stay_name,
							inq_created_at, inq_is_answered, inq_answer, inq_answered_at
						FROM inquiry
						LEFT JOIN stay ON inq_stay_num=stay_num
						WHERE inq_users_num = ?
						ORDER BY inq_num DESC
						) result1
					)
				WHERE rnum BETWEEN ? AND ?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, dto.getUsersNum());
			pstmt.setLong(2, dto.getStartRowNum());
			pstmt.setLong(3, dto.getEndRowNum());
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				InquiryDto dto2=new InquiryDto();
				dto2.setNum(rs.getLong("inq_num"));
				dto2.setTitle(rs.getString("inq_title"));
				dto2.setContent(rs.getString("inq_content"));
				dto2.setType(rs.getString("inq_type"));
				dto2.setStayName(rs.getString("stay_name"));
				dto2.setCreatedAt(rs.getString("inq_created_at"));
				dto2.setIsAnswered(rs.getLong("inq_is_answered"));
				dto2.setAnswer(rs.getString("inq_answer"));
				dto2.setAnsweredAt(rs.getString("inq_answered_at"));
				
				list.add(dto2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;

	}
	
	//특정 기간에 해당하는 row 만 select 해서 리턴하는 메소드
	public List<InquiryDto> selectByCreatedAt(Long usersNum, String startDate, String endDate){
		List<InquiryDto> list=new ArrayList<>();
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT inq_num, inq_title, inq_content, inq_type, stay_name, inq_created_at, inq_is_answered, inq_answer, inq_answered_at
				FROM inquiry
				LEFT JOIN stay ON inq_stay_num=stay_num
				WHERE inq_users_num = ?
					AND inq_created_at >= TO_DATE(?, 'YYYY-MM-DD')
					AND inq_created_at < TO_DATE(?, 'YYYY-MM-DD')+1
				ORDER BY inq_num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setLong(1, usersNum);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
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
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	//특정 기간에 해당하는 row 의 개수를 리턴하는 메소드
	public long getCountByCreatedAt(Long usersNum, String startDate, String endDate){
		long count=0;
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT COUNT(*) AS count
				FROM inquiry
				WHERE inq_users_num = ?
					AND inq_created_at >= TO_DATE(?, 'YYYY-MM-DD')
					AND inq_created_at < TO_DATE(?, 'YYYY-MM-DD')+1
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setLong(1, usersNum);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			if (rs.next()) {
				count=rs.getLong("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return count;
	}
	
	// 특정 page 와 기간에 해당하는 row 만 select 해서 리턴하는 메소드
	// InquiryDto 객체에 startRowNum 과 endRowNum 을 담아와서 select 
	public List<InquiryDto> selectPageByCreatedAt(InquiryDto dto){
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		List<InquiryDto> list=new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT *
				FROM
					(SELECT result1.*, ROWNUM AS rnum
					FROM	
						(SELECT inq_num, inq_title, inq_content, inq_type, stay_name,
							inq_created_at, inq_is_answered, inq_answer, inq_answered_at
						FROM inquiry
						LEFT JOIN stay ON inq_stay_num=stay_num
						WHERE inq_users_num = ?
						AND inq_created_at >= TO_DATE(?, 'YYYY-MM-DD')
						AND inq_created_at < TO_DATE(?, 'YYYY-MM-DD')+1
						ORDER BY inq_num DESC
						) result1
					)
				WHERE rnum BETWEEN ? AND ?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, dto.getUsersNum());
			pstmt.setString(2, dto.getStartDate());
			pstmt.setString(3, dto.getEndDate());
			pstmt.setLong(4, dto.getStartRowNum());
			pstmt.setLong(5, dto.getEndRowNum());
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				InquiryDto dto2=new InquiryDto();
				dto2.setNum(rs.getLong("inq_num"));
				dto2.setTitle(rs.getString("inq_title"));
				dto2.setContent(rs.getString("inq_content"));
				dto2.setType(rs.getString("inq_type"));
				dto2.setStayName(rs.getString("stay_name"));
				dto2.setCreatedAt(rs.getString("inq_created_at"));
				dto2.setIsAnswered(rs.getLong("inq_is_answered"));
				dto2.setAnswer(rs.getString("inq_answer"));
				dto2.setAnsweredAt(rs.getString("inq_answered_at"));
				list.add(dto2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	}
	
	//글 하나의 정보를 리턴하는 메소드(글번호 조회)
	public InquiryDto getByNum(long num) {
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		InquiryDto dto = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT users_num, inq_title, inq_content, inq_type, stay_name, inq_is_answered, inq_answer
				FROM inquiry
				JOIN users ON users_num=inq_users_num
				LEFT JOIN stay ON stay_num=inq_stay_num
				WHERE inq_num = ?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, num);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				dto=new InquiryDto();
				dto.setTitle(rs.getString("inq_title"));
				dto.setContent(rs.getString("inq_content"));
				dto.setType(rs.getString("inq_type"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setUsersNum(rs.getLong("users_num"));
				dto.setIsAnswered(rs.getLong("inq_is_answered"));
				dto.setAnswer(rs.getString("inq_answer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return dto;
	}
	
	//답변 저장하는 메소드
	public boolean updateAnswer(InquiryDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
				UPDATE inquiry
				SET inq_is_answered = 1, inq_answer = ?, inq_answered_at = SYSDATE
				WHERE inq_num = ?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, dto.getAnswer());
			pstmt.setLong(2, dto.getNum());
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		// 작업의 성공 여부 (변화된 row 의 갯수로 판단)
		if (rowCount > 0) {
			return true; // 작업 성공
		} else {
			return false; // 작업 실패
		}
	}
}