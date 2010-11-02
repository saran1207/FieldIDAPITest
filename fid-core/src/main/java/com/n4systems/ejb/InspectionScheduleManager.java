package com.n4systems.ejb;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;

public interface InspectionScheduleManager {
	
	public List<EventSchedule> autoSchedule(Asset asset);
	
	public EventSchedule update(EventSchedule schedule);
	public void restoreScheduleForInspection(Event event);
	

	
	public void removeAllSchedulesFor(Asset asset);
	
	
	
	public List<EventSchedule> getAvailableSchedulesFor(Asset asset);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getAssetIdForSchedule(Long scheduleId);
	public Long getEventTypeIdForSchedule(Long scheduleId);
	
	public Long getInspectionIdForSchedule(Long scheduleId);
}
