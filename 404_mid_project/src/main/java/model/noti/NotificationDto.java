package model.noti;

public class NotificationDto {
	// Notification Table 기본 필드
	private long notiNum;
	private long notiRecipientNum;
	private long notiSenderNum;
	private String notiTypeGroupId;
	private int notiTypeCode;
	private String notiTargetGroupId;
	private int notiTargetTypeCode;
	private String notiTargetNum;
	private String notiMessage;
	private String notiReadGroupId;
	private int notiReadCode;
	private String notiCreatedAt;
	
	
	// 공통(추가 필드)
	// private String notiImage;
	private String notiType;
	private String notiDaysAgo;
	
	
	// 예약 타입(추가 필드)
	private String notiCheckIn;
	private String notiCheckOut;
	private String notiStayName;
	
	
	// 댓글 타입(추가 필드)
	private String notiCommentWriter;
	private String notiCommentContent;
	// private String 게시글 PK??
	
	
	
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
	public String getNotiTargetNum() {
		return notiTargetNum;
	}
	public void setNotiTargetNum(String notiTargetNum) {
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
	public String getNotiType() {
		return notiType;
	}
	public void setNotiType(String notiType) {
		this.notiType = notiType;
	}
	public String getNotiDaysAgo() {
		return notiDaysAgo;
	}
	public void setNotiDaysAgo(String notiDaysAgo) {
		this.notiDaysAgo = notiDaysAgo;
	}
	public String getNotiCheckIn() {
		return notiCheckIn;
	}
	public void setNotiCheckIn(String notiCheckIn) {
		this.notiCheckIn = notiCheckIn;
	}
	public String getNotiCheckOut() {
		return notiCheckOut;
	}
	public void setNotiCheckOut(String notiCheckOut) {
		this.notiCheckOut = notiCheckOut;
	}
	public String getNotiStayName() {
		return notiStayName;
	}
	public void setNotiStayName(String notiStayName) {
		this.notiStayName = notiStayName;
	}
	public String getNotiCommentWriter() {
		return notiCommentWriter;
	}
	public void setNotiCommentWriter(String notiCommentWriter) {
		this.notiCommentWriter = notiCommentWriter;
	}
	public String getNotiCommentContent() {
		return notiCommentContent;
	}
	public void setNotiCommentContent(String notiCommentcontent) {
		this.notiCommentContent = notiCommentcontent;
	}
	
	
}
