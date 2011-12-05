package com.n4systems.fieldid.ws.v1.resources.model;

public class ApiReadWriteModelWithOwner extends ApiReadWriteModel {
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
