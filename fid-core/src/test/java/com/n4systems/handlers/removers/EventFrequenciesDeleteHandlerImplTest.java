package com.n4systems.handlers.removers;
import static com.n4systems.model.builders.EventTypeBuilder.*;
import static org.easymock.EasyMock.*;

import com.n4systems.handlers.remover.EventFrequenciesDeleteHandlerImpl;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.inspectiontype.EventFrequencySaver;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.remover.EventFrequenciesDeleteHandler;
import com.n4systems.model.EventType;
import com.n4systems.model.assettype.EventFrequencyListLoader;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;


public class EventFrequenciesDeleteHandlerImplTest {

	private Transaction mockTransaction; 
	
	private void mockTransaction() {
		mockTransaction = createMock(FieldIdTransaction.class);
		mockTransaction.commit();
		expectLastCall().once();
		replay(mockTransaction);
	}
	
	@Before
	public void setup() {
		mockTransaction();
	}
	
	@Test
	public void should_remove_all_event_frequencies_for_event_type() {
		
		EventType eventTypeToBeRemoved = anEventType().build();
		AssetTypeSchedule scheduleToBeRemoved = new AssetTypeSchedule();
		
		EventFrequencyListLoader mockListLoader = createMock(EventFrequencyListLoader.class);
		expect(mockListLoader.setEventTypeId(eventTypeToBeRemoved.getId())).andReturn(mockListLoader);
		expect(mockListLoader.load(mockTransaction)).andReturn(new FluentArrayList<AssetTypeSchedule>().stickOn(scheduleToBeRemoved));
		replay(mockListLoader);
		
		EventFrequencySaver mockSaver = createMock(EventFrequencySaver.class);
		mockSaver.remove(mockTransaction, scheduleToBeRemoved);
		replay(mockSaver);
		
		EventFrequenciesDeleteHandler sut = new EventFrequenciesDeleteHandlerImpl(mockListLoader, mockSaver);
		
		sut.forEventType(eventTypeToBeRemoved).remove(mockTransaction);
		
		verify(mockListLoader);
		verify(mockSaver);
	}
	
	
	
	
	
	
	
	
	
}
