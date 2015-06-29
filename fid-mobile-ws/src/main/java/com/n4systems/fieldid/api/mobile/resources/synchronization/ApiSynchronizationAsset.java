package com.n4systems.fieldid.api.mobile.resources.synchronization;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	
}
