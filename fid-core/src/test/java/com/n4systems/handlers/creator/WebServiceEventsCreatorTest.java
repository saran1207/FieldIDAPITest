package com.n4systems.handlers.creator;

import static com.n4systems.model.builders.EventBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.services.NextEventScheduleSerivce;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.test.helpers.FluentArrayList;


public class WebServiceEventsCreatorTest {
	private final class CreateEventsMethodObjectSabatour implements CreateEventsMethodObject {
		@Override
		public List<Event> createEvents(String transactionGUID, List<Event> events, Map<Event, Date> nextEventDates) throws TransactionAlreadyProcessedException, ProcessingProofTestException,
				FileAttachmentException, UnknownSubAsset {
					return createEvents(transactionGUID, events);
				}

		@Override
		public List<Event> createEvents(String transactionGUID, List<Event> events) throws TransactionAlreadyProcessedException, ProcessingProofTestException,
				FileAttachmentException, UnknownSubAsset {
			throw new TransactionAlreadyProcessedException();
		}
	}


	private TestDoubleTransactionManager transactionManager  = new TestDoubleTransactionManager();
	private NullObjectDefaultedEventPersistenceFactory eventPersistenceFactory = new NullObjectDefaultedEventPersistenceFactory();

	
	private String transactionGUID = "301001-aas-df-as-dl2kajd";
	private List<Event> events = new FluentArrayList<Event>(anEvent().build(), anEvent().build());
	private Map<Event, Date> nextEventDates = ImmutableMap.of(events.get(0), new Date(), events.get(1), new Date());
	
	
	@Test
	public void should_use_basic_transaction_management() throws Exception {
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		assertThat(sut, instanceOf(BasicTransactionManagement.class));
	}
	
	
	@Test
	public void should_create_dependancies_from_the_factory_with_the_created_transaction() throws Exception {
		Transaction transaction = transactionManager.getTransaction();
		
		EventPersistenceFactory eventPersistenceFactory = createMock(EventPersistenceFactory.class);
		expect(eventPersistenceFactory.createCreateEventsMethodObject(transaction)).andReturn(new NullCreateEventsMethodObject());
		expect(eventPersistenceFactory.createNextEventScheduleService(transaction)).andReturn(new NullNextEventScheduleSerivce());
		expect(eventPersistenceFactory.createCreateEventAuditLogger()).andReturn(new NullAuditLogger());
		replay(eventPersistenceFactory);
		
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(transactionGUID, events, nextEventDates);
		
		verify(eventPersistenceFactory);
	}
	

	
	@Test(expected=TransactionAlreadyProcessedException.class)
	public void should_not_wrap_an_exception_thrown_by_the_create_events_method() throws Exception {
		eventPersistenceFactory.createEventsMethodObject = new CreateEventsMethodObjectSabatour();
		
		
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(transactionGUID, events, nextEventDates);
		
	}	
	
	
	@Test
	public void should_call_the_with_the_events_sent_in_CreateEventsMethodObject() throws Exception {
		
		CreateEventsMethodObject mockCreateEventsMethod = createMock(CreateEventsMethodObject.class);
		expect(mockCreateEventsMethod.createEvents(transactionGUID, events)).andReturn(events);
		replay(mockCreateEventsMethod);
		
		eventPersistenceFactory.createEventsMethodObject = mockCreateEventsMethod;
		
		
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(transactionGUID, events, nextEventDates);
		
		verify(mockCreateEventsMethod);
	}
	
	
	
	@Test
	public void should_return_the_events_returned_from_CreateEventsMethodObject() throws Exception {
		List<Event> savedEvents = new FluentArrayList<Event>(anEvent().build(), anEvent().build());
		
		CreateEventsMethodObject mockCreateEventsMethod = createMock(CreateEventsMethodObject.class);
		expect(mockCreateEventsMethod.createEvents(transactionGUID, events)).andReturn(savedEvents);
		replay(mockCreateEventsMethod);
		
		eventPersistenceFactory.createEventsMethodObject = mockCreateEventsMethod;
		
		
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		List<Event> actualSavedEvents = sut.create(transactionGUID, events, nextEventDates);
		
		Assert.assertThat(actualSavedEvents, equalTo(savedEvents));
	}
	
	
	
	@Test
	public void should_send_a_success_audit_for_each_event_saved_after_everything_has_been_saved() throws Exception {
		
		AuditLogger auditLogger = createMock(AuditLogger.class);
		for (Event event : events) {
			auditLogger.audit((String)anyObject(), same(event), (Throwable)isNull());
		}
		
		replay(auditLogger);
		
		eventPersistenceFactory.auditLogger = auditLogger;

		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(transactionGUID, events, nextEventDates);
				
		verify(auditLogger);
	}
	
	
	@Test
	public void should_send_a_failure_audit_for_each_event_after_everything_has_failed() throws Exception {
		
		AuditLogger auditLogger = createMock(AuditLogger.class);
		for (Event event : events) {
			auditLogger.audit(isA(String.class), same(event), isA(TransactionAlreadyProcessedException.class));
		}
		replay(auditLogger);
		
		
		eventPersistenceFactory.createEventsMethodObject = new CreateEventsMethodObjectSabatour();
		eventPersistenceFactory.auditLogger = auditLogger;
		
		
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		try {
			sut.create(transactionGUID, events, nextEventDates);
		} catch(Exception e) {}
				
		verify(auditLogger);
	}
	
	
	@Test
	public void should_create_all_schedules_for_the_next_event_date_map() throws Exception {
		
		NextEventScheduleSerivce nextEventScheduleSerivce = createMock(NextEventScheduleSerivce.class);
		expect(nextEventScheduleSerivce.createNextSchedule(isA(Event.class)))
			.andAnswer(new IAnswer<Event>() {	@Override public Event answer() throws Throwable { return (Event)getCurrentArguments()[0]; } })
			.times(nextEventDates.size());
		replay(nextEventScheduleSerivce);
		
		eventPersistenceFactory.nextEventScheduleSerivce = nextEventScheduleSerivce;
		
		
		WebServiceEventsCreator sut = new WebServiceEventsCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(transactionGUID, events, nextEventDates);
		
		verify(nextEventScheduleSerivce);
	}
	
}
