package com.n4systems.fieldid.actions.helpers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.util.DateHelper;


public class InspectionScheduleSuggestionTest {
	
	@Test public void should_suggested_schedule_with_no_schedulesid() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(InspectionScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_1_schedule_inside_30_days() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		schedules.add(createSchedule(DateHelper.getTomorrow(), 1L));
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_2_schedule_inside_30_days() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		schedules.add(createSchedule(DateHelper.getTomorrow(), 1L));
		schedules.add(createSchedule(DateHelper.getToday(), 2L));
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_yesterday_vs_tomorrow() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		schedules.add(createSchedule(DateHelper.getTomorrow(), 1L));
		schedules.add(createSchedule(DateHelper.getYesterday(), 2L));
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggests_no_schedule_when_schedules_are_more_than_30_days_away() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		schedules.add(createSchedule(DateHelper.addDaysToDate(DateHelper.getToday(),32L), 1L));
		schedules.add(createSchedule(DateHelper.addDaysToDate(DateHelper.getToday(),-41L), 2L));
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(InspectionScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_yesterday() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		schedules.add(createSchedule(DateHelper.getToday(), 1L));
		schedules.add(createSchedule(DateHelper.getYesterday(), 2L));
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_tomorrow() {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();
		schedules.add(createSchedule(DateHelper.getToday(), 1L));
		schedules.add(createSchedule(DateHelper.getTomorrow(), 2L));
		InspectionScheduleSuggestion suggestion = new InspectionScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	private InspectionSchedule createSchedule(Date nextDate, Long id) {
		InspectionSchedule schedule = new InspectionSchedule();
		schedule.setId(id);
		schedule.setNextDate(nextDate);
		return schedule;
	}
	
	
}
