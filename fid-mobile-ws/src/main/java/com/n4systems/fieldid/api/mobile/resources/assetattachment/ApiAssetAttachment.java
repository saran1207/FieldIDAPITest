package com.n4systems.fieldid.api.mobile.resources.assetattachment;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadWriteModel;

import java.net.URL;

public class ApiAssetAttachment extends ApiReadWriteModel {
	private String assetId;
	private String comments;
	private String fileName;
    private String filePath;
	private boolean image;
	private byte[] data;
    private URL url;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

	public boolean isImage() {
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
