package com.n4systems.fieldid.api.mobile.resources.model;

public abstract class ApiReadonlyModelWithOwner extends ApiReadonlyModel {
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
}
