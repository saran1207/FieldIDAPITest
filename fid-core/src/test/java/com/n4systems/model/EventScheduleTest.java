package com.n4systems.model;

import com.n4systems.model.EventSchedule.ScheduleStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.EventScheduleBuilder.aScheduledEventSchedule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EventScheduleTest {

	
	private EventSchedule schedule;
	private Event event;

	@Before public void setUp() throws Exception {
		Asset asset = anAsset().build();
		event = new Event();
        event.setDate(new Date());
		event.setId(1L);
		event.setAsset(asset);
		
		schedule = aScheduledEventSchedule().asset(asset).build();
	}

	@After public void tearDown() throws Exception {
	}

	
	@Test public void test_completed_event() {
		schedule.completed(event);
		assertEquals(event, schedule.getEvent());
        assertEquals(event.getDate(), schedule.getCompletedDate());
	}

	@Test public void test_remove_event() {
		test_completed_event();
		
		schedule.removeEvent();
		assertNull(schedule.getCompletedDate());
		assertEquals(ScheduleStatus.SCHEDULED, schedule.getStatus());
		
	}
}
