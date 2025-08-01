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
            conn = new DBConnector().getConn();
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
            try {
                if (rs != null)
                	rs.close();
                if (pstmt != null)
                	pstmt.close();
                if (conn != null)
                	conn.close();
            } catch (Exception e) {}
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
            conn = new DBConnector().getConn();
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
            try {
                if (rs != null)
                	rs.close();
                if (pstmt != null)
                	pstmt.close();
                if (conn != null)
                	conn.close();
            } catch (Exception e) {}
        }
        return count;
    }
    
 // Page 정보 가져오기
    public PageDto getPage(long stayNum) {
        PageDto dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = new DBConnector().getConn();
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
                dto.setPage_content(rs.getString("page_content"));
                dto.setPage_reserve(rs.getString("page_reserve"));
                dto.setPage_guide(rs.getString("page_guide"));
                dto.setPage_refund(rs.getString("page_refund"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
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
				dto.setStay_num(rs.getLong("stay_num"));
				dto.setStay_name(rs.getString("stay_name"));
				dto.setStay_addr(rs.getString("stay_addr"));
				dto.setStay_update_at(rs.getString("stay_update_at"));
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
            conn = new DBConnector().getConn();
            // 실행할 sql 문
            String sql = """
                SELECT *
                FROM stay
                WHERE stay_num = ?
            """;
            pstmt = conn.prepareStatement(sql);
            // ? 에 바인딩
            pstmt.setLong(1, stayNum);
            // select 문 실행하고 결과를 ResultSet 으로 받아온다
            rs = pstmt.executeQuery();
            // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해 줄 객체에 담는다
            if (rs.next()) {
                dto = new StayDto();
                dto.setStay_name(rs.getString("stay_name"));
                dto.setStay_loc(rs.getString("stay_loc"));
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
            } catch (Exception e) {}
        }
        return dto;
    }
}