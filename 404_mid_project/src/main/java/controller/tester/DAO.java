package controller.tester;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.util.DBConnector;

public class DAO {
	public boolean test() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """

					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩

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
	
	public int select() {
		// 필요한 객체를 담을 지역변수를 미리 만든다.
		// ex) List<DTO> list=new ArrayList<>();
		
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
		}
	}
	
}
