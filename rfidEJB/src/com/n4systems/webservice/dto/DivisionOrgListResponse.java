package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.tools.Pager;

public class DivisionOrgListResponse extends AbstractListResponse {

	public DivisionOrgListResponse() {
		super();
	}

	public DivisionOrgListResponse(Pager<?> page, int pageSize) {
		super(page, pageSize);
	}

	private List<DivisionOrgServiceDTO> divisions = new ArrayList<DivisionOrgServiceDTO>();

	public List<DivisionOrgServiceDTO> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<DivisionOrgServiceDTO> divisions) {
		this.divisions = divisions;
	}
}
