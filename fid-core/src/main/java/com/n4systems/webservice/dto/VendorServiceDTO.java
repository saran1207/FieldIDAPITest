package com.n4systems.webservice.dto;

public class VendorServiceDTO extends AbstractBaseOrgServiceDTO {
	
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
