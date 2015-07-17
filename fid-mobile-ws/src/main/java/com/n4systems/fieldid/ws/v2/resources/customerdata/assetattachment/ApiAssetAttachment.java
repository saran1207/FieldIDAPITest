package com.n4systems.fieldid.ws.v2.resources.customerdata.assetattachment;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModel;

import java.net.URL;

public class ApiAssetAttachment extends ApiReadWriteModel {
	private String assetId;
	private String comments;
	private byte[] data;
    private URL url;
	private String mimeType;

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

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
