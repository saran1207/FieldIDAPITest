/**
 * N4 systems copyright
 */
package com.n4systems.model.utils;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.api.DisplayEnum;

public enum CompressedScheduleStatus implements DisplayEnum {
	INCOMPLETE("label.incomplete", ScheduleStatus.SCHEDULED, ScheduleStatus.IN_PROGRESS), 
	COMPLETE("label.complete", ScheduleStatus.COMPLETED), 
	ALL("label.all", ScheduleStatus.values());
	
	private String label;
	private List<ScheduleStatus> statuses;
	
	private CompressedScheduleStatus(String label, ScheduleStatus...scheduleStatus) {
		this.label = label;
		statuses = Arrays.asList(scheduleStatus);
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name();
	}

	public List<ScheduleStatus> getScheduleStatuses() {
		return statuses;
	}
}