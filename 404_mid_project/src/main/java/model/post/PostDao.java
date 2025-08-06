package model.post;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class PostDao {

	// 자신을 참조할 static
	private static PostDao dao;
	// static 초기화 블럭에서 객체 생성 -> static 필드에 저장
	static {
		dao=new PostDao();
	}
	// 외부에서 생성 못하도록 private
	private PostDao() {}
	// 참조값 리턴
	public static PostDao getInstance() {
		return dao;
	}
	
	
	// 게시글 리스트
	public List<PostDto> selectAll(){
		List<PostDto> list=new ArrayList<PostDto>();
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT post_num, post_writer_num, post_title, post_content, 
					post_stay_num, post_type, post_views, post_created_at, post_updated_at, post_deleted
					FROM posts
					ORDER BY post_num DESC
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			while (rs.next()) {
				PostDto dto=new PostDto();
				
				dto.setPostNum(rs.getInt("post_num"));
				dto.setPostWriterNum(rs.getLong("post_writer_num"));
				dto.setPostTitle(rs.getString("post_title"));
				dto.setPostContent(rs.getString("post_content"));
				dto.setPostStayNum(rs.getInt("post_stay_num"));
				dto.setPostType(rs.getInt("post_type"));
				dto.setPostViews(rs.getInt("post_views"));
				dto.setPostCreatedAt(rs.getString("post_created_at"));
				dto.setPostUpdatedAt(rs.getString("post_updated_at"));
				dto.setPostDeleted(rs.getString("post_deleted"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	// 전체 글 갯수 리턴
	public int getPostCount() {
		int count=0;
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		// ex) List<DTO> list=new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT COUNT(*) 
					FROM posts 
					WHERE post_deleted='no'
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				count = rs.getInt(1); // 첫 번째 컬럼 값 가져오기
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return count;
	}
	
	//특정 page에 해당하는 row만 select해서 리턴하는 메소드
	//BoardDto 객체에 startRowNum 과 endRowNum을 담아와서 select
	public List<PostDto> selectPage(PostDto dto){
		List<PostDto> list=new ArrayList<PostDto>();
		
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		// ex) List<DTO> list=new ArrayList<>();

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
							(SELECT post_num, post_writer_num, post_title, post_views, post_created_at
							FROM posts
							ORDER BY post_num DESC) result1)
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
				PostDto dto2=new PostDto();
				dto2.setPostNum(rs.getInt("post_num"));
				dto2.setPostWriterNum(rs.getLong("post_writer_num"));
				dto2.setPostTitle(rs.getString("post_title"));
				dto2.setPostViews(rs.getInt("post_views"));
				dto2.setPostCreatedAt(rs.getString("post_created_at"));
				
				list.add(dto2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	}
	
	//특정 page와 keyword에 해당하는 row만 select해서 리턴하는 메소드
		//BoardDto 객체에 startRowNum 과 endRowNum을 담아와서 select
		public List<PostDto> selectPageByKeyword(PostDto dto){
			List<PostDto> list=new ArrayList<PostDto>();
			
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
								(SELECT post_num, post_writer_num, post_title, post_views, post_created_at
								FROM posts
								WHERE title LIKE '%'||?||'%' OR content LIKE '%'||?||'%'
								ORDER BY post_num DESC) result1)				
						WHERE rnum BETWEEN ? AND ?
						""";
				pstmt = conn.prepareStatement(sql);
				// ? 에 값 바인딩
				pstmt.setString(1, dto.getPostKeyword());
				pstmt.setString(2, dto.getPostKeyword());
				pstmt.setInt(3, dto.getStartRowNum());
				pstmt.setInt(4, dto.getEndRowNum());
				// Select 문 실행하고 결과를 ResultSet 으로 받아온다
				rs = pstmt.executeQuery();
				// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
				// 단일 : if  /  다중 : while
				while (rs.next()) {
					PostDto dto2=new PostDto();
					dto2.setPostNum(rs.getInt("post_num"));
					dto2.setPostWriterNum(rs.getLong("post_writer_num"));
					dto2.setPostTitle(rs.getString("post_title"));
					dto2.setPostViews(rs.getInt("post_views"));
					dto2.setPostCreatedAt(rs.getString("post_created_at"));
					
					list.add(dto2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBConnector.close(rs, pstmt, conn);
			} // 하단에 return 값 넣어주셔야함!
			return list;
		}	
		
	// 검색 키워드 리스트
	public int getCountByKeyword(String keyword) {
		int count=0;
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		// ex) List<DTO> list=new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
						SELECT MAX(rownum) AS count
						FROM posts
						WHERE title LIKE '%' || ? || '%' OR content LIKE '%' || ? || '%'
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
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
	
	// 게시글 1개 정보 리턴
	public PostDto getByPostNum(int num) {
		PostDto dto=null;
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT 
					    p.post_num, 
					    p.post_writer_num, 
					    p.post_title, 
					    p.post_content, 
					    p.post_views, 
					    p.post_stay_num, 
					    	TO_CHAR(p.post_created_at, 'YYYY-MM-DD') AS post_created_at, 
					    p.post_updated_at, 
					    p.post_deleted, 
					    p.post_type, 
					    u.users_ID
					FROM 
					    posts p
					LEFT JOIN 
					    users u ON p.post_writer_num = u.users_num
					WHERE 
					    p.post_num = ?		
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setInt(1, num);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			if (rs.next()) {
				dto=new PostDto();
				dto.setPostNum(num);
				dto.setPostWriterNum(rs.getLong("post_writer_num"));
				dto.setPostTitle(rs.getString("post_title"));
				dto.setPostContent(rs.getString("post_content"));
				dto.setPostStayNum(rs.getInt("post_stay_num"));
				dto.setPostType(rs.getInt("post_type"));
				dto.setPostViews(rs.getInt("post_views"));
				dto.setPostCreatedAt(rs.getString("post_created_at"));
				dto.setPostUpdatedAt(rs.getString("post_updated_at"));
				dto.setPostDeleted(rs.getString("post_deleted"));
				dto.setUsersID(rs.getString("users_ID"));
				
				
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
		return dto;
	}

	// 글번호 미리 select 해서 리턴
	public int getSequence() {
		// 글 번호를 저장할 지역변수
		int num=0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT posts_seq.NEXTVAL AS num
					FROM DUAL
					""";
			pstmt = conn.prepareStatement(sql);
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
			try {
			DBConnector.close(rs, pstmt, conn);
			} catch (Exception e) {
			}
		}
		return num;
	}
	
	
	// 게시글 저장
	public boolean insert(PostDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					INSERT INTO posts
					(post_num, post_writer_num, post_title, post_content, 
					post_type, post_stay_num, post_created_at)
					VALUES(?, ?, ?, ?, ?, ?, SYSDATE)
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			
			pstmt.setInt(1, dto.getPostNum());
			pstmt.setLong(2, dto.getPostWriterNum());
			pstmt.setString(3, dto.getPostTitle());
			pstmt.setString(4, dto.getPostContent());
			pstmt.setInt(5, dto.getPostType());
			pstmt.setInt(6, dto.getPostStayNum());
			
			
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
	
	
	// 게시글 업데이트
	public boolean update(PostDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE posts
					SET 
						post_title=?,
						post_content=?,
						post_stay_num=?
					WHERE post_num=?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩

			pstmt.setString(1, dto.getPostTitle());
	        pstmt.setString(2, dto.getPostContent());
	        pstmt.setInt(3, dto.getPostStayNum());
	        pstmt.setInt(4, dto.getPostNum());
	        
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
	
	
	// 게시글 삭제
	public boolean deleteByNum(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					DELETE
					FROM posts
					WHERE post_num=?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setInt(1, num);
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
	
	// 조회수 증가
	public boolean addViews(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
						UPDATE posts
						SET post_views = post_views+1
						WHERE post_num=?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setInt(1, num);
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
