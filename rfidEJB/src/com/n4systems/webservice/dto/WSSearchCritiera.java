package com.n4systems.webservice.dto;

import java.util.List;

public class WSSearchCritiera {

	private List<Long> customerIds;
	private List<Long> divisionIds;
	private List<Long> jobSiteIds;
	private String createDate;
	
	public List<Long> getCustomerIds() {
		return customerIds;
	}
	public void setCustomerIds(List<Long> customerIds) {
		this.customerIds = customerIds;
	}
	public List<Long> getDivisionIds() {
		return divisionIds;
	}
	public void setDivisionIds(List<Long> divisionIds) {
		this.divisionIds = divisionIds;
	}
	public List<Long> getJobSiteIds() {
		return jobSiteIds;
	}
	public void setJobSiteIds(List<Long> jobSiteIds) {
		this.jobSiteIds = jobSiteIds;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
