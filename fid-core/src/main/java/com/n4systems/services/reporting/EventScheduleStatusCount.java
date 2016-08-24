package com.n4systems.services.reporting;

import com.n4systems.model.WorkflowState;

import java.io.Serializable;

public class EventScheduleStatusCount implements Serializable{
		
	public EventScheduleStatusCount(WorkflowState state, Long count) {
		this.state = state;
		this.count = count;
	}
	
	public WorkflowState state;
	public Long count;
}