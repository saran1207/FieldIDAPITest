package com.n4systems.fieldid.actions.event;

import java.util.List;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;

public class MultiEventScheduleListHelper {

	private EventScheduleManager eventScheduleManager;

	public MultiEventScheduleListHelper(EventScheduleManager eventScheduleManager) {
		this.eventScheduleManager = eventScheduleManager;
	}

	List<EventSchedule> eventSchedules;

	public List<EventSchedule> getEventSchedulesForAsset(Asset asset) {
		if (eventSchedules == null) {
			eventSchedules = eventScheduleManager.getAvailableSchedulesFor(asset);
		}
		return eventSchedules;
	}
	
	public EventSchedule getEventScheduleById(Long scheduleId){
		return eventScheduleManager.getEventScheduleById(scheduleId);
	}
	
}
