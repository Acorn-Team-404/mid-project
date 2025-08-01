package model.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class PageDao {
	private static PageDao dao;
	static {
		dao=new PageDao();
	}
	private PageDao() {}
	public static PageDao getInstance() {
		return dao;
	}
	
	// 글 키워드
	public int getCountByKeyword(String keyword) {
		int count=0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return count;
	}
	
	// 글 수정
		public boolean update(PageDto dto) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
			int rowCount = 0;

			try {
				conn = DBConnector.getConn();
				String sql = """
					UPDATE page
					SET page_content=?, page_reserve=?, page_guide=?, page_refund=?
					WHERE page_num=?
				""";
				pstmt = conn.prepareStatement(sql);
				// ? 에 순서대로 필요한 값 바인딩
				pstmt.setString(1, dto.getPage_content());
				pstmt.setString(2, dto.getPage_reserve());
				pstmt.setString(3, dto.getPage_guide());
				pstmt.setString(4, dto.getPage_refund());
				pstmt.setLong(5, dto.getPage_num());
				// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
				rowCount = pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 닫을 땐 실행되는 순서의 역순으로 닫아야 함
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception e) {}
			}
			// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아낼 수 있다
			if (rowCount > 0) {
				return true; // 작업 성공이라는 의미에서 true 리턴하기
			} else {
				return false; // 작업 실패라는 의미에서 false 리턴하기
			}
		}
		
		// 글 삭제
		public boolean deleteByNum(int num) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
			int rowCount = 0;

			try {
				conn = DBConnector.getConn();
				String sql = """
					DELETE FROM page
					WHERE num=?
				""";
				pstmt = conn.prepareStatement(sql);
				// ? 에 순서대로 필요한 값 바인딩
				pstmt.setInt(1, num);
				// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
				rowCount = pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 닫을 땐 실행되는 순서의 역순으로 닫아야 함
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception e) {}
			}
			// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아낼 수 있다
			if (rowCount > 0) {
				return true; // 작업 성공이라는 의미에서 true 리턴하기
			} else {
				return false; // 작업 실패라는 의미에서 false 리턴하기
			}
		}
	
	// 글 전체 가져오기
	public List<PageDto> selectAll(){
		List<PageDto> list=new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT page_num, stay_name, user_name, page_created_at
				FROM page p
				INNER JOIN users u ON p.user_num=u.user_num
				INNER JOIN stay s ON p.stay_num=s.stay_num
				ORDER BY num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	}
	
	// 글 하나의 정보 불러오기
	public PageDto getByNum(long num) {
		PageDto dto=null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT stay_name, user_name, page_content, page_reserve, page_guide, page_refund
				FROM page p
				INNER JOIN users u ON p.user_num=u.user_num
				INNER JOIN stay s ON p.stay_num=s.stay_num
				WHERE page_num=?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, num);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				dto=new PageDto();
				dto.setPage_num(num);
				dto.setStay_name(rs.getString("stay_name"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setPage_content(rs.getString("page_content"));
				dto.setPage_reserve(rs.getString("page_reserve"));
				dto.setPage_guide(rs.getString("page_guide"));
				dto.setPage_refund(rs.getString("page_refund"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return dto;
	}
	
	// 글 번호 미리 받기
	public long getSequence() {
		long num=0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT page_seq.NEXTVAL AS num FROM DUAL
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				num=rs.getInt("num");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return num;
	}
	
	// 글 작성
	public boolean insert(PageDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;

		try {
			conn = DBConnector.getConn();
			// 
			String sql = """
				INSERT INTO Page
				(page_num, stay_num, user_num, page_content, page_created_at, page_reserve, page_guide, page_refund)
				VALUES(SEQ_page.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?, ?)
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, dto.getStay_num());
			pstmt.setLong(2, dto.getUser_num());
			pstmt.setString(3, dto.getPage_content());
			pstmt.setString(4, dto.getPage_reserve()); 
			pstmt.setString(5, dto.getPage_guide());
			pstmt.setString(6, dto.getPage_refund());
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 닫을 땐 실행되는 순서의 역순으로 닫아야 함
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {}
		}
		// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아낼 수 있다
		if (rowCount > 0) {
			return true; // 작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; // 작업 실패라는 의미에서 false 리턴하기
		}
	}
}