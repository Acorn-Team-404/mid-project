package model.pay;

import java.sql.Date;

public class PaymentDTO {
	private String payNum; //결제 번호 pk
	private String bookNum; //예약 번호 fk
	private String orderId; //토스에서 발급된 주문 I
	private long payUserNum; // 결제자(회원 번호)
	private long payAmount; //결제 금액
	
	private String payMethodGroupId;
	private String payMethodCode;
	
	private String payStatusGroupId; 
	private int payStatusCode;
	
	private Date payApprovedAt;//결제 승인 시간
	private Date payCreatedAt; //결제 생성 시간
	private Date payPaidAt; // 실제 결제 완료 일시
	
	private String payMethodName;
	private String payStatusName;
	public String getPayNum() {
		return payNum;
	}
	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public long getPayUserNum() {
		return payUserNum;
	}
	public void setPayUserNum(long payUserNum) {
		this.payUserNum = payUserNum;
	}
	public long getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	public String getPayMethodGroupId() {
		return payMethodGroupId;
	}
	public void setPayMethodGroupId(String payMethodGroupId) {
		this.payMethodGroupId = payMethodGroupId;
	}
	public String getPayMethodCode() {
		return payMethodCode;
	}
	public void setPayMethodCode(String payMethodCode) {
		this.payMethodCode = payMethodCode;
	}
	public String getPayStatusGroupId() {
		return payStatusGroupId;
	}
	public void setPayStatusGroupId(String payStatusGroupId) {
		this.payStatusGroupId = payStatusGroupId;
	}
	public int getPayStatusCode() {
		return payStatusCode;
	}
	public void setPayStatusCode(int payStatusCode) {
		this.payStatusCode = payStatusCode;
	}
	public Date getPayApprovedAt() {
		return payApprovedAt;
	}
	public void setPayApprovedAt(Date payApprovedAt) {
		this.payApprovedAt = payApprovedAt;
	}
	public Date getPayCreatedAt() {
		return payCreatedAt;
	}
	public void setPayCreatedAt(Date payCreatedAt) {
		this.payCreatedAt = payCreatedAt;
	}
	public Date getPayPaidAt() {
		return payPaidAt;
	}
	public void setPayPaidAt(Date payPaidAt) {
		this.payPaidAt = payPaidAt;
	}
	public String getPayMethodName() {
		return payMethodName;
	}
	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
	}
	public String getPayStatusName() {
		return payStatusName;
	}
	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}
	
	
	
}
