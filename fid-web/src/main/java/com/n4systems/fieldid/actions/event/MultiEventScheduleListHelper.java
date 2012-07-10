package com.n4systems.fieldid.actions.event;

import java.util.List;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.fieldid.actions.helpers.EventScheduleSuggestion;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;

public class MultiEventScheduleListHelper {

	private EventScheduleManager eventScheduleManager;
	private List<Event> eventSchedules;
	private EventType eventType;
	
	public MultiEventScheduleListHelper(EventScheduleManager eventScheduleManager, EventType eventType) {
		this.eventScheduleManager = eventScheduleManager;
		this.eventType = eventType;
	}

	public List<Event> getEventSchedulesForAsset(Asset asset) {
		if (eventSchedules == null) {
			eventSchedules = eventScheduleManager.getAvailableSchedulesForAssetFilteredByEventType(asset, eventType);
		}
		return eventSchedules;
	}
	
	public Long getSuggestedEventScheduleIdForAsset(Asset asset){
		List<Event> eventSchedules = getEventSchedulesForAsset(asset);
		
		if (eventSchedules.isEmpty()){
			//This enables the schedule select box to have the default "Not Scheduled option".
			return 0L;
		}
		
		Event suggestedSchedule = new EventScheduleSuggestion(eventSchedules).getSuggestedSchedule();
		
		return (suggestedSchedule == null) ? 0L : suggestedSchedule.getId();
	}
	
}
