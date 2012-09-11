package com.n4systems.reporting;

import java.io.InputStream;

public class CriteriaResultImageView {
	private String comments;
	private InputStream image;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
}
