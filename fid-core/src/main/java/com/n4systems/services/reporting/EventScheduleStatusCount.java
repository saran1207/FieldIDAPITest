package com.n4systems.services.reporting;

import java.io.Serializable;

import com.n4systems.model.EventSchedule.ScheduleStatus;

public class EventScheduleStatusCount implements Serializable{
		
	public EventScheduleStatusCount(ScheduleStatus status, Long count) {
		this.status = status;
		this.count = count;
	}
	
	public ScheduleStatus status;
	public Long count;
}