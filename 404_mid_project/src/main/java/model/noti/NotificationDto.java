package model.noti;

public class NotificationDto {
	// TABLE 기본 필드
	private long notiNum;
	private long notiRecipientNum;
	private long notiSenderNum;
	private String notiTypeGroupId;
	private int notiTypeCode;
	private String notiTargetGroupId;
	private int notiTargetTypeCode;
	private long notiTargetNum;
	private String notiMessage;
	private String notiReadGroupId;
	private int notiReadCode;
	private String notiCreatedAt;
	
	
	// 추가로 필요한 필드
	//private String 이미지가져와야함
	
	
	public long getNotiNum() {
		return notiNum;
	}
	public void setNotiNum(long notiNum) {
		this.notiNum = notiNum;
	}
	public long getNotiRecipientNum() {
		return notiRecipientNum;
	}
	public void setNotiRecipientNum(long notiRecipientNum) {
		this.notiRecipientNum = notiRecipientNum;
	}
	public long getNotiSenderNum() {
		return notiSenderNum;
	}
	public void setNotiSenderNum(long notiSenderNum) {
		this.notiSenderNum = notiSenderNum;
	}
	public String getNotiTypeGroupId() {
		return notiTypeGroupId;
	}
	public void setNotiTypeGroupId(String notiTypeGroupId) {
		this.notiTypeGroupId = notiTypeGroupId;
	}
	public int getNotiTypeCode() {
		return notiTypeCode;
	}
	public void setNotiTypeCode(int notiTypeCode) {
		this.notiTypeCode = notiTypeCode;
	}
	public String getNotiTargetGroupId() {
		return notiTargetGroupId;
	}
	public void setNotiTargetGroupId(String notiTargetGroupId) {
		this.notiTargetGroupId = notiTargetGroupId;
	}
	public int getNotiTargetTypeCode() {
		return notiTargetTypeCode;
	}
	public void setNotiTargetTypeCode(int notiTargetTypeCode) {
		this.notiTargetTypeCode = notiTargetTypeCode;
	}
	public long getNotiTargetNum() {
		return notiTargetNum;
	}
	public void setNotiTargetNum(long notiTargetNum) {
		this.notiTargetNum = notiTargetNum;
	}
	public String getNotiMessage() {
		return notiMessage;
	}
	public void setNotiMessage(String notiMessage) {
		this.notiMessage = notiMessage;
	}
	public String getNotiReadGroupId() {
		return notiReadGroupId;
	}
	public void setNotiReadGroupId(String notiReadGroupId) {
		this.notiReadGroupId = notiReadGroupId;
	}
	public int getNotiReadCode() {
		return notiReadCode;
	}
	public void setNotiReadCode(int notiReadCode) {
		this.notiReadCode = notiReadCode;
	}
	public String getNotiCreatedAt() {
		return notiCreatedAt;
	}
	public void setNotiCreatedAt(String notiCreatedAt) {
		this.notiCreatedAt = notiCreatedAt;
	}
}
