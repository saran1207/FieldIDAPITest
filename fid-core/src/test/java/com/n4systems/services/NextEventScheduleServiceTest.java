package com.n4systems.services;

import static com.n4systems.model.builders.EventScheduleBuilder.*;
import static com.n4systems.model.builders.EventTypeBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.EventType;
import com.n4systems.test.helpers.DateHelper;

public class NextEventScheduleServiceTest {

	private EventScheduleManager mockEventScheduleManager;
	
	@Before
	public void setUp() throws Exception {
		mockEventScheduleManager = createMock(EventScheduleManager.class);
	}
	
	@Test
	public void test_creates_schedule_when_one_doesnt_exist() {
		Asset asset = anAsset().build();
		EventType eventType = anEventType().build();
		Date nextDate = DateHelper.oneYearFromToday();
		EventSchedule schedule = aScheduledEventSchedule().asset(asset).eventType(eventType).nextDate(nextDate).build();
		List<EventSchedule> existingSchedules = new ArrayList<EventSchedule>();
		
		expect(mockEventScheduleManager.getAvailableSchedulesFor(asset)).andReturn(existingSchedules);
		expect(mockEventScheduleManager.update((EventSchedule)anyObject())).andReturn(schedule);
		replay(mockEventScheduleManager);
		
		ManagerBackedNextEventScheduleService scheduleService = new ManagerBackedNextEventScheduleService(mockEventScheduleManager);
		EventSchedule returnedSchedule = scheduleService.createNextSchedule(new EventSchedule(asset, eventType, nextDate));
		
		verify(mockEventScheduleManager);
		assertEquals(schedule.getId(), returnedSchedule.getId());
	}
	
	@Test
	public void test_returns_existing_schedule_if_one_exists_on_same_day_ignoring_time() {
		Asset asset = anAsset().build();
		EventType eventType = anEventType().build();
		Date nextDate = DateHelper.oneYearFromToday();
		Date nextDateDifferentTime = new Date(nextDate.getTime() + 1);
		EventSchedule schedule = aScheduledEventSchedule().asset(asset).eventType(eventType).nextDate(nextDate).build();
		List<EventSchedule> existingSchedules = new ArrayList<EventSchedule>();
		existingSchedules.add(schedule);
				
		expect(mockEventScheduleManager.getAvailableSchedulesFor(asset)).andReturn(existingSchedules);
		replay(mockEventScheduleManager);
		
		ManagerBackedNextEventScheduleService scheduleService = new ManagerBackedNextEventScheduleService(mockEventScheduleManager);
		EventSchedule returnedSchedule = scheduleService.createNextSchedule(new EventSchedule(asset, eventType, nextDateDifferentTime));
		
		verify(mockEventScheduleManager);
		assertEquals(schedule.getId(), returnedSchedule.getId());		
	}
}
