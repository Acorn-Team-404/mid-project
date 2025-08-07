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
	
	// 글 전체 카운트
	public int getCount() {
		int count=0;
				
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT COUNT(*) AS count
				FROM page
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				count=rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return count;
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
				SELECT page_num, s.stay_name, u.users_name, page_created_at
				FROM page p
				INNER JOIN users u ON page_users_num=u.users_num
				INNER JOIN stay s ON p.page_stay_num=s.stay_num
				ORDER BY page_num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				PageDto dto=new PageDto();
				dto.setPageNum(rs.getLong("page_num"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setUsersName(rs.getString("users_name"));
				dto.setPageCreatedAt(rs.getString("page_created_at"));
					
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	}
	
	// 검색 키워드에 부합하는 글의 갯수를 리턴
	public int getCountByKeyword(String keyword) {
		int count=0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT COUNT(*) AS count
				FROM page p
				JOIN stay s ON p.page_stay_num = s.stay_num
				WHERE (s.stay_name LIKE '%' || ? || '%' OR p.page_content LIKE '%' || ? || '%' OR s.stay_loc LIKE '%' || ? || '%')
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			pstmt.setString(3, keyword);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				count=rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return count;
	}
	
	// 특정 페이지에 해당하는 row 만 select 해서 리턴 + startRowNum 과 endRowNum 을 담아와서 select
	public List<PageDto> selectPage(PageDto dto){
		List<PageDto> list=new ArrayList<>();
		
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
						(SELECT p.page_num, p.page_stay_num, p.page_users_num, s.stay_name, s.stay_loc, p.page_content, p.page_reserve, p.page_guide, p.page_refund, p.page_created_at
						FROM page p
						JOIN stay s ON p.page_stay_num = s.stay_num
						JOIN users u ON p.page_users_num = u.users_num
						ORDER BY p.page_num DESC) result1)
				WHERE rnum BETWEEN ? AND ?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setInt(1, dto.getStartRowNum());
			pstmt.setInt(2, dto.getEndRowNum());
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				// 커서가 위치한 곳의 회원 정보를 저장할 PageDto 객체 생성
				PageDto dto2=new PageDto();
				// ResultSet 으로부터 얻어낸 회원 번호를 PageDto 객체의 setter 메소드를 이용해서 dto 에 저장
				dto2.setPageNum(rs.getLong("page_num"));
				dto2.setStayNum(rs.getLong("page_stay_num"));
				dto2.setUsersNum(rs.getLong("page_users_num"));
				dto2.setStayName(rs.getString("stay_name"));
				dto2.setStayLoc(rs.getString("stay_loc"));
				dto2.setPageContent(rs.getString("page_content"));
				dto2.setPageReserve(rs.getString("page_reserve"));
				dto2.setPageGuide(rs.getString("page_guide"));
				dto2.setPageRefund(rs.getString("page_refund"));
				dto2.setPageCreatedAt(rs.getString("page_created_at"));
				// 회원 한 명의 정보가 담긴 새로운 PageDto 객체의 참조값을 List 에 누적시키기
				list.add(dto2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	}
	
	// 특정 페이지와 검색 키워드에 해당하는 row 만 select 해서 리턴 + startRowNum 과 endRowNum 을 담아와서 select
	public List<PageDto> searchPageByKeyword(PageDto dto){
		List<PageDto> list=new ArrayList<>();
		
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
						(SELECT p.page_num, s.stay_name, u.users_name, p.page_created_at
						FROM page p
						JOIN stay s ON p.page_stay_num = s.stay_num
						JOIN users u ON p.page_users_num = u.users_num
						WHERE (s.stay_name LIKE '%' || ? || '%' OR p.page_content LIKE '%' || ? || '%' OR s.stay_loc LIKE '%' || ? || '%')
						ORDER BY p.page_num DESC) result1)
				WHERE rnum BETWEEN ? AND ?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			String keyword = dto.getKeyword();
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			pstmt.setString(3, keyword);
			pstmt.setInt(4, dto.getStartRowNum());
	        pstmt.setInt(5, dto.getEndRowNum());
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				// 커서가 위치한 곳의 회원 정보를 저장할 PageDto 객체 생성
				PageDto dto2=new PageDto();
				// ResultSet 으로부터 얻어낸 회원 번호를 PageDto 객체의 setter 메소드를 이용해서 dto 에 저장
				dto2.setPageNum(rs.getLong("page_num"));
				dto2.setStayName(rs.getString("stay_name"));
				dto2.setUsersName(rs.getString("users_name"));
				dto2.setPageCreatedAt(rs.getString("page_created_at"));
				// 회원 한 명의 정보가 담긴 새로운 PageDto 객체의 참조값을 List 에 누적시키기
				list.add(dto2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
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
				SET page_content=?, page_reserve=?, page_guide=?, page_refund=?, page_update_at=SYSDATE
				WHERE page_num=?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, dto.getPageContent());
			pstmt.setString(2, dto.getPageReserve());
			pstmt.setString(3, dto.getPageGuide());
			pstmt.setString(4, dto.getPageRefund());
			pstmt.setLong(5, dto.getPageNum());
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아낼 수 있다
		if (rowCount > 0) {
			return true; // 작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; // 작업 실패라는 의미에서 false 리턴하기
		}
	}
		
	// 글 삭제
	public boolean deleteByNum(long num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;

		try {
			conn = DBConnector.getConn();
			String sql = """
				DELETE FROM page
				WHERE page_num=?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, num);
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아낼 수 있다
		if (rowCount > 0) {
			return true; // 작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; // 작업 실패라는 의미에서 false 리턴하기
		}
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
				num=rs.getLong("num");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return num;
	}
	
	// 글 하나의 정보 불러오기
	public PageDto getByNum(long pageNum) {
		PageDto dto=null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT stay_num, stay_name, stay_loc, page_content, stay_addr
				FROM stay
				JOIN page ON page_stay_num=stay_num
				WHERE page_num=?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, pageNum);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				dto=new PageDto();
				dto.setStayNum(rs.getLong("stay_num"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setStayLoc(rs.getString("stay_loc"));
				dto.setPageContent(rs.getString("page_content"));
				dto.setStayAddr(rs.getString("stay_addr"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return dto;
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
				INSERT INTO page
				(page_num, page_stay_num, page_users_num, page_content, page_created_at, page_reserve, page_guide, page_refund)
				VALUES (?, ?, ?, ?, SYSDATE, ?, ?, ?)
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, dto.getPageNum());
			pstmt.setLong(2, dto.getStayNum());
			pstmt.setLong(3, dto.getUsersNum());
			pstmt.setString(4, dto.getPageContent());
			pstmt.setString(5, dto.getPageReserve()); 
			pstmt.setString(6, dto.getPageGuide());
			pstmt.setString(7, dto.getPageRefund());
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("[PageDao.insert()] 데이터 저장 중 예외 발생!");
		    System.err.println("PageNum: " + dto.getPageNum());
		    System.err.println("StayNum: " + dto.getStayNum());
		    System.err.println("UsersNum: " + dto.getUsersNum());
		    System.err.println("Content: " + dto.getPageContent());
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		// 변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아낼 수 있다
		if (rowCount > 0) {
			return true; // 작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; // 작업 실패라는 의미에서 false 리턴하기
		}
	}
}