package com.n4systems.fieldid.actions.event;

import java.util.List;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;

public class MultiEventScheduleListHelper {

	private EventScheduleManager eventScheduleManager;
	private List<EventSchedule> eventSchedules;
	
	public MultiEventScheduleListHelper(EventScheduleManager eventScheduleManager) {
		this.eventScheduleManager = eventScheduleManager;
	}

	public List<EventSchedule> getEventSchedulesForAsset(Asset asset) {
		if (eventSchedules == null) {
			eventSchedules = eventScheduleManager.getAvailableSchedulesFor(asset);
		}
		return eventSchedules;
	}
	
}
