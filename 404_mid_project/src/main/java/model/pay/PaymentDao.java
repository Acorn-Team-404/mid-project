package model.pay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.util.DBConnector;

public class PaymentDao {
	
	private static PaymentDao dao;
	static {
		dao = new PaymentDao();
	}
	private PaymentDao(){};
	
	public static PaymentDao getInstance() {
		return dao;
	}
	
	
    public void insertPayment(Connection conn, String paymentKey, String orderId, int amount) throws SQLException {
        String sql = """
        		
        		
        		""";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //pstmt.setString(1, paymentKey);
            //pstmt.setString(2, orderId);
            //pstmt.setInt(3, amount);
            //pstmt.executeUpdate();
        }
    }
    
    //결제 번호 생성 메서드
    public String generatePayNum() {
		String PayNum = null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			conn = DBConnector.getConn();

			// 시퀀스에서 번호 추출
			String sql = "SELECT book_seq.NEXTVAL FROM dual";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			// 시퀀스 숫자를 받아와 finalNum 에 저장
			int finalNum = 0;
			if(rs.next()) {
				// 첫번째 행의 값
				finalNum = rs.getInt(1);
			}
			
			PayNum = "P" + String.format("%04d", finalNum);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return PayNum;
	}
	
    
    
}
