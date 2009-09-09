package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class SecondaryOrgListResponse extends AbstractListResponse {
	
	private List<SecondaryOrgServiceDTO> secondaryOrgs = new ArrayList<SecondaryOrgServiceDTO>();

	public List<SecondaryOrgServiceDTO> getSecondaryOrgs() {
		return secondaryOrgs;
	}

	public void setSecondaryOrgs(List<SecondaryOrgServiceDTO> secondaryOrgs) {
		this.secondaryOrgs = secondaryOrgs;
	}
}
