package model.room;

public class RoomDto {
	// 객실 번호
	private long roomNum;
	// 숙소 번호
	private int roomStayNum;
	// 객실명
	private String roomName;
	// 객실 타입
	private String roomType;
	// 객실 요금
	private int roomPrice;
	// 객실 내 최대 성인 수
	private int roomAdultMax;
	// 객실 내 최대 어린이 수
	private int roomChildrenMax;
	// 객실 내 최대 영아 수
	private int roomInfantMax;
	// 객실 내 최대 인원 수
	private int roomPaxMax;
	// 객실 체크인 날짜
	private String roomCheckIn;
	// 객실 체크아웃 날짜
	private String roomCheckOut;
	// 객실 예약 불가 날짜
	private String roomBlockDate;
	// 객실 설명
	private String roomContent;
	
	// 숙소명 s.STAY_NAME 
	private String roomStayName;
	// 숙소 편의시설 s.STAY_FACILITIES
	private String roomStayFacilities;
	
	// setter&getter for s.STAY_NAME, s.STAY_FACILITIES
	public void setRoomStayName(String roomStayName) {
		this.roomStayName = roomStayName;
	}
	
	public String getRoomStayName() {
		return roomStayName;
	}
	
	public void setRoomStayFacilities(String roomStayFacilities) {
		this.roomStayFacilities = roomStayFacilities;
	}
	
	public String getRoomStayFacilities() {
		return roomStayFacilities;
	}
	

	
	// setter & getter
	public long getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(long roomNum) {
		this.roomNum = roomNum;
	}
	public int getRoomStayNum() {
		return roomStayNum;
	}
	public void setRoomStayNum(int roomStayNum) {
		this.roomStayNum = roomStayNum;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public int getRoomPrice() {
		return roomPrice;
	}
	public void setRoomPrice(int roomPrice) {
		this.roomPrice = roomPrice;
	}
	public int getRoomAdultMax() {
		return roomAdultMax;
	}
	public void setRoomAdultMax(int roomAdultMax) {
		this.roomAdultMax = roomAdultMax;
	}
	public int getRoomChildrenMax() {
		return roomChildrenMax;
	}
	public void setRoomChildrenMax(int roomChildrenMax) {
		this.roomChildrenMax = roomChildrenMax;
	}
	public int getRoomInfantMax() {
		return roomInfantMax;
	}
	public void setRoomInfantMax(int roomInfantMax) {
		this.roomInfantMax = roomInfantMax;
	}
	public int getRoomPaxMax() {
		return roomPaxMax;
	}
	public void setRoomPaxMax(int roomPaxMax) {
		this.roomPaxMax = roomPaxMax;
	}
	public String getRoomCheckIn() {
		return roomCheckIn;
	}
	public void setRoomCheckIn(String roomCheckIn) {
		this.roomCheckIn = roomCheckIn;
	}
	public String getRoomCheckOut() {
		return roomCheckOut;
	}
	public void setRoomCheckOut(String roomCheckOut) {
		this.roomCheckOut = roomCheckOut;
	}
	public String getRoomBlockDate() {
		return roomBlockDate;
	}
	public void setRoomBlockDate(String roomBlockDate) {
		this.roomBlockDate = roomBlockDate;
	}
	public String getRoomContent() {
		return roomContent;
	}
	public void setRoomContent(String roomContent) {
		this.roomContent = roomContent;
	}

	

}
