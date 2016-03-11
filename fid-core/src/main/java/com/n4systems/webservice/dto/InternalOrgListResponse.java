package com.n4systems.webservice.dto;

import com.n4systems.tools.Pager;

import java.util.ArrayList;
import java.util.List;

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
