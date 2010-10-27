package com.n4systems.services;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static com.n4systems.model.builders.InspectionScheduleBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import org.junit.Test;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionSchedule;

public class InspectionScheduleServiceTest {


	@Test public void should_save_schedule_and_update_asset() {
		InspectionSchedule schedule = aScheduledInspectionSchedule().asset(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.save(schedule)).andReturn(3L);
		expect(mockPersistenceManager.update(schedule.getAsset())).andReturn(schedule.getAsset());
		replay(mockPersistenceManager);
		
		InspectionScheduleService sut = new InspectionScheduleServiceImpl(mockPersistenceManager);
		
		assertEquals(new Long(3), sut.createSchedule(schedule));
		verify(mockPersistenceManager);
	}
	
	
	@Test public void should_update_schedule_and_update_asset() {
		InspectionSchedule schedule = aScheduledInspectionSchedule().asset(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.update((InspectionSchedule)anyObject())).andReturn(schedule);
		expect(mockPersistenceManager.update(schedule.getAsset())).andReturn(schedule.getAsset());
		replay(mockPersistenceManager);
		
		InspectionScheduleService sut = new InspectionScheduleServiceImpl(mockPersistenceManager);
		
		assertEquals(schedule, sut.updateSchedule(schedule));
		verify(mockPersistenceManager);
	}
}
