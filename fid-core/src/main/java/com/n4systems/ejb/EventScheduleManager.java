package com.n4systems.ejb;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;

import java.util.List;

public interface EventScheduleManager {
	
	public List<Event> autoSchedule(Asset asset);

    public Event reattach(Event event);
	public Event update(Event event);
	public void restoreScheduleForEvent(Event event);
	

	
	public void removeAllSchedulesFor(Asset asset);
	
	
	public List<Event> getAvailableSchedulesFor(Asset asset);
	
	public List<Event> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, EventType eventType);
	
	public List<Event> getAutoEventSchedules(Asset asset);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getAssetIdForSchedule(Long scheduleId);
	public Long getEventTypeIdForSchedule(Long scheduleId);
	
	public Long getEventIdForSchedule(Long scheduleId);
}
