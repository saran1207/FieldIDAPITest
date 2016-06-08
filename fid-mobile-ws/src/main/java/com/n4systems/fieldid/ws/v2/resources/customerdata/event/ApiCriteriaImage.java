package com.n4systems.fieldid.ws.v2.resources.customerdata.event;

public class ApiCriteriaImage {
	private String criteriaResultSid;
	private String fileName;
	private String comments;
	private byte[] image;
	private byte[] srcImage;

	public String getCriteriaResultSid() {
		return criteriaResultSid;
	}

	public void setCriteriaResultSid(String criteriaResultSid) {
		this.criteriaResultSid = criteriaResultSid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getSrcImage() {
		return srcImage;
	}

	public ApiCriteriaImage setSrcImage(byte[] srcImage) {
		this.srcImage = srcImage;
		return this;
	}
}
