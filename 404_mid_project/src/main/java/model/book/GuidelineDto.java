package model.book;

import java.sql.Clob;

public class GuidelineDto {
	private long guideId;
	private String guideInformation;
	private String stayPolicy;
	private String refundPolicy;
	
	public long getGuideId() {
		return guideId;
	}
	public void setGuideId(long guideId) {
		this.guideId = guideId;
	}
	public String getGuideInformation() {
		return guideInformation;
	}
	public void setGuideInformation(String guideInformation) {
		this.guideInformation = guideInformation;
	}
	public String getStayPolicy() {
		return stayPolicy;
	}
	public void setStayPolicy(String stayPolicy) {
		this.stayPolicy = stayPolicy;
	}
	public String getRefundPolicy() {
		return refundPolicy;
	}
	public void setRefundPolicy(String refundPolicy) {
		this.refundPolicy = refundPolicy;
	}	
}

