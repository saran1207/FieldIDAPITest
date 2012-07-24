/**
 * N4 systems copyright
 */
package com.n4systems.model.utils;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.api.DisplayEnum;

public enum CompressedScheduleStatus implements DisplayEnum {
	INCOMPLETE("Incomplete", Arrays.asList(Event.EventState.OPEN), ScheduleStatus.SCHEDULED, ScheduleStatus.IN_PROGRESS),
	COMPLETE("Complete", Arrays.asList(Event.EventState.COMPLETED), ScheduleStatus.COMPLETED),
	ALL("All", Arrays.asList(Event.EventState.OPEN, Event.EventState.COMPLETED), ScheduleStatus.values());
	
	private String label;
	private List<ScheduleStatus> statuses;
    private List<Event.EventState> eventStates;
	
	private CompressedScheduleStatus(String label, List<Event.EventState> eventStates, ScheduleStatus...scheduleStatus) {
		this.label = label;
        this.eventStates = eventStates;
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

    public List<Event.EventState> getEventStates() {
        return eventStates;
    }

}