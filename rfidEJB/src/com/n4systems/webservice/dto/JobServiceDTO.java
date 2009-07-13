package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class JobServiceDTO extends AbstractBaseServiceDTO {

	private String projectId;
	private String name;
	private long customerId;
	private long divisionId;
	private long jobSiteId;
	private List<Long> resourceUserIds = new ArrayList<Long>();

	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
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
	public long getJobSiteId() {
		return jobSiteId;
	}
	public void setJobSiteId(long jobSiteId) {
		this.jobSiteId = jobSiteId;
	}
	public List<Long> getResourceUserIds() {
		return resourceUserIds;
	}
	public void setResourceUserIds(List<Long> resourceUserIds) {
		this.resourceUserIds = resourceUserIds;
	}
}
