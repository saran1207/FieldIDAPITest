package com.n4systems.webservice.dto;

public class JobSiteServiceDTO extends AbstractBaseServiceDTO {
	
	private String name;
	private long customerId;
	private long divisionId;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	
}
