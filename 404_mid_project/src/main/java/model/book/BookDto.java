package model.book;

public class BookDto {
	// 예약 번호
	private String bookNum;
	// 예약자 아이디
	private long bookUsersNum;
	// 숙소 번호
	private int bookStayNum;
	// 객실 번호
	private int bookRoomId;
	// 체크인 날짜
	private String bookCheckIn;
	// 체크아웃 날짜
	private String bookCheckOut;
	// 성인 수
	private int bookAdult;
	// 어린이 수
	private int bookChildren;
	// 유아 수
	private int bookInfant;
	// 총 인원 수
	private int bookTotalPax;
	// 간이 침대
	private int bookExtraBed;
	// 유아 침대
	private int bookInfantBed;
	// 체크인 시간
	private String bookCheckInTime;
	// 추가 요청 사항
	private String bookRequest;
	// 총 금액
	private int bookTotalAmount;
	// 
	private String bookStatusGroupId;
	// 예약 상태
	private int bookStatusCode;
	// 예약 날짜
	private String bookCreatedAt;
	// 예약 수정 날짜
	private String bookUpdatedAt;
	
	// setter & getter
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public long getBookUsersNum() {
		return bookUsersNum;
	}
	public void setBookUserNum(long bookUsersNum) {
		this.bookUsersNum = bookUsersNum;
	}
	public int getBookStayNum() {
		return bookStayNum;
	}
	public void setBookStayNum(int bookStayNum) {
		this.bookStayNum = bookStayNum;
	}
	public int getBookRoomId() {
		return bookRoomId;
	}
	public void setBookRoomId(int bookRoomId) {
		this.bookRoomId = bookRoomId;
	}
	public String getBookCheckIn() {
		return bookCheckIn;
	}
	public void setBookCheckIn(String bookCheckIn) {
		this.bookCheckIn = bookCheckIn;
	}
	public String getBookCheckOut() {
		return bookCheckOut;
	}
	public void setBookCheckOut(String bookCheckOut) {
		this.bookCheckOut = bookCheckOut;
	}
	public int getBookAdult() {
		return bookAdult;
	}
	public void setBookAdult(int bookAdult) {
		this.bookAdult = bookAdult;
	}
	public int getBookChildren() {
		return bookChildren;
	}
	public void setBookChildren(int bookChildren) {
		this.bookChildren = bookChildren;
	}
	public int getBookInfant() {
		return bookInfant;
	}
	public void setBookInfant(int bookInfant) {
		this.bookInfant = bookInfant;
	}
	public int getBookTotalPax() {
		return bookTotalPax;
	}
	public void setBookTotalPax(int bookTotalPax) {
		this.bookTotalPax = bookTotalPax;
	}
	public int getBookExtraBed() {
		return bookExtraBed;
	}
	public void setBookExtraBed(int bookExtraBed) {
		this.bookExtraBed = bookExtraBed;
	}
	public int getBookInfantBed() {
		return bookInfantBed;
	}
	public void setBookInfantBed(int bookInfantBed) {
		this.bookInfantBed = bookInfantBed;
	}
	public String getBookCheckInTime() {
		return bookCheckInTime;
	}
	public void setBookCheckInTime(String bookCheckInTime) {
		this.bookCheckInTime = bookCheckInTime;
	}
	public String getBookRequest() {
		return bookRequest;
	}
	public void setBookRequest(String bookRequest) {
		this.bookRequest = bookRequest;
	}
	public int getBookTotalAmount() {
		return bookTotalAmount;
	}
	public void setBookTotalAmount(int bookTotalAmount) {
		this.bookTotalAmount = bookTotalAmount;
	}
	public String getBookStatusGroupId() {
		return bookStatusGroupId;
	}
	public void setBookStatusGroupId(String bookStatusGroupId) {
		this.bookStatusGroupId = bookStatusGroupId;
	}
	public int getBookStatusCode() {
		return bookStatusCode;
	}
	public void setBookStatusCode(int bookStatusCode) {
		this.bookStatusCode = bookStatusCode;
	}
	public String getBookCreatedAt() {
		return bookCreatedAt;
	}
	public void setBookCreatedAt(String bookCreatedAt) {
		this.bookCreatedAt = bookCreatedAt;
	}
	public String getBookUpdatedAt() {
		return bookUpdatedAt;
	}
	public void setBookUpdatedAt(String bookUpdatedAt) {
		this.bookUpdatedAt = bookUpdatedAt;
	}	
}
