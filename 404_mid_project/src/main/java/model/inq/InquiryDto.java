package model.inq;

public class InquiryDto {
	private long num;
	private long stayNum;
	private long userNum;
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
	
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public long getStayNum() {
		return stayNum;
	}
	public void setStayNum(long stayNum) {
		this.stayNum = stayNum;
	}
	public long getUsersNum() {
		return userNum;
	}
	public void setUsersNum(long usersNum) {
		this.userNum = usersNum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public long getIsAnswered() {
		return isAnswered;
	}
	public void setIsAnswered(long isAnswered) {
		this.isAnswered = isAnswered;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnsweredAt() {
		return answeredAt;
	}
	public void setAnsweredAt(String answeredAt) {
		this.answeredAt = answeredAt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStayName() {
		return stayName;
	}
	public void setStayName(String stayName) {
		this.stayName = stayName;
	}
	
}