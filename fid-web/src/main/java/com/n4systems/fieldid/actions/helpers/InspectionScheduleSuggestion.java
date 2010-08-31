package com.n4systems.fieldid.actions.helpers;

import java.util.List;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.util.DateHelper;

public class InspectionScheduleSuggestion {
	private static final Long SUGGESTED_SCHEDULE_DATE_LIMIT = 31L;
	public static final Long NO_SCHEDULE = 0L;
	public static final Long NEW_SCHEDULE = -1L;
	
	List<InspectionSchedule> schedules;

	public InspectionScheduleSuggestion(List<InspectionSchedule> schedules) {
		super();
		this.schedules = schedules;
	}
	
	public Long getSuggestedScheduleId() {
		
		InspectionSchedule suggestedSchedule = null;
		Long currentDaysFromToday = SUGGESTED_SCHEDULE_DATE_LIMIT;
		for (InspectionSchedule schedule : schedules) {
			Long currentDays = Math.abs(DateHelper.getDaysFromToday(schedule.getNextDate()));
			if (currentDays < SUGGESTED_SCHEDULE_DATE_LIMIT) { 
				if ((currentDays < currentDaysFromToday) || 
						(currentDays == currentDaysFromToday && schedule.getNextDate().before(suggestedSchedule.getNextDate()))){
					suggestedSchedule = schedule;
					currentDaysFromToday = currentDays;
				} 
			}
		}
		
		return (suggestedSchedule != null) ? suggestedSchedule.getId() : NO_SCHEDULE;
	}
	
	
	
}
