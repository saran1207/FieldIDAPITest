package com.n4systems.model.eventtype;

import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;
import org.junit.Test;

import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static org.easymock.EasyMock.*;



public class AggragateEventTypeCleanerTest {

    ThingEventType eventType = anEventType().build();
	
	@Test
	public void should_run_with_out_errors_when_no_cleaners_given() throws Exception {
		Cleaner<EventType> sut = new AggragateEventTypeCleaner();
		
		sut.clean(eventType);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_push_clean_call_to_supplied_event_type_cleaner() throws Exception {
		
		
		Cleaner<EventType> subCleaner = createMock(Cleaner.class);
		subCleaner.clean(eventType);
		replay(subCleaner);
		
		AggragateEventTypeCleaner sut = new AggragateEventTypeCleaner();
		sut.addCleaner(subCleaner);
		sut.clean(eventType);
		
		verify(subCleaner);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_push_clean_call_to_supplied_event_type_cleaners() throws Exception {
		Cleaner<EventType> subCleaner = createMock(Cleaner.class);
		subCleaner.clean(eventType);
		replay(subCleaner);
		
		Cleaner<EventType> subCleaner2 = createMock(Cleaner.class);
		subCleaner2.clean(eventType);
		replay(subCleaner2);
		
		AggragateEventTypeCleaner sut = new AggragateEventTypeCleaner();
		sut.addCleaner(subCleaner);
		sut.addCleaner(subCleaner2);
		
		sut.clean(eventType);
		
		verify(subCleaner);
		verify(subCleaner2);
	}
	
}
