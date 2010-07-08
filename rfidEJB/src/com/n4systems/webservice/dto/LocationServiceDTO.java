package com.n4systems.webservice.dto;

import static com.n4systems.webservice.dto.MobileDTOHelper.*;

public class LocationServiceDTO {
	
	private String freeformLocation;
	private long predefinedLocationId;

	public String getFreeformLocation() {
		return freeformLocation;
	}
	public void setFreeformLocation(String location) {
		this.freeformLocation = location;
	}

	public long getPredefinedLocationId() {
		return predefinedLocationId;
	}
	public void setPredefinedLocationId(long predefinedLocationId) {
		this.predefinedLocationId = predefinedLocationId;
	}
	public boolean predefinedLocationIdExists() {
		return isValidServerId(predefinedLocationId);
	}
	

}
