package com.n4systems.model;

import static com.n4systems.model.builders.InspectionScheduleBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.EventSchedule.ScheduleStatus;
import static com.n4systems.test.helpers.Asserts.*;

public class InspectionScheduleTest {

	
	private EventSchedule schedule;
	private Event event;

	@Before public void setUp() throws Exception {
		Asset asset = anAsset().build();
		event = new Event();
		event.setId(1L);
		event.setAsset(asset);
		
		schedule = aScheduledInspectionSchedule().asset(asset).build();
	}

	@After public void tearDown() throws Exception {
	}

	
	@Test public void test_completed_inspection() {
		schedule.completed(event);
		assertEquals(event, schedule.getEvent());
		assertInRange(new Date(), schedule.getCompletedDate(), 1000L);
	}

	@Test public void test_remove_inspection() {
		test_completed_inspection();
		
		schedule.removeInspection();
		assertNull(schedule.getCompletedDate());
		assertNull(schedule.getEvent());
		assertEquals(ScheduleStatus.SCHEDULED, schedule.getStatus());
		
	}
}
