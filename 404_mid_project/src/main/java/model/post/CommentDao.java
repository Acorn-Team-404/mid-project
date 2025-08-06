 package model.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class CommentDao {
	private static CommentDao dao;
	static {
		dao=new CommentDao();
	}
	private CommentDao() {}
	public static CommentDao getInstance() {
		return dao;
	}
	
	// 댓글 리스트
	public List<CommentDto> selectAll(int parentNum){
		List<CommentDto> list = new ArrayList<CommentDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT comment_num, comment_writer, comment_target_writer, 
						comment_content, comment_deleted, comment_parent_num, comment_group_num, comment_created_at,
						u.users_profile_image AS comment_profile_image, u.users_id AS comment_writer_id, u2.users_id AS comment_target_writer_id
					FROM comments
					LEFT JOIN users u ON comment_writer = u.users_num
					LEFT JOIN users u2 ON comment_target_writer = u2.users_num
					WHERE comment_parent_num = ?
					ORDER BY comment_group_num ASC, comment_num ASC
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setInt(1, parentNum);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				CommentDto dto = new CommentDto();
				dto.setCommentNum(rs.getInt("comment_num"));
				dto.setCommentWriter(rs.getInt("comment_writer"));
				dto.setCommentTargetWriter(rs.getInt("comment_target_writer"));
				dto.setCommentContent(rs.getString("comment_content"));
				dto.setCommentParentNum(rs.getInt("comment_parent_num"));
				dto.setCommentGroupNum(rs.getInt("comment_group_num"));
				dto.setCommentDeleted(rs.getString("comment_deleted"));
				dto.setCommentCreatedAt(rs.getString("comment_created_at"));
				dto.setCommentProfileImage(rs.getString("comment_profile_image"));
				dto.setCommentWriterId(rs.getString("comment_writer_id"));
				dto.setCommentTargetWriterId(rs.getString("comment_target_writer_id"));
				
				
				
				//dto.setCommentReplyCount(rs.getInt("comment_reply_count"));
				
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	// 댓글 저장
	public boolean insert(CommentDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					INSERT INTO comments
					(comment_num, comment_writer, comment_target_writer, comment_content, comment_parent_num, comment_group_num)
					VALUES (?, ?, ?, ?, ?, ?)
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setInt(1, dto.getCommentNum());
			pstmt.setInt(2, dto.getCommentWriter());
			pstmt.setInt(3, dto.getCommentTargetWriter());
			pstmt.setString(4, dto.getCommentContent());
			pstmt.setInt(5, dto.getCommentParentNum());
			pstmt.setInt(6, dto.getCommentGroupNum());
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
	
	// 댓글 번호 리턴
	public int getSequence() {
		int num=0;
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT comments_seq.NEXTVAL AS num FROM DUAL
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
			DBConnector.close(rs, pstmt, conn);
		} 
		return num;
	}
	
	// 댓글 수정
	public boolean update(CommentDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE comments
					SET comment_content=?
					WHERE comment_num=?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, dto.getCommentContent());
			pstmt.setInt(2, dto.getCommentNum());
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
	
	
	//댓글 삭제 메소드 deleted => yes
	public boolean deleteByNum(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE comments
					SET comment_deleted='yes'
					WHERE comment_num=?
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
