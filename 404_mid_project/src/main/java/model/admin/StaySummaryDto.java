package model.admin;

public class StaySummaryDto {
    private long stayNum;
    private String stayName;
    private String stayLoc;
    private int minPrice;        // 객실 최저가
    private double avgRating;    // 별점 평균
    private String imageName;    // 대표 이미지 파일명
    private int reviewCount;   // 리뷰 카운트 추가
	public long getStayNum() {
		return stayNum;
	}
	public void setStayNum(long stayNum) {
		this.stayNum = stayNum;
	}
	public String getStayName() {
		return stayName;
	}
	public void setStayName(String stayName) {
		this.stayName = stayName;
	}
	public String getStayLoc() {
		return stayLoc;
	}
	public void setStayLoc(String stayLoc) {
		this.stayLoc = stayLoc;
	}
	public int getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}
	public double getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public int getReviewCount() {
        return reviewCount;
    }
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
    
}
