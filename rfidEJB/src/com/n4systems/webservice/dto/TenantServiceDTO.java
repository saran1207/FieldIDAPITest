package com.n4systems.webservice.dto;

/**
 * This DTO is sent down to the handheld on every login.  It contains any tenant specific configuration updates
 * and is also used to initially setup the handheld.
 */
public class TenantServiceDTO extends AbstractBaseServiceDTO {
	
	private String name;
	private String displayName;
	private String serialNumberFormat;	
	private boolean usingJobSites;
	private boolean usingSerialNumber;
	private boolean usingJobs;
	private boolean usingIntegration;
	private boolean usingAssignedTo;
	
	public String getSerialNumberFormat() {
		return serialNumberFormat;
	}
	public void setSerialNumberFormat(String serialNumberFormat) {
		this.serialNumberFormat = serialNumberFormat;
	}
	public boolean isUsingJobSites() {
		return usingJobSites;
	}
	public void setUsingJobSites(boolean usingJobSites) {
		this.usingJobSites = usingJobSites;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isUsingSerialNumber() {
		return usingSerialNumber;
	}
	public void setUsingSerialNumber(boolean usingSerialNumber) {
		this.usingSerialNumber = usingSerialNumber;
	}
	public boolean isUsingJobs() {
		return usingJobs;
	}
	public void setUsingJobs(boolean usingJobs) {
		this.usingJobs = usingJobs;
	}
	public boolean isUsingIntegration() {
		return usingIntegration;
	}
	public void setUsingIntegration(boolean usingIntegration) {
		this.usingIntegration = usingIntegration;
	}
	
	public void setUsingAssignedTo(boolean usingAssignedTo) {
		this.usingAssignedTo = usingAssignedTo;
	}
	public boolean isUsingAssignedTo() {
		return usingAssignedTo;
	}
}
