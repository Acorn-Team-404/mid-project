package model.image;

public class ImageDto {
	private int imageNum;
    private String imageOriginalName;
    private String imageSavedName;
    private String imageUploadDate;
    private int imageSortOrder;
    private String imageTargetType;
    private int imageTargetId;
    
    public ImageDto() {}

	public int getImageNum() {
		return imageNum;
	}

	public void setImageNum(int imageNum) {
		this.imageNum = imageNum;
	}

	public String getImageOriginalName() {
		return imageOriginalName;
	}

	public void setImageOriginalName(String imageOriginalName) {
		this.imageOriginalName = imageOriginalName;
	}

	public String getImageSavedName() {
		return imageSavedName;
	}

	public void setImageSavedName(String imageSavedName) {
		this.imageSavedName = imageSavedName;
	}

	public String getImageUploadDate() {
		return imageUploadDate;
	}

	public void setImageUploadDate(String imageUploadDate) {
		this.imageUploadDate = imageUploadDate;
	}

	public int getImageSortOrder() {
		return imageSortOrder;
	}

	public void setImageSortOrder(int imageSortOrder) {
		this.imageSortOrder = imageSortOrder;
	}

	public String getImageTargetType() {
		return imageTargetType;
	}

	public void setImageTargetType(String imageTargetType) {
		this.imageTargetType = imageTargetType;
	}

	public int getImageTargetId() {
		return imageTargetId;
	}

	public void setImageTargetId(int imageTargetId) {
		this.imageTargetId = imageTargetId;
	}
    
    
}
