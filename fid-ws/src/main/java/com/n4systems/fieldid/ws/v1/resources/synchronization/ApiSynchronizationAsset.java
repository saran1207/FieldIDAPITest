package com.n4systems.fieldid.ws.v1.resources.synchronization;

import java.util.Date;

public class ApiSynchronizationAsset {
	private String assetId;
	private Date modified;

	public ApiSynchronizationAsset() {
		this(null, null);
	}
	
	public ApiSynchronizationAsset(String assetId, Date modified) {
		this.assetId = assetId;
		this.modified = modified;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

}
