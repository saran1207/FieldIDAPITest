package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.fieldid.actions.helpers.EventScheduleSuggestion;
import com.n4systems.model.*;

import java.util.List;

public class MultiEventScheduleListHelper {

	private EventScheduleManager eventScheduleManager;
	private List<ThingEvent> eventSchedules;
	private ThingEventType eventType;
	
	public MultiEventScheduleListHelper(EventScheduleManager eventScheduleManager, ThingEventType eventType) {
		this.eventScheduleManager = eventScheduleManager;
		this.eventType = eventType;
	}

	public List<ThingEvent> getEventSchedulesForAsset(Asset asset) {
		if (eventSchedules == null) {
			eventSchedules = eventScheduleManager.getAvailableSchedulesForAssetFilteredByEventType(asset, eventType);
		}
		return eventSchedules;
	}
	
	public Long getSuggestedEventScheduleIdForAsset(Asset asset){
		List<ThingEvent> eventSchedules = getEventSchedulesForAsset(asset);
		
		if (eventSchedules.isEmpty()){
			//This enables the schedule select box to have the default "Not Scheduled option".
			return 0L;
		}
		
		Event suggestedSchedule = new EventScheduleSuggestion(eventSchedules).getSuggestedSchedule();
		
		return (suggestedSchedule == null) ? 0L : suggestedSchedule.getId();
	}
	
}
