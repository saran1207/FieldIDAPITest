package com.n4systems.services;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static com.n4systems.model.builders.EventScheduleBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;

import com.n4systems.model.EventSchedule;
import org.junit.Test;

import com.n4systems.ejb.PersistenceManager;

public class InspectionScheduleServiceTest {


	@Test public void should_save_schedule_and_update_asset() {
		EventSchedule schedule = aScheduledEventSchedule().asset(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.save(schedule)).andReturn(3L);
		expect(mockPersistenceManager.update(schedule.getAsset())).andReturn(schedule.getAsset());
		replay(mockPersistenceManager);
		
		InspectionScheduleService sut = new InspectionScheduleServiceImpl(mockPersistenceManager);
		
		assertEquals(new Long(3), sut.createSchedule(schedule));
		verify(mockPersistenceManager);
	}
	
	
	@Test public void should_update_schedule_and_update_asset() {
		EventSchedule schedule = aScheduledEventSchedule().asset(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.update((EventSchedule)anyObject())).andReturn(schedule);
		expect(mockPersistenceManager.update(schedule.getAsset())).andReturn(schedule.getAsset());
		replay(mockPersistenceManager);
		
		InspectionScheduleService sut = new InspectionScheduleServiceImpl(mockPersistenceManager);
		
		assertEquals(schedule, sut.updateSchedule(schedule));
		verify(mockPersistenceManager);
	}
}
