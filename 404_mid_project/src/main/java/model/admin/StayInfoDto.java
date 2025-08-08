package model.admin;

import java.util.List;
import model.room.RoomDto;

/**
 * STAY + 연관 ROOM 정보를 한 번에 담기 위한 DTO
 */
public class StayInfoDto {
    private long stayNum;            // STAY.STAY_NUM
    private int stayUserNum;         // STAY.STAY_USER_NUM (작성자)
    private String stayName;         // STAY.STAY_NAME
    private String stayAddr;         // STAY.STAY_ADDR
    private String stayLoc;          // STAY.STAY_LOC
    private String stayLat;          // STAY.STAY_LAT
    private String stayLong;         // STAY.STAY_LONG
    private String stayPhone;        // STAY.STAY_PHONE
    private String stayFacilities;   // STAY.STAY_FACILITIES
    private String stayContent;		 // STAY.STAY_CONTENT
    private List<RoomDto> rooms;     // 연관된 ROOM 목록
    
    // getters / setters
    
    public String getStayContent() {
		return stayContent;
	}
    public void setStayContent(String stayContent) {
		this.stayContent = stayContent;
	}
	public long getStayNum() {
		return stayNum;
	}
	public void setStayNum(long stayNum) {
		this.stayNum = stayNum;
	}
	public int getStayUserNum() {
		return stayUserNum;
	}
	public void setStayUserNum(int stayUserNum) {
		this.stayUserNum = stayUserNum;
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
	public String getStayFacilities() {
		return stayFacilities;
	}
	public void setStayFacilities(String stayFacilities) {
		this.stayFacilities = stayFacilities;
	}
	public List<RoomDto> getRooms() {
		return rooms;
	}
	public void setRooms(List<RoomDto> rooms) {
		this.rooms = rooms;
	}

}
