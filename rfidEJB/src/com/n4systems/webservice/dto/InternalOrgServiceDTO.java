package com.n4systems.webservice.dto;

public class InternalOrgServiceDTO extends AbstractBaseOrgServiceDTO {
	
	private boolean primary;

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
}
