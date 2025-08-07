package model.review;

import java.sql.Timestamp;

public class ReviewDto {
	
	private long reviewId;
	private String bookNum;
	private String usersId;
	private int rating;
	private String comment;
	private Timestamp createdAt;
	
	private long reviewStayNum;
	
	//페이징 처리를 위한 필드
	private long startRowNum;
	private long endRowNum;
	
	public long getStartRowNum() {
		return startRowNum;
	}

	public long getEndRowNum() {
		return endRowNum;
	}

	public void setStartRowNum(long startRowNum) {
		this.startRowNum = startRowNum;
	}

	public void setEndRowNum(long endRowNum) {
		this.endRowNum = endRowNum;
	}

	public ReviewDto() {
		
	}
	
	public long getReviewStayNum() {
		return reviewStayNum;
	}

	public void setReviewStayNum(long reviewStayNum) {
		this.reviewStayNum = reviewStayNum;
	}
	
	public long getReviewId() {
		return reviewId;
	}
	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
	}
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public String getUsersId() {
		return usersId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
