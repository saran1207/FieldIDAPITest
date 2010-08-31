package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class JobListResponse extends AbstractListResponse {

	private List<JobServiceDTO> jobs = new ArrayList<JobServiceDTO>();

	public List<JobServiceDTO> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobServiceDTO> jobs) {
		this.jobs = jobs;
	}
}
