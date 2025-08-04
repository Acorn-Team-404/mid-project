package model.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class StayDao {
	private static StayDao dao;
	static {
		dao=new StayDao();
	}
	private StayDao() {}
	public static StayDao getInstance() {
		return dao;
	}
	
	// 숙소에 대한 평균 별점
    public double getAverageStar(long stayNum) {
        double avg = 0.00;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	conn = DBConnector.getConn();
            // 실행할 sql 문
         	String sql = """
         		SELECT AVG(scope_star) AS avg_star
         		FROM scope
         		WHERE stay_num = ? AND scope_delete IS NULL
         	""";
            pstmt = conn.prepareStatement(sql);
            // ? 에 바인딩
            pstmt.setLong(1, stayNum);
            // select 문 실행하고 결과를 ResultSet 으로 받아온다
            rs = pstmt.executeQuery();
            // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해 줄 객체에 담는다
            if (rs.next()) {
                avg = rs.getDouble("avg_star");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBConnector.close(rs, pstmt, conn);
        }
        return avg;
    }

    // 숙소에 대한 리뷰 수 갯수를 리턴
    public int getReviewCount(long stayNum) {
        int count = 0;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	conn = DBConnector.getConn();
            // 실행할 sql 문
         	String sql = """
         		SELECT COUNT(*) AS star_count
         		FROM scope
         		WHERE stay_num = ? AND scope_delete IS NULL
         	""";
            pstmt = conn.prepareStatement(sql);
            // ? 에 바인딩
            pstmt.setLong(1, stayNum);
            // select 문 실행하고 결과를 ResultSet 으로 받아온다
            rs = pstmt.executeQuery();
            // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해 줄 객체에 담는다
            if(rs.next()) {
                count = rs.getInt("star_count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBConnector.close(rs, pstmt, conn);
        }
        return count;
    }
    
    // 글 수정
 	public boolean update(StayDto dto) {
 		Connection conn = null;
 		PreparedStatement pstmt = null;
 		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
 		int rowCount = 0;

 		try {
 			conn = DBConnector.getConn();
 			String sql = """
 				UPDATE stay
 				SET stay_name=?, stay_addr=?, stay_loc=?, stay_phone=?, stay_facilities=?, stay_update_at=SYSDATE
 				WHERE stay_num=?
 			""";
 			pstmt = conn.prepareStatement(sql);
 			// ? 에 순서대로 필요한 값 바인딩
 			pstmt.setString(1, dto.getStayName());
 	        pstmt.setString(2, dto.getStayAddr());
 	        pstmt.setString(3, dto.getStayLoc());
 	        pstmt.setString(4, dto.getStayPhone());
 	        pstmt.setString(5, dto.getStayFacilities());
 	        pstmt.setLong(6, dto.getStayNum());
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
 	public boolean deleteByNum(long stayNum) {
 		Connection conn = null;
 		PreparedStatement pstmt = null;
 		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
 		int rowCount = 0;

 		try {
 			conn = DBConnector.getConn();
 			String sql = """
 				DELETE FROM stay
 				WHERE stay_num=?
 			""";
 			pstmt = conn.prepareStatement(sql);
 			// ? 에 순서대로 필요한 값 바인딩
 			pstmt.setLong(1, stayNum);
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
    
    // Page 정보 가져오기
    public PageDto getPage(long stayNum) {
        PageDto dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	conn = DBConnector.getConn();
            String sql = """
                SELECT page_content, page_reserve, page_guide, page_refund
                FROM page
                WHERE stay_num = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, stayNum);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dto = new PageDto();
                dto.setPageContent(rs.getString("page_content"));
                dto.setPageReserve(rs.getString("page_reserve"));
                dto.setPageGuide(rs.getString("page_guide"));
                dto.setPageRefund(rs.getString("page_refund"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBConnector.close(rs, pstmt, conn);
        }
        return dto;
    }
	
	// 숙소 전체 가져오기
	public List<StayDto> selectAll() {
		List<StayDto> list=new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT stay_num, stay_name, stay_addr, stay_update_at
				FROM stay
				ORDER BY stay_num DESC
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩

			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			while (rs.next()) {
				StayDto dto=new StayDto();
				dto.setStayNum(rs.getLong("stay_num"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setStayAddr(rs.getString("stay_addr"));
				dto.setStayUpdateAt(rs.getString("stay_update_at"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return list;
	}
	
	// 숙소 정보 가져오기
    public StayDto getByNum(long stayNum) {
    	StayDto dto = null;
    	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
        try {
        	conn = DBConnector.getConn();
            // 실행할 sql 문
            String sql = """
                SELECT stay_num, s.users_num, stay_name, stay_addr, stay_loc, stay_lat, stay_long, stay_phone, stay_facilities, stay_update_at, u.users_name
				FROM stay s
				INNER JOIN users u ON s.users_num=u.users_num
				WHERE stay_num=?
            """;
            pstmt = conn.prepareStatement(sql);
            // ? 에 바인딩
            pstmt.setLong(1, stayNum);
            // select 문 실행하고 결과를 ResultSet 으로 받아온다
            rs = pstmt.executeQuery();
            // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해 줄 객체에 담는다
            if (rs.next()) {
                dto = new StayDto();
                dto.setStayNum(rs.getLong("stay_num"));
                dto.setUsersNum(rs.getLong("users_num"));
                dto.setStayName(rs.getString("stay_name"));
                dto.setStayAddr(rs.getString("stay_addr"));
                dto.setStayLoc(rs.getString("stay_loc"));
                dto.setStayLat(rs.getString("stay_lat"));
                dto.setStayLong(rs.getString("stay_long"));
                dto.setStayPhone(rs.getString("stay_phone"));
                dto.setStayUpdateAt(rs.getString("stay_update_at"));
                dto.setStayFacilities(rs.getString("stay_facilities"));
                dto.setUsersName(rs.getString("users_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBConnector.close(rs, pstmt, conn);
        }
        return dto;
    }
    
    // 숙소 등록
    public boolean insert(StayDto dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        // 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
     	int rowCount = 0;

        try {
        	conn = DBConnector.getConn();
            // 실행할 sql 문
            String sql = """
            	INSERT INTO stay
            	(stay_num, users_num, stay_name, stay_addr, stay_loc,
            	stay_lat, stay_long, stay_phone, stay_facilities, stay_update_at)
            	VALUES (stay_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)
            """;
            pstmt = conn.prepareStatement(sql);
            // ? 에 바인딩  
            pstmt.setLong(1, dto.getUsersNum());
            pstmt.setString(2, dto.getStayName());
            pstmt.setString(3, dto.getStayAddr());
            pstmt.setString(4, dto.getStayLoc());
            pstmt.setString(5, dto.getStayLat());
            pstmt.setString(6, dto.getStayLong());
            pstmt.setString(7, dto.getStayPhone());
            pstmt.setString(8, dto.getStayFacilities());
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
}