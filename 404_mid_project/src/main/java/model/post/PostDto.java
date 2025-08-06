package model.post;

public class PostDto {
	private int postNum;
	private Long postWriterNum;
	private String postTitle;
	private String postContent;
	private int postType;
	private int postStayNum;
	private int postViews;
	private String postCreatedAt;
	private String postUpdatedAt;
	private String postDeleted;
	private String usersID;
	//페이징 처리를 위한 필드
	private int startRowNum;
	private int endRowNum;
	//검색 키워드 담기 위한 필드
	private String postKeyword;
	public int getPostNum() {
		return postNum;
	}
	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}
	public Long getPostWriterNum() {
		return postWriterNum;
	}
	public void setPostWriterNum(Long postWriterNum) {
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
	public String getUsersID() {
		return usersID;
	}
	public void setUsersID(String usersID) {
		this.usersID = usersID;
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
	public String getPostKeyword() {
		return postKeyword;
	}
	public void setPostKeyword(String postKeyword) {
		this.postKeyword = postKeyword;
	}
	
	
}
