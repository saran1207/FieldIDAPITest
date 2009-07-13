package com.n4systems.webservice.dto;

import java.util.List;

public class WSJobSearchCriteria {

	private List<Long> jobIds;
	private String createDate;

	public List<Long> getJobIds() {
		return jobIds;
	}
	public void setJobIds(List<Long> jobIds) {
		this.jobIds = jobIds;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
