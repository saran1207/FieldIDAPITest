package com.n4systems.services;

import static com.n4systems.model.builders.EventScheduleBuilder.*;
import static com.n4systems.model.builders.EventTypeBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.model.EventType;
import com.n4systems.test.helpers.DateHelper;

public class NextInspectionScheduleServiceTest {

	private InspectionScheduleManager mockInspectionScheduleManager;
	
	@Before
	public void setUp() throws Exception {
		mockInspectionScheduleManager = createMock(InspectionScheduleManager.class);
	}
	
	@Test
	public void test_creates_schedule_when_one_doesnt_exist() {
		Asset asset = anAsset().build();
		EventType eventType = anEventType().build();
		Date nextDate = DateHelper.oneYearFromToday();
		EventSchedule schedule = aScheduledEventSchedule().asset(asset).eventType(eventType).nextDate(nextDate).build();
		List<EventSchedule> existingSchedules = new ArrayList<EventSchedule>();
		
		expect(mockInspectionScheduleManager.getAvailableSchedulesFor(asset)).andReturn(existingSchedules);
		expect(mockInspectionScheduleManager.update((EventSchedule)anyObject())).andReturn(schedule);
		replay(mockInspectionScheduleManager);
		
		ManagerBackedNextInspectionScheduleService scheduleService = new ManagerBackedNextInspectionScheduleService(mockInspectionScheduleManager);		
		EventSchedule returnedSchedule = scheduleService.createNextSchedule(new EventSchedule(asset, eventType, nextDate));
		
		verify(mockInspectionScheduleManager);
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
				
		expect(mockInspectionScheduleManager.getAvailableSchedulesFor(asset)).andReturn(existingSchedules);
		replay(mockInspectionScheduleManager);
		
		ManagerBackedNextInspectionScheduleService scheduleService = new ManagerBackedNextInspectionScheduleService(mockInspectionScheduleManager);
		EventSchedule returnedSchedule = scheduleService.createNextSchedule(new EventSchedule(asset, eventType, nextDateDifferentTime));
		
		verify(mockInspectionScheduleManager);
		assertEquals(schedule.getId(), returnedSchedule.getId());		
	}
}
