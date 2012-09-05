package com.n4systems.services;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Event;
import org.junit.Ignore;
import org.junit.Test;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.EventBuilder.anOpenEvent;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class EventScheduleServiceTest {

	@Test @Ignore
    public void should_save_schedule_and_update_asset() {
		Event openEvent = anOpenEvent().on(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.save(openEvent)).andReturn(3L);
		expect(mockPersistenceManager.update(openEvent.getAsset())).andReturn(openEvent.getAsset());
		replay(mockPersistenceManager);
		
		EventScheduleService sut = new EventScheduleServiceImpl(mockPersistenceManager);
		
		assertEquals(new Long(3), sut.createSchedule(openEvent));
		verify(mockPersistenceManager);
	}
	
	
	@Test public void should_update_schedule_and_update_asset() {
		Event openEvent = anOpenEvent().on(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.update((Event)anyObject())).andReturn(openEvent);
		expect(mockPersistenceManager.update(openEvent.getAsset())).andReturn(openEvent.getAsset());
		replay(mockPersistenceManager);
		
		EventScheduleService sut = new EventScheduleServiceImpl(mockPersistenceManager);
		
		assertEquals(openEvent, sut.updateSchedule(openEvent));
		verify(mockPersistenceManager);
	}
}
