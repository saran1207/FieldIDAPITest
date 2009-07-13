package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class InspectionTypeListResponse extends AbstractListResponse {
	
	private List<InspectionTypeServiceDTO> inspectionTypes = new ArrayList<InspectionTypeServiceDTO>();

	public List<InspectionTypeServiceDTO> getInspectionTypes() {
		return inspectionTypes;
	}

	public void setInspectionTypes(List<InspectionTypeServiceDTO> inspectionTypes) {
		this.inspectionTypes = inspectionTypes;
	}

}
