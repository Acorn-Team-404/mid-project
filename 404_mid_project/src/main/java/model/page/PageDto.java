package model.page;

public class PageDto {
	private int page_num;
	private int stay_num;
	private int user_num;
	private String page_content;
	private String page_created_at;
	private String page_update_at;
	private String page_delete;
	
	// 숙소 정보, 작성자, 키워드
	private String stay_name;
	private String user_name;
	private String keyword;
	
	// setter, getter
	public int getPage_num() {
		return page_num;
	}
	public void setPage_num(int page_num) {
		this.page_num = page_num;
	}
	public int getStay_num() {
		return stay_num;
	}
	public void setStay_num(int stay_num) {
		this.stay_num = stay_num;
	}
	public int getUser_num() {
		return user_num;
	}
	public void setUser_num(int user_num) {
		this.user_num = user_num;
	}
	public String getPage_content() {
		return page_content;
	}
	public void setPage_content(String page_content) {
		this.page_content = page_content;
	}
	public String getPage_created_at() {
		return page_created_at;
	}
	public void setPage_created_at(String page_created_at) {
		this.page_created_at = page_created_at;
	}
	public String getPage_update_at() {
		return page_update_at;
	}
	public void setPage_update_at(String page_update_at) {
		this.page_update_at = page_update_at;
	}
	public String getPage_delete() {
		return page_delete;
	}
	public void setPage_delete(String page_delete) {
		this.page_delete = page_delete;
	}
	public String getStay_name() {
		return stay_name;
	}
	public void setStay_name(String stay_name) {
		this.stay_name = stay_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
