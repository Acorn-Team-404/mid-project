package model.page;

public class PageDto {
	private long pageNum;
	private long stayNum;
	private long usersNum;
	private String pageContent;
	private String pageCreatedAt;
	private String pageUpdateAt;
	private String pageDelete;
	private String pageReserve;
	private String pageGuide;
	private String pageRefund;
	
	// 숙소 정보, 작성자, 키워드
	private String stayName;
	private String usersName;
	private String usersId;
	private String keyword;
	
	// 페이징 처리를 위한 필드
	private int startRowNum;
	private int endRowNum;
		
	// 이전글, 다음글
	private int prevNum;
	private int nextNum;
	
	// setter, getter
	public long getPageNum() {
		return pageNum;
	}
	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}
	public long getStayNum() {
		return stayNum;
	}
	public void setStayNum(long stayNum) {
		this.stayNum = stayNum;
	}
	public long getUsersNum() {
		return usersNum;
	}
	public void setUsersNum(long usersNum) {
		this.usersNum = usersNum;
	}
	public String getPageContent() {
		return pageContent;
	}
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public String getPageCreatedAt() {
		return pageCreatedAt;
	}
	public void setPageCreatedAt(String pageCreatedAt) {
		this.pageCreatedAt = pageCreatedAt;
	}
	public String getPageUpdateAt() {
		return pageUpdateAt;
	}
	public void setPageUpdateAt(String pageUpdateAt) {
		this.pageUpdateAt = pageUpdateAt;
	}
	public String getPageDelete() {
		return pageDelete;
	}
	public void setPageDelete(String pageDelete) {
		this.pageDelete = pageDelete;
	}
	public String getPageReserve() {
		return pageReserve;
	}
	public void setPageReserve(String pageReserve) {
		this.pageReserve = pageReserve;
	}
	public String getPageGuide() {
		return pageGuide;
	}
	public void setPageGuide(String pageGuide) {
		this.pageGuide = pageGuide;
	}
	public String getPageRefund() {
		return pageRefund;
	}
	public void setPageRefund(String pageRefund) {
		this.pageRefund = pageRefund;
	}
	public String getStayName() {
		return stayName;
	}
	public void setStayName(String stayName) {
		this.stayName = stayName;
	}
	public String getUsersName() {
		return usersName;
	}
	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}
	public String getUsersId() {
		return usersId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getStartRowNum() {
		return startRowNum;
	}
	public void setStartRowNum(int startRowNum) {
		this.startRowNum = startRowNum;
	}
	public int getEndRowNum() {
		return endRowNum;
	}
	public void setEndRowNum(int endRowNum) {
		this.endRowNum = endRowNum;
	}
	public int getPrevNum() {
		return prevNum;
	}
	public void setPrevNum(int prevNum) {
		this.prevNum = prevNum;
	}
	public int getNextNum() {
		return nextNum;
	}
	public void setNextNum(int nextNum) {
		this.nextNum = nextNum;
	}
}