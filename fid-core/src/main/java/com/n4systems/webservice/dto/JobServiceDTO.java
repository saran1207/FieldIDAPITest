package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class JobServiceDTO extends AbstractBaseDTOWithOwner {

	private String projectId;
	private String name;
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
	public List<Long> getResourceUserIds() {
		return resourceUserIds;
	}
	public void setResourceUserIds(List<Long> resourceUserIds) {
		this.resourceUserIds = resourceUserIds;
	}
}
