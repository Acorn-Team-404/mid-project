package model.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class ReviewDao {

	private static ReviewDao dao;
	   // static 초기화 블럭에서 객체 생성해서 static 필드에 저장
	   static {
	      dao = new ReviewDao();
	   }
	   
	   // 외부에서 객체 생성하지 못하도록 생성자의 접근 지정자를 private 로 지정
	   private ReviewDao() {
	   }

	   // 참조값을 리턴해주는 static 메소드 제공
	   public static ReviewDao getInstance() {
	      return dao;
	   }
	   
	   //특정 숙소번호에 해당하는 리뷰 가져오기
	   public List<ReviewDto> getByStayNum(long stayNum){
		   
		   // 필요한 객체를 담을 지역변수를 미리 만든다.
		// ex) List<DTO> list=new ArrayList<>();
		List<ReviewDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
					SELECT review_id, review_book_num, review_users_id, review_rating, review_comment, review_created_at,review_stay_num
					FROM REVIEW
					WHERE review_stay_num = ?
					ORDER BY review_created_at DESC
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, stayNum);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				ReviewDto dto = new ReviewDto();
				dto.setReviewId(rs.getLong("review_id"));
				dto.setBookNum(rs.getString("review_book_num"));
				dto.setUsersId(rs.getString("review_users_id"));
				dto.setRating(rs.getInt("review_rating"));
	            dto.setComment(rs.getString("review_comment"));
	            dto.setCreatedAt(rs.getTimestamp("review_created_at"));
	            dto.setReviewStayNum(rs.getLong("review_stay_num"));
	            list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	   }
	   
	   //리뷰 등록 메소드
	   public boolean insert(ReviewDto dto) {
		 Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					INSERT INTO REVIEW 
					(review_id, review_book_num, review_users_id, review_rating, review_comment, review_created_at, review_stay_num)
					VALUES (review_seq.NEXTVAL, ?, ?, ?, ?, SYSTIMESTAMP, ?)
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, dto.getBookNum());
			pstmt.setString(2, dto.getUsersId());
			pstmt.setInt(3, dto.getRating());
			pstmt.setString(4, dto.getComment());
			pstmt.setLong(5, dto.getReviewStayNum());
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("DAO 입력 확인: " + dto);
			System.out.println("insert 결과 rowCount = " + rowCount);
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
