package com.n4systems.fieldid.ws.v1.resources.tenant;

public class ApiTenant {
	private String serialNumberLabel;
	private boolean usingAssignedTo;
	private boolean usingJobSites;
	private boolean usingAdvancedLocation;
	private boolean usingOrderDetails;
	private boolean usingGpsCapture;
	
	public void setSerialNumberLabel(String serialNumberLabel) {
		this.serialNumberLabel = serialNumberLabel;
	}

	public String getSerialNumberLabel() {
		return serialNumberLabel;
	}
	
	public void setUsingAssignedTo(boolean usingAssignedTo) {
		this.usingAssignedTo = usingAssignedTo;
	}

	public boolean isUsingAssignedTo() {
		return usingAssignedTo;
	}

	public void setUsingJobSites(boolean usingJobSites) {
		this.usingJobSites = usingJobSites;
	}

	public boolean isUsingJobSites() {
		return usingJobSites;
	}

	public void setUsingAdvancedLocation(boolean usingAdvancedLocation) {
		this.usingAdvancedLocation = usingAdvancedLocation;
	}

	public boolean isUsingAdvancedLocation() {
		return usingAdvancedLocation;
	}

	public void setUsingOrderDetails(boolean usingOrderDetails) {
		this.usingOrderDetails = usingOrderDetails;
	}

	public boolean isUsingOrderDetails() {
		return usingOrderDetails;
	}

	public void setUsingGpsCapture(boolean usingGpsCapture) {
		this.usingGpsCapture = usingGpsCapture;
	}

	public boolean isUsingGpsCapture() {
		return usingGpsCapture;
	}
	
	
}
