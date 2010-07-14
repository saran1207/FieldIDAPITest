package com.n4systems.webservice.dto;

public interface LocationServiceDTO {
	public String getLocation();
	public void setLocation(String freeFormLocation);
	public Long getPredefinedLocationId();
	public void setPredefinedLocationId(Long predefinedLocationId);
}
