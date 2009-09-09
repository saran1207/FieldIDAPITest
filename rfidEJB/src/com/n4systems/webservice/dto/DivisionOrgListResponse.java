package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class DivisionOrgListResponse extends AbstractListResponse {

	private List<DivisionOrgServiceDTO> divisions = new ArrayList<DivisionOrgServiceDTO>();

	public List<DivisionOrgServiceDTO> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<DivisionOrgServiceDTO> divisions) {
		this.divisions = divisions;
	}
}
