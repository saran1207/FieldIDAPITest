package com.n4systems.fieldid.actions.helpers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.n4systems.model.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.EventSchedule;
import com.n4systems.util.DateHelper;


public class EventScheduleSuggestionTest {
	private TimeZone originalZone;
	
	@Before
	public void setDefaultTimeZoneUTC() {
		originalZone = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@After
	public void resetDefaultTimeZone() {
		TimeZone.setDefault(originalZone);
	}
	
	@Test public void should_suggested_schedule_with_no_schedulesid() {
		List<Event> schedules = new ArrayList<Event>();
		
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(EventScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_1_schedule_inside_30_days() {
		List<Event> schedules = new ArrayList<Event>();
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 1L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_2_schedule_inside_30_days() {
		List<Event> schedules = new ArrayList<Event>();
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 1L));
		schedules.add(createOpenEvent(DateHelper.getToday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_yesterday_vs_tomorrow() {
		List<Event> schedules = new ArrayList<Event>();
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 1L));
		schedules.add(createOpenEvent(DateHelper.getYesterday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggests_no_schedule_when_schedules_are_more_than_30_days_away() {
		List<Event> schedules = new ArrayList<Event>();
		schedules.add(createOpenEvent(DateHelper.addDaysToDate(DateHelper.getToday(), 32L), 1L));
		schedules.add(createOpenEvent(DateHelper.addDaysToDate(DateHelper.getToday(), -41L), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(EventScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_yesterday() {
		List<Event> schedules = new ArrayList<Event>();
        Date today = DateHelper.getToday();
        Date yesterday = DateHelper.getYesterday();
        schedules.add(createOpenEvent(today, 1L));
        schedules.add(createOpenEvent(yesterday, 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_tomorrow() {
		List<Event> schedules = new ArrayList<Event>();
		schedules.add(createOpenEvent(DateHelper.getToday(), 1L));
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	private Event createOpenEvent(Date nextDate, Long id) {
        Event event = new Event();
        event.setId(id);
        event.setNextDate(nextDate);
		return event;
	}
	
}
