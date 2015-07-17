package com.n4systems.fieldid.ws.v2.resources.setupdata.assettypeattachment;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModel;

import java.net.URL;

public class ApiAssetTypeAttachment extends ApiReadWriteModel {
	private Long assetTypeId;
	private String comments;
	private URL url;
	private String mimeType;
	private String fileName;

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
