package com.n4systems.ejb;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;

public interface EventScheduleManager {
	
	public List<EventSchedule> autoSchedule(Asset asset);
	
	public EventSchedule update(EventSchedule schedule);
	public void restoreScheduleForEvent(Event event);
	

	
	public void removeAllSchedulesFor(Asset asset);
	
	
	public List<EventSchedule> getAvailableSchedulesFor(Asset asset);
	
	public List<EventSchedule> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, EventType eventType);
	
	public List<EventSchedule> getAutoEventSchedules(Asset asset);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getAssetIdForSchedule(Long scheduleId);
	public Long getEventTypeIdForSchedule(Long scheduleId);
	
	public Long getEventIdForSchedule(Long scheduleId);
}
