package com.n4systems.fieldid.actions.helpers;

import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.util.DateHelper;

import java.util.List;

public class EventScheduleSuggestion {
	private static final Long SUGGESTED_SCHEDULE_DATE_LIMIT = 31L;
	public static final Long NO_SCHEDULE = 0L;
	public static final Long NEW_SCHEDULE = -1L;
	
	List<ThingEvent> schedules;

	public EventScheduleSuggestion(List<ThingEvent> schedules) {
		this.schedules = schedules;
	}
	
	public Long getSuggestedScheduleId() {
		Event suggestedSchedule = selectAScheduleWithinATimeFrame();
		
		return (suggestedSchedule != null) ? suggestedSchedule.getId() : NO_SCHEDULE;
	}
	
	public Event getSuggestedSchedule() {
		return selectAScheduleWithinATimeFrame();
	}
	
	public Event selectAScheduleWithinATimeFrame() {
		Event suggestedSchedule = null;
		Long currentDaysFromToday = SUGGESTED_SCHEDULE_DATE_LIMIT;
		for (Event openEvent : schedules) {
			Long currentDays = Math.abs(DateHelper.getDaysFromToday(openEvent.getDueDate()));
			if (currentDays < SUGGESTED_SCHEDULE_DATE_LIMIT) { 
				if ((currentDays < currentDaysFromToday) || 
						(currentDays.equals(currentDaysFromToday) && openEvent.getDueDate().before(suggestedSchedule.getDueDate()))){
					suggestedSchedule = openEvent;
					currentDaysFromToday = currentDays;
				} 
			}
		}
		return suggestedSchedule;
	}
}
