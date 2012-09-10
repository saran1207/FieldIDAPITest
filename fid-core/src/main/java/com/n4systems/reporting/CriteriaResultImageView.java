package com.n4systems.reporting;

public class CriteriaResultImageView {
	private String comments;
	private byte[] image;

	public CriteriaResultImageView(String comments, byte[] image) {
		this.comments = comments;
		this.image = image;
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
}
