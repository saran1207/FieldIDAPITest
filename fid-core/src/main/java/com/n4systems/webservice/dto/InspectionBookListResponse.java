package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class InspectionBookListResponse extends AbstractListResponse {

	private List<InspectionBookServiceDTO> inspectionBooks = new ArrayList<InspectionBookServiceDTO>();

	public List<InspectionBookServiceDTO> getInspectionBooks() {
		return inspectionBooks;
	}

	public void setInspectionBooks(List<InspectionBookServiceDTO> inspectionBooks) {
		this.inspectionBooks = inspectionBooks;
	}
	
}
