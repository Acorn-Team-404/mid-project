package model.post;

public class CommentDto {
	private int commentNum;
	private String commentWriter;
	private String commentContent;
	private String commentTargetWriter;
	private int commentGroupNum;
	private int commentParentNum;
	private String commentDeleted;
	private String commentCreatedAt;
	private String commentProfileImage; //프로필이미지
	private int commentReplyCount; // 대댓글의 갯수 저장
	
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public String getCommentWriter() {
		return commentWriter;
	}
	public void setCommentWriter(String commentWriter) {
		this.commentWriter = commentWriter;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentTargetWriter() {
		return commentTargetWriter;
	}
	public void setCommentTargetWriter(String commentTargetWriter) {
		this.commentTargetWriter = commentTargetWriter;
	}
	public int getCommentGroupNum() {
		return commentGroupNum;
	}
	public void setCommentGroupNum(int commentGroupNum) {
		this.commentGroupNum = commentGroupNum;
	}
	public int getCommentParentNum() {
		return commentParentNum;
	}
	public void setCommentParentNum(int commentParentNum) {
		this.commentParentNum = commentParentNum;
	}
	public String getCommentDeleted() {
		return commentDeleted;
	}
	public void setCommentDeleted(String commentDeleted) {
		this.commentDeleted = commentDeleted;
	}
	public String getCommentCreatedAt() {
		return commentCreatedAt;
	}
	public void setCommentCreatedAt(String commentCreatedAt) {
		this.commentCreatedAt = commentCreatedAt;
	}
	
	public String getCommentProfileImage() {
		return commentProfileImage;
	}
	public void setCommentProfileImage(String commentProfileImage) {
		this.commentProfileImage = commentProfileImage;
	}
	
	public int getCommentReplyCount() {
		return commentReplyCount;
	}
	public void setCommentReplyCount(int commentReplyCount) {
		this.commentReplyCount = commentReplyCount;
	}
}
