package model.post;

public class postDto {
	private int postNum;
	private int postWriterNum;
	private String postTitle;
	private String postContent;
	private int postType;
	private int postStayNum;
	private int postViews;
	private String postCreatedAt;
	private String postUpdatedAt;
	private String postDeleted;
	
	public int getPostNum() {
		return postNum;
	}
	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}
	public int getPostWriterNum() {
		return postWriterNum;
	}
	public void setPostWriterNum(int postWriterNum) {
		this.postWriterNum = postWriterNum;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public int getPostType() {
		return postType;
	}
	public void setPostType(int postType) {
		this.postType = postType;
	}
	public int getPostStayNum() {
		return postStayNum;
	}
	public void setPostStayNum(int postStayNum) {
		this.postStayNum = postStayNum;
	}
	public int getPostViews() {
		return postViews;
	}
	public void setPostViews(int postViews) {
		this.postViews = postViews;
	}
	public String getPostCreatedAt() {
		return postCreatedAt;
	}
	public void setPostCreatedAt(String postCreatedAt) {
		this.postCreatedAt = postCreatedAt;
	}
	public String getPostUpdatedAt() {
		return postUpdatedAt;
	}
	public void setPostUpdatedAt(String postUpdatedAt) {
		this.postUpdatedAt = postUpdatedAt;
	}
	public String getPostDeleted() {
		return postDeleted;
	}
	public void setPostDeleted(String postDeleted) {
		this.postDeleted = postDeleted;
	}
	
	
}
