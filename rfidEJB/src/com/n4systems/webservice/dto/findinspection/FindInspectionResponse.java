package com.n4systems.webservice.dto.findinspection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.webservice.dto.InspectionServiceDTO;

public class FindInspectionResponse {
	private List<InspectionServiceDTO> inspections = new ArrayList<InspectionServiceDTO>();

	public List<InspectionServiceDTO> getInspections() {
		return inspections;
	}

	public void setInspections(List<InspectionServiceDTO> inspections) {
		this.inspections = inspections;
	}
}
