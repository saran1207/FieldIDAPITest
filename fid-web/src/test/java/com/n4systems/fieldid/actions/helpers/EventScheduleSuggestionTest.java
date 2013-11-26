package com.n4systems.fieldid.actions.helpers;

import com.n4systems.model.ThingEvent;
import com.n4systems.util.DateHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;


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
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(EventScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_1_schedule_inside_30_days() {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 1L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_2_schedule_inside_30_days() {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 1L));
		schedules.add(createOpenEvent(DateHelper.getToday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_yesterday_vs_tomorrow() {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 1L));
		schedules.add(createOpenEvent(DateHelper.getYesterday(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(2), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggests_no_schedule_when_schedules_are_more_than_30_days_away() {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		schedules.add(createOpenEvent(DateHelper.addDaysToDate(DateHelper.getToday(), 32L), 1L));
		schedules.add(createOpenEvent(DateHelper.addDaysToDate(DateHelper.getToday(), -41L), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(EventScheduleSuggestion.NO_SCHEDULE, suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_yesterday() {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
        Date today = DateHelper.getToday();
        Date yesterday = DateHelper.getYesterday();
        schedules.add(createOpenEvent(today, 1L));
        schedules.add(createOpenEvent(yesterday, 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	@Test public void should_suggested_schedule_with_today_vs_tomorrow() {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		schedules.add(createOpenEvent(DateHelper.getToday(), 1L));
		schedules.add(createOpenEvent(DateHelper.getTomorrow(), 2L));
		EventScheduleSuggestion suggestion = new EventScheduleSuggestion(schedules);
		assertEquals(new Long(1), suggestion.getSuggestedScheduleId());
	}
	
	private ThingEvent createOpenEvent(Date nextDate, Long id) {
        ThingEvent event = new ThingEvent();
        event.setId(id);
        event.setDueDate(nextDate);
		return event;
	}
	
}
