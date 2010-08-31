package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.tools.Pager;

public class InternalOrgListResponse extends AbstractListResponse {
	
	public InternalOrgListResponse() {
		super();
	}

	public InternalOrgListResponse(Pager<?> page, int pageSize) {
		super(page, pageSize);
	}

	private List<InternalOrgServiceDTO> internalOrgs = new ArrayList<InternalOrgServiceDTO>();

	public List<InternalOrgServiceDTO> getInternalOrgs() {
		return internalOrgs;
	}

	public void setInternalOrgs(List<InternalOrgServiceDTO> internalOrgs) {
		this.internalOrgs = internalOrgs;
	}
}
