package com.n4systems.fieldid.ws.v2.resources.setupdata.assettypeattachment;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModel;

public class ApiAssetTypeAttachment extends ApiReadWriteModel {
	private Long assetTypeId;
	private String comments;
	private byte[] data;
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

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
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
