package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class InspectionListResponse extends AbstractListResponse {

	private List<InspectionServiceDTO> inspections = new ArrayList<InspectionServiceDTO>();

	public List<InspectionServiceDTO> getInspections() {
		return inspections;
	}

	public void setInspections(List<InspectionServiceDTO> inspections) {
		this.inspections = inspections;
	}
	
}
