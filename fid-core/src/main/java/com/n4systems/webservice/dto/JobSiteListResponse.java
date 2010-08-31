package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class JobSiteListResponse extends AbstractListResponse {
	
	private List<JobSiteServiceDTO> jobSites = new ArrayList<JobSiteServiceDTO>();

	public List<JobSiteServiceDTO> getJobSites() {
		return jobSites;
	}

	public void setJobSites(List<JobSiteServiceDTO> jobSites) {
		this.jobSites = jobSites;
	}
}
