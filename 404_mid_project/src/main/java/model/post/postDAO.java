package model.post;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class postDAO {

	// 게시글 리스트
	public List<postDTO> selectAll(){
		List<postDTO> list=new ArrayList<postDTO>();
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
				postDTO dto=new postDTO();
				
				dto.setPost_num(rs.getInt("post_num"));
				dto.setPost_writer_num(rs.getInt("post_writer_num"));
				dto.setPost_title(rs.getString("post_title"));
				dto.setPost_content(rs.getString("post_content"));
				dto.setPost_stay_num(rs.getInt("post_stay_num"));
				dto.setPost_type(rs.getInt("post_type"));
				dto.setPost_views(rs.getInt("post_views"));
				dto.setPost_created_at(rs.getString("post_created_at"));
				dto.setPost_updated_at(rs.getString("post_updated_at"));
				dto.setPost_deleted(rs.getString("post_deleted"));
				
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
	
	
	// 게시글 1개 정보 리턴
	public postDTO getByPostNum(int num) {
		postDTO dto=null;
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT *
					FROM posts
					WHERE num=?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setInt(1, num);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			if (rs.next()) {
				dto=new postDTO();
				dto.setPost_num(num);
				dto.setPost_writer_num(rs.getInt("post_writer_num"));
				dto.setPost_title(rs.getString("post_title"));
				dto.setPost_content(rs.getString("post_content"));
				dto.setPost_stay_num(rs.getInt("post_stay_num"));
				dto.setPost_type(rs.getInt("post_type"));
				dto.setPost_views(rs.getInt("post_views"));
				dto.setPost_created_at(rs.getString("post_created_at"));
				dto.setPost_updated_at(rs.getString("post_updated_at"));
				dto.setPost_deleted(rs.getString("post_deleted"));
				
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

	
	// 게시글 저장
	public boolean insert(postDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					INSERT INTO posts
					(post_num, post_writer_num, post_title, post_content, post_stay_num)
					VALUES(?, ?, ?, ?, ?)
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setInt(1, dto.getPost_num());
			pstmt.setInt(2, dto.getPost_writer_num());
			pstmt.setString(3, dto.getPost_title());
			pstmt.setString(4, dto.getPost_content());
			pstmt.setInt(5, dto.getPost_stay_num());
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
