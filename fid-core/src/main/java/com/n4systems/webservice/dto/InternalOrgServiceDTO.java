package com.n4systems.webservice.dto;

public class InternalOrgServiceDTO extends AbstractBaseOrgServiceDTO {
	
	private boolean primaryOrg;

	public boolean isPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(boolean primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

}
