package com.n4systems.fieldid.actions.helpers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.EventSchedule;
import org.junit.Test;

import com.n4systems.util.DateHelper;


public class EventScheduleSuggestionTest {
	
	@Test public void should_suggested_schedule_with_no_schedulesid() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(EventScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_1_schedule_inside_30_days() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		schedules.add(createSchedule(DateHelper.getTomorrow(), 1L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_2_schedule_inside_30_days() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		schedules.add(createSchedule(DateHelper.getTomorrow(), 1L));
		schedules.add(createSchedule(DateHelper.getToday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_yesterday_vs_tomorrow() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		schedules.add(createSchedule(DateHelper.getTomorrow(), 1L));
		schedules.add(createSchedule(DateHelper.getYesterday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggests_no_schedule_when_schedules_are_more_than_30_days_away() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		schedules.add(createSchedule(DateHelper.addDaysToDate(DateHelper.getToday(),32L), 1L));
		schedules.add(createSchedule(DateHelper.addDaysToDate(DateHelper.getToday(),-41L), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(EventScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_yesterday() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		schedules.add(createSchedule(DateHelper.getToday(), 1L));
		schedules.add(createSchedule(DateHelper.getYesterday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_tomorrow() {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		schedules.add(createSchedule(DateHelper.getToday(), 1L));
		schedules.add(createSchedule(DateHelper.getTomorrow(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	private EventSchedule createSchedule(Date nextDate, Long id) {
		EventSchedule schedule = new EventSchedule();
		schedule.setId(id);
		schedule.setNextDate(nextDate);
		return schedule;
	}
	
	
}
