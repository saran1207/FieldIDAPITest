package com.n4systems.services.reporting;

import java.io.Serializable;

import com.n4systems.model.Event;

public class EventScheduleStatusCount implements Serializable{
		
	public EventScheduleStatusCount(Event.WorkflowState state, Long count) {
		this.state = state;
		this.count = count;
	}
	
	public Event.WorkflowState state;
	public Long count;
}