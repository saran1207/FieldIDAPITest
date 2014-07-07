package com.n4systems.reporting;

import java.io.InputStream;
import java.net.URL;

public class CriteriaResultImageView {
	private String comments;
	private InputStream image;
    private URL imageUrl;

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

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }
}
