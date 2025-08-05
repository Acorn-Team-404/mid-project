package model.inq;

public class InquiryDto {
	private long num;
	private Long stayNum;
	private long usersNum;
	private String title;
	private String content;
	private String type;
	private String createdAt;
	private long isAnswered;
	private String answer;
	private String answeredAt;
	private String userName;
	private String phone;
	private String email;
	private String stayName;
	//검색 기간을 담기 위한 필드
	private String startDate;
	private String endDate;
	//페이징 처리를 위한 필드
	private long startRowNum;
	private long endRowNum;
	
	
	public long getNum() {
		return num;
	}
	public Long getStayNum() {
		return stayNum;
	}
	public long getUsersNum() {
		return usersNum;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getType() {
		return type;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public long getIsAnswered() {
		return isAnswered;
	}
	public String getAnswer() {
		return answer;
	}
	public String getAnsweredAt() {
		return answeredAt;
	}
	public String getUserName() {
		return userName;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public String getStayName() {
		return stayName;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public long getStartRowNum() {
		return startRowNum;
	}
	public long getEndRowNum() {
		return endRowNum;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public void setStayNum(Long stayNum) {
		this.stayNum = stayNum;
	}
	public void setUsersNum(long usersNum) {
		this.usersNum = usersNum;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public void setIsAnswered(long isAnswered) {
		this.isAnswered = isAnswered;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setAnsweredAt(String answeredAt) {
		this.answeredAt = answeredAt;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setStayName(String stayName) {
		this.stayName = stayName;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setStartRowNum(long startRowNum) {
		this.startRowNum = startRowNum;
	}
	public void setEndRowNum(long endRowNum) {
		this.endRowNum = endRowNum;
	}
}