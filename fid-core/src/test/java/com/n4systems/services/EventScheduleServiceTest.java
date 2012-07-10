package com.n4systems.services;

import static com.n4systems.model.builders.EventBuilder.anOpenEvent;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static com.n4systems.model.builders.EventScheduleBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;

import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.ejb.PersistenceManager;

public class EventScheduleServiceTest {

	@Test @Ignore
    public void should_save_schedule_and_update_asset() {
		Event openEvent = anOpenEvent().on(anAsset().build()).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.save(openEvent)).andReturn(3L);
		expect(mockPersistenceManager.update(openEvent.getAsset())).andReturn(openEvent.getAsset());
        expect(mockPersistenceManager.save(new EventGroup())).andReturn(7L);
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
