package com.n4systems.fieldid.actions.helpers;

import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.util.DateHelper;

public class EventScheduleSuggestion {
	private static final Long SUGGESTED_SCHEDULE_DATE_LIMIT = 31L;
	public static final Long NO_SCHEDULE = 0L;
	public static final Long NEW_SCHEDULE = -1L;
	
	List<Event> schedules;

	public EventScheduleSuggestion(List<Event> schedules) {
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
			Long currentDays = Math.abs(DateHelper.getDaysFromToday(openEvent.getNextDate()));
			if (currentDays < SUGGESTED_SCHEDULE_DATE_LIMIT) { 
				if ((currentDays < currentDaysFromToday) || 
						(currentDays.equals(currentDaysFromToday) && openEvent.getNextDate().before(suggestedSchedule.getNextDate()))){
					suggestedSchedule = openEvent;
					currentDaysFromToday = currentDays;
				} 
			}
		}
		return suggestedSchedule;
	}
}
