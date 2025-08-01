package model.post;

public class CommentDto {
	private int commentNum;
	private int commentWriter;
	private String commentContent;
	private int commentTargetWriter;
	private int commentGroupNum;
	private int commentParentNum;
	private String commentDeleted;
	private String commentCreatedAt;
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getCommentWriter() {
		return commentWriter;
	}
	public void setCommentWriter(int commentWriter) {
		this.commentWriter = commentWriter;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public int getCommentTargetWriter() {
		return commentTargetWriter;
	}
	public void setCommentTargetWriter(int commentTargetWriter) {
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
	
	
}
