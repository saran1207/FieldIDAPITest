package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.tools.Pager;

public class SecondaryOrgListResponse extends AbstractListResponse {
	
	public SecondaryOrgListResponse() {
		super();
	}

	public SecondaryOrgListResponse(Pager<?> page, int pageSize) {
		super(page, pageSize);
	}

	private List<SecondaryOrgServiceDTO> secondaryOrgs = new ArrayList<SecondaryOrgServiceDTO>();

	public List<SecondaryOrgServiceDTO> getSecondaryOrgs() {
		return secondaryOrgs;
	}

	public void setSecondaryOrgs(List<SecondaryOrgServiceDTO> secondaryOrgs) {
		this.secondaryOrgs = secondaryOrgs;
	}
}
