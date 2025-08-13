package model.pay;

import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.book.BookDto;
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
	
	//결제 테이블 같은 예약번호로 결제 두번 되는 거 방지용
	public boolean existsByBookNum(String payBookNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnector.getConn();
			String sql = """
					SELECT 1
					FROM PAYMENTS
					WHERE PAY_BOOK_NUM = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, payBookNum);
			rs = pstmt.executeQuery();
			
			return rs.next();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			DBConnector.close(rs,pstmt, conn);
		}
		
		return false;
	}
	
	public String getMethodCodeByPayNum(String payNum) {	
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String method_code = null;
		try {
			conn = DBConnector.getConn();
		
			String sql = """
					SELECT CC_CODE_NAME
					FROM COMMON_CODE 
					WHERE CC_CODE = (
					SELECT PAY_METHOD_CODE 
					FROM PAYMENTS
					WHERE PAY_NUM = ?)
					AND CC_GROUP_ID = 'PAYMENT_METHOD'
					
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, payNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				method_code = rs.getString(1);	
			}
		} catch (Exception e){
			e.printStackTrace();
		}finally {DBConnector.close(rs,pstmt, conn);}
		
		return method_code;
	} 
	
	public PaymentDto getPayBybookNum(String bookNum) {
		PaymentDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = """
				SELECT *
				FROM PAYMENTS
				WHERE PAY_BOOK_NUM = ?
				
				""";
		try {
			conn = DBConnector.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new PaymentDto();
				dto.setPayNum(rs.getString("PAY_NUM"));
				dto.setpayBookNum(rs.getString("PAY_BOOK_NUM"));
				dto.setOrderId(rs.getString("ORDER_ID"));
				dto.setPayUserNum(rs.getLong("PAY_USERS_NUM"));
				dto.setPayAmount(rs.getLong("PAY_AMOUNT"));
				dto.setPayMethodGroupId(rs.getString("PAY_METHOD_GROUP_ID"));
				dto.setPayMethodCode(rs.getInt("PAY_METHOD_CODE"));
				dto.setPayStatusGroupId(rs.getString("PAY_STATUS_GROUP_ID"));
				dto.setPayStatusCode(rs.getInt("PAY_STATUS_CODE"));
				dto.setPayApprovedAt(rs.getTimestamp("PAY_APPROVED_AT"));
				dto.setPayCreatedAt(rs.getTimestamp("PAY_CREATED_AT"));
				dto.setPayPaidAt(rs.getTimestamp("PAY_PAID_AT"));
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnector.close(rs,pstmt, conn);
		}
		
		return dto;
	};
	
	public PaymentDto getPayByOrderId(String orderId) {
		PaymentDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = """
				SELECT *
				FROM PAYMENTS
				WHERE ORDER_ID = ?
				
				""";
		try {
			conn = DBConnector.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new PaymentDto();
				dto.setPayNum(rs.getString("PAY_NUM"));
				dto.setpayBookNum(rs.getString("PAY_BOOK_NUM"));
				dto.setOrderId(rs.getString("ORDER_ID"));
				dto.setPayUserNum(rs.getLong("PAY_USERS_NUM"));
				dto.setPayAmount(rs.getLong("PAY_AMOUNT"));
				dto.setPayMethodGroupId(rs.getString("PAY_METHOD_GROUP_ID"));
				dto.setPayMethodCode(rs.getInt("PAY_METHOD_CODE"));
				dto.setPayStatusGroupId(rs.getString("PAY_STATUS_GROUP_ID"));
				dto.setPayStatusCode(rs.getInt("PAY_STATUS_CODE"));
				dto.setPayApprovedAt(rs.getTimestamp("PAY_APPROVED_AT"));
				dto.setPayCreatedAt(rs.getTimestamp("PAY_CREATED_AT"));
				dto.setPayPaidAt(rs.getTimestamp("PAY_PAID_AT"));
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnector.close(rs,pstmt, conn);
		}
		
		return dto;
	};
	
    public boolean insertPayment(Connection conn, PaymentDto payDto) throws SQLException {
  
        String sql = """
        		INSERT INTO PAYMENTS(pay_num,pay_book_num,order_id,pay_users_num,
        		pay_amount,pay_method_group_id,pay_method_code,
        		pay_status_group_id,pay_status_code,pay_approved_at,
        		pay_created_at,pay_paid_at)
        		VALUES(?,?,?,?,?,?,?,?,?,?,?,?)
        		""";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, payDto.getPayNum());
            pstmt.setString(2, payDto.getpayBookNum());
            pstmt.setString(3, payDto.getOrderId());
            pstmt.setLong(4, payDto.getPayUserNum()); 
            pstmt.setLong(5, payDto.getPayAmount());
            pstmt.setString(6, payDto.getPayMethodGroupId());
            pstmt.setInt(7, payDto.getPayMethodCode());
            pstmt.setString(8, payDto.getPayStatusGroupId());
            pstmt.setInt(9, payDto.getPayStatusCode());
            pstmt.setTimestamp(10, payDto.getPayApprovedAt());
            pstmt.setTimestamp(11,payDto.getPayCreatedAt());
            pstmt.setTimestamp(12, payDto.getPayPaidAt());
            int res = pstmt.executeUpdate();
            

           return res == 1;
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
