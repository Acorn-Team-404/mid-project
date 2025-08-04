package controller.tester;

import model.page.StayDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class RoomTestDao {
	private static RoomTestDao dao;
	static {
		dao=new RoomTestDao();
	}
	private RoomTestDao() {}
	public static RoomTestDao getInstance() {
		if (dao == null) dao = new RoomTestDao();
		return dao;
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
                dto.setStayName(rs.getString("stay_name"));
                dto.setStayNum(rs.getLong("stay_num"));
                dto.setStayFacilities(rs.getString("stay_facilities"));
                dto.setStayPhone(rs.getString("stay_phone"));
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
