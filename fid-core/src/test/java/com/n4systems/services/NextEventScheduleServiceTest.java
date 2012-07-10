package com.n4systems.services;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.test.helpers.DateHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.EventBuilder.anOpenEvent;
import static com.n4systems.model.builders.EventScheduleBuilder.aScheduledEventSchedule;
import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

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
		Event openEvent = anOpenEvent().on(asset).ofType(eventType).scheduledFor(nextDate).build();
		List<Event> existingSchedules = new ArrayList<Event>();
		
		expect(mockEventScheduleManager.getAvailableSchedulesFor(asset)).andReturn(existingSchedules);
		expect(mockEventScheduleManager.update((Event)anyObject())).andReturn(openEvent);
		replay(mockEventScheduleManager);
		
		ManagerBackedNextEventScheduleService scheduleService = new ManagerBackedNextEventScheduleService(mockEventScheduleManager);
		Event returnedSchedule = scheduleService.createNextSchedule(createOpenEvent(asset,eventType, nextDate));
		
		verify(mockEventScheduleManager);
		assertEquals(openEvent.getId(), returnedSchedule.getId());
	}
	
	@Test
	public void test_returns_existing_schedule_if_one_exists_on_same_day_ignoring_time() {
		Asset asset = anAsset().build();
		EventType eventType = anEventType().build();
		Date nextDate = DateHelper.oneYearFromToday();
		Date nextDateDifferentTime = new Date(nextDate.getTime() + 1);
		Event openEvent = anOpenEvent().on(asset).ofType(eventType).scheduledFor(nextDate).build();
		List<Event> existingSchedules = new ArrayList<Event>();
		existingSchedules.add(openEvent);
				
		expect(mockEventScheduleManager.getAvailableSchedulesFor(asset)).andReturn(existingSchedules);
		replay(mockEventScheduleManager);
		
		ManagerBackedNextEventScheduleService scheduleService = new ManagerBackedNextEventScheduleService(mockEventScheduleManager);
		Event returnedSchedule = scheduleService.createNextSchedule(createOpenEvent(asset, eventType, nextDate));
		
		verify(mockEventScheduleManager);
		assertEquals(openEvent.getId(), returnedSchedule.getId());
	}

    private Event createOpenEvent(Asset asset, EventType eventType, Date nextDate) {
        Event event = new Event();
        event.setAsset(asset);
        event.setType(eventType);
        event.setNextDate(nextDate);
        return event;
    }
}
