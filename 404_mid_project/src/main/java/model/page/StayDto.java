package model.page;

public class StayDto {
	private long stayNum;
	private long stayUsersNum;
	private String stayName;
	private String stayAddr;
	private String stayLoc;
	private String stayLat;
	private String stayLong;
	private String stayPhone;
	private String stayUpdateAt;
	private String stayDelete;
	private String stayFacilities;
	
	private String stayUsersId;
	
	// setter, getter
	public String getUsersId() {
		return stayUsersId;
	}
	
	public void setUsersId(String usersId) {
		this.stayUsersId = usersId;
	}
	
	public long getStayNum() {
		return stayNum;
	}

	public void setStayNum(long stayNum) {
		this.stayNum = stayNum;
	}

	public long getStayUsersNum() {
		return stayUsersNum;
	}

	public void setStayUsersNum(long stayUsersNum) {
		this.stayUsersNum = stayUsersNum;
	}

	public String getStayName() {
		return stayName;
	}

	public void setStayName(String stayName) {
		this.stayName = stayName;
	}

	public String getStayAddr() {
		return stayAddr;
	}

	public void setStayAddr(String stayAddr) {
		this.stayAddr = stayAddr;
	}

	public String getStayLoc() {
		return stayLoc;
	}

	public void setStayLoc(String stayLoc) {
		this.stayLoc = stayLoc;
	}

	public String getStayLat() {
		return stayLat;
	}

	public void setStayLat(String stayLat) {
		this.stayLat = stayLat;
	}

	public String getStayLong() {
		return stayLong;
	}

	public void setStayLong(String stayLong) {
		this.stayLong = stayLong;
	}

	public String getStayPhone() {
		return stayPhone;
	}

	public void setStayPhone(String stayPhone) {
		this.stayPhone = stayPhone;
	}

	public String getStayUpdateAt() {
		return stayUpdateAt;
	}

	public void setStayUpdateAt(String stayUpdateAt) {
		this.stayUpdateAt = stayUpdateAt;
	}

	public String getStayDelete() {
		return stayDelete;
	}

	public void setStayDelete(String stayDelete) {
		this.stayDelete = stayDelete;
	}

	public String getStayFacilities() {
		return stayFacilities;
	}

	public void setStayFacilities(String stayFacilities) {
		this.stayFacilities = stayFacilities;
	}

}