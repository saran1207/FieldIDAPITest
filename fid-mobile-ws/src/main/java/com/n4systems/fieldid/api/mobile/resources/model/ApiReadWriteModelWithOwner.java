package com.n4systems.fieldid.api.mobile.resources.model;

public class ApiReadWriteModelWithOwner extends ApiReadWriteModel {
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
