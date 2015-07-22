package com.n4systems.fieldid.ws.v2.resources.model;

public abstract class ApiReadOnlyModelWithOwner2 extends ApiReadOnlyModel2 {
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
}
