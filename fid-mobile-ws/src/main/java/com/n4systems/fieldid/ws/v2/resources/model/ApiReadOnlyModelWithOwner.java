package com.n4systems.fieldid.ws.v2.resources.model;

public abstract class ApiReadOnlyModelWithOwner extends ApiReadOnlyModel {
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
}
