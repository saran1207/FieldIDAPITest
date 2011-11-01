package com.n4systems.services.reporting;

import java.io.Serializable;

import com.n4systems.model.orgs.BaseOrg;

public class EventKpiRecord implements Serializable {
	
	private Long completed = 0L;
	
	private Long inProgress = 0L;
	
	private Long scheduled = 0L;
	
	private Long failed = 0L;
	
	private BaseOrg customer;

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(Long completed) {
		this.completed = completed;
	}

	public Long getInProgress() {
		return inProgress;
	}

	public void setInProgress(Long inProgress) {
		this.inProgress = inProgress;
	}

	public Long getScheduled() {
		return scheduled;
	}

	public void setScheduled(Long scheduled) {
		this.scheduled = scheduled;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public BaseOrg getCustomer() {
		return customer;
	}

	public void setCustomer(BaseOrg customer) {
		this.customer = customer;
	}

	public Long getTotalScheduledEvents() {
		return completed + inProgress + scheduled;
	}
}
