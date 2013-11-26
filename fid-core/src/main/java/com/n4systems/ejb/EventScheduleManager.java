package com.n4systems.ejb;

import com.n4systems.model.*;

import java.util.List;

public interface EventScheduleManager {
	
	public List<ThingEvent> autoSchedule(Asset asset);

    public ThingEvent reattach(ThingEvent event);
	public ThingEvent update(ThingEvent event);
	public void restoreScheduleForEvent(ThingEvent event);
	
	public void removeAllSchedulesFor(Asset asset);
	
	public List<ThingEvent> getAvailableSchedulesFor(Asset asset, String... postFetchFields);
	
	public List<ThingEvent> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, ThingEventType eventType);
	
	public List<ThingEvent> getAutoEventSchedules(Asset asset);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getAssetIdForSchedule(Long scheduleId);
	public Long getEventTypeIdForSchedule(Long scheduleId);
	
	public Long getEventIdForSchedule(Long scheduleId);
}
