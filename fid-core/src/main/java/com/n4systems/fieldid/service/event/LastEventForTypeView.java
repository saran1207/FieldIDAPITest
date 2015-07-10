package com.n4systems.fieldid.service.event;

import java.util.Date;

public class LastEventForTypeView {
	private final String mobileId;
	private final Long assetId;
	private final Date modified;
	private final Long typeId;
	private final Date completed;

	public LastEventForTypeView(String mobileId, Long assetId, Date modified, Long typeId, Date completed) {
		this.mobileId = mobileId;
		this.assetId = assetId;
		this.modified = modified;
		this.typeId = typeId;
		this.completed = completed;
	}

	public String getMobileId() {
		return mobileId;
	}

	public Long getAssetId() {
		return assetId;
	}

	public Date getModified() {
		return modified;
	}

	public Long getTypeId() {
		return typeId;
	}

	public Date getCompleted() {
		return completed;
	}
}
