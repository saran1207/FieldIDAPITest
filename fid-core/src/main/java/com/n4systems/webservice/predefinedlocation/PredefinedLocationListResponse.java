package com.n4systems.webservice.predefinedlocation;

import com.n4systems.webservice.dto.AbstractListResponse;

import java.util.ArrayList;
import java.util.List;

public class PredefinedLocationListResponse extends AbstractListResponse {
	private List<PredefinedLocationServiceDTO> locations = new ArrayList<PredefinedLocationServiceDTO>();

	public PredefinedLocationListResponse(int currentPage, long totalPages, int recordsPerPage) {
		super(currentPage, (int)totalPages, recordsPerPage);
	}

	public List<PredefinedLocationServiceDTO> getLocations() {
		return locations;
	}

	public void setLocations(List<PredefinedLocationServiceDTO> locations) {
		this.locations = locations;
	}

}
