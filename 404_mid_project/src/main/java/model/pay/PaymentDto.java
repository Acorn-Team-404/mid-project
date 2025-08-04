package model.pay;

import java.sql.Timestamp;

public class PaymentDto {
	private String payNum; //결제 번호 pk
	private String payBookNum; //예약 번호 fk
	private String orderId; //토스에서 발급된 주문 Id
	private long payUserNum; // 결제자(회원 번호)
	private long payAmount; //결제 금액
	
	private String payMethodGroupId;
	private int payMethodCode;
	
	private String payStatusGroupId; 
	private int payStatusCode;
	
	private Timestamp payApprovedAt;//결제 승인 시간
	private Timestamp payCreatedAt; //결제 생성 시간
	private Timestamp payPaidAt; // 실제 결제 완료 일시
	
	public String getPayNum() {
		return payNum;
	}
	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}
	public String getpayBookNum() {
		return payBookNum;
	}
	public void setpayBookNum(String payBookNum) {
		this.payBookNum = payBookNum;
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
	public int getPayMethodCode() {
		return payMethodCode;
	}
	public void setPayMethodCode(int payMethodCode) {
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
	public Timestamp getPayApprovedAt() {
		return payApprovedAt;
	}
	public void setPayApprovedAt(Timestamp payApprovedAt) {
		this.payApprovedAt = payApprovedAt;
	}
	public Timestamp getPayCreatedAt() {
		return payCreatedAt;
	}
	public void setPayCreatedAt(Timestamp payCreatedAt) {
		this.payCreatedAt = payCreatedAt;
	}
	public Timestamp getPayPaidAt() {
		return payPaidAt;
	}
	public void setPayPaidAt(Timestamp payPaidAt) {
		this.payPaidAt = payPaidAt;
	}
	
	
}
