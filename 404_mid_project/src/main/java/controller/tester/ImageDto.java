package controller.tester;

public class ImageDto {
	private int id;
	private String fileName;
	private String createdAt;
	
	public ImageDto() {}
	
	public ImageDto(int id, String fileName, String createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.createdAt = createdAt;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
