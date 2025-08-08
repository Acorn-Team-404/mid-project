package model.page;

public class ScopeDto {
	private long scopeNum;
	private long stayNum;
	private long userNum;
	private int scopeStar;
	private String scopeContent;
	private String scopeCreatedAt;
	private String scopeDelete;
	
	private String usersId;
	
	// setter, getter
	public long getScopeNum() {
		return scopeNum;
	}
	public void setScopeNum(long scopeNum) {
		this.scopeNum = scopeNum;
	}
	public long getStayNum() {
		return stayNum;
	}
	public void setStayNum(long stayNum) {
		this.stayNum = stayNum;
	}
	public long getUserNum() {
		return userNum;
	}
	public void setUserNum(long userNum) {
		this.userNum = userNum;
	}
	public int getScopeStar() {
		return scopeStar;
	}
	public void setScopeStar(int scopeStar) {
		this.scopeStar = scopeStar;
	}
	public String getScopeContent() {
		return scopeContent;
	}
	public void setScopeContent(String scopeContent) {
		this.scopeContent = scopeContent;
	}
	public String getScopeCreatedAt() {
		return scopeCreatedAt;
	}
	public void setScopeCreatedAt(String scopeCreatedAt) {
		this.scopeCreatedAt = scopeCreatedAt;
	}
	public String getScopeDelete() {
		return scopeDelete;
	}
	public void setScopeDelete(String scopeDelete) {
		this.scopeDelete = scopeDelete;
	}
	public String getUsersId() {
		return usersId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}
}