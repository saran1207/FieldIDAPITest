package com.n4systems.fieldid.selenium.datatypes;

public class SchedulesSearchCriteria extends AssetSearchCriteria {

	private String scheduleStatus;
	
	private String eventTypeGroup;
	
	private String job;

	public String getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(String scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public String getEventTypeGroup() {
		return eventTypeGroup;
	}

	public void setEventTypeGroup(String eventTypeGroup) {
		this.eventTypeGroup = eventTypeGroup;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	
}
