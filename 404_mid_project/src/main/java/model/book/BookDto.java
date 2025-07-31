package model.book;

public class BookDto {
	// 예약 번호
	private String num;
	// 예약자 아이디
	private int userNum;
	// 숙소 번호
	private int stayNum;
	// 객실 번호
	private int roomId;
	// 체크인 날짜
	private String checkIn;
	// 체크아웃 날짜
	private String checkOut;
	// 성인 수
	private int adult;
	// 어린이 수
	private int children;
	// 유아 수
	private int infant;
	// 총 인원 수
	private int totalPax;
	// 간이 침대
	private int extraBed;
	// 유아 침대
	private int infantBed;
	// 체크인 시간
	private String checkInTime;
	// 추가 요청 사항
	private String request;
	// 총 금액
	private int totalAmount;
	// 
	private String statusGroupId;
	// 예약 상태
	private int statusCode;
	// 예약 날짜
	private String createdAt;
	// 예약 수정 날짜
	private String updatedAt;
	
	// setter & getter
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getStayNum() {
		return stayNum;
	}
	public void setStayNum(int stayNum) {
		this.stayNum = stayNum;
	}
	public String getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}
	public String getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(String checkOut) {
		this.checkOut = checkOut;
	}
	public int getAdult() {
		return adult;
	}
	public void setAdult(int adult) {
		this.adult = adult;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
	public int getInfant() {
		return infant;
	}
	public void setInfant(int infant) {
		this.infant = infant;
	}
	public int getTotalPax() {
		return totalPax;
	}
	public void setTotalPax(int totalPax) {
		this.totalPax = totalPax;
	}
	public int getExtraBed() {
		return extraBed;
	}
	public void setExtraBed(int extraBed) {
		this.extraBed = extraBed;
	}
	public int getInfantBed() {
		return infantBed;
	}
	public void setInfantBed(int infantBed) {
		this.infantBed = infantBed;
	}
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatusGroupId() {
		return statusGroupId;
	}
	public void setStatusGroupId(String statusGroupId) {
		this.statusGroupId = statusGroupId;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
