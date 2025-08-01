package model.page;

public class ScopeDto {
	private long scope_num;
	private long stay_num;
	private long user_num;
	private int scope_star;
	private String scope_content;
	private String scope_created_at;
	private String scope_delete;
	
	
	public long getScope_num() {
		return scope_num;
	}
	public void setScope_num(long scope_num) {
		this.scope_num = scope_num;
	}
	public long getStay_num() {
		return stay_num;
	}
	public void setStay_num(long stay_num) {
		this.stay_num = stay_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public int getScope_star() {
		return scope_star;
	}
	public void setScope_star(int scope_star) {
		this.scope_star = scope_star;
	}
	public String getScope_content() {
		return scope_content;
	}
	public void setScope_content(String scope_content) {
		this.scope_content = scope_content;
	}
	public String getScope_created_at() {
		return scope_created_at;
	}
	public void setScope_created_at(String scope_created_at) {
		this.scope_created_at = scope_created_at;
	}
	public String getScope_delete() {
		return scope_delete;
	}
	public void setScope_delete(String scope_delete) {
		this.scope_delete = scope_delete;
	}
}