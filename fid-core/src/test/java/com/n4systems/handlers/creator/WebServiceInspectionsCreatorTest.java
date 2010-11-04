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
import com.n4systems.services.NextInspectionScheduleSerivce;
import com.n4systems.test.helpers.FluentArrayList;


public class WebServiceInspectionsCreatorTest {
	private final class CreateInspectionsMethodObjectSabatour implements CreateEventsMethodObject {
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
	private NullObjectDefaultedInspectionPersistenceFactory inspectionPersistenceFactory = new NullObjectDefaultedInspectionPersistenceFactory();

	
	private String transactionGUID = "301001-aas-df-as-dl2kajd";
	private List<Event> events = new FluentArrayList<Event>(anEvent().build(), anEvent().build());
	private Map<Event, Date> nextInspectionDates = ImmutableMap.of(events.get(0), new Date(), events.get(1), new Date());
	
	
	@Test
	public void should_use_basic_transaction_management() throws Exception {
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		assertThat(sut, instanceOf(BasicTransactionManagement.class));
	}
	
	
	@Test
	public void should_create_dependancies_from_the_factory_with_the_created_transaction() throws Exception {
		Transaction transaction = transactionManager.getTransaction();
		
		EventPersistenceFactory eventPersistenceFactory = createMock(EventPersistenceFactory.class);
		expect(eventPersistenceFactory.createCreateEventsMethodObject(transaction)).andReturn(new NullCreateEventsMethodObject());
		expect(eventPersistenceFactory.createNextEventScheduleService(transaction)).andReturn(new NullNextInspectionScheduleSerivce());
		expect(eventPersistenceFactory.createCreateEventAuditLogger()).andReturn(new NullAuditLogger());
		replay(eventPersistenceFactory);
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(transactionGUID, events, nextInspectionDates);
		
		verify(eventPersistenceFactory);
	}
	

	
	@Test(expected=TransactionAlreadyProcessedException.class)
	public void should_not_wrap_an_exception_thrown_by_the_create_inspections_method() throws Exception {
		inspectionPersistenceFactory.createEventsMethodObject = new CreateInspectionsMethodObjectSabatour();
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, events, nextInspectionDates);
		
	}	
	
	
	@Test
	public void should_call_the_with_the_inspections_sent_in_CreateInspectionsMethodObject() throws Exception {
		
		CreateEventsMethodObject mockCreateEventsMethod = createMock(CreateEventsMethodObject.class);
		expect(mockCreateEventsMethod.createEvents(transactionGUID, events)).andReturn(events);
		replay(mockCreateEventsMethod);
		
		inspectionPersistenceFactory.createEventsMethodObject = mockCreateEventsMethod;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, events, nextInspectionDates);
		
		verify(mockCreateEventsMethod);
	}
	
	
	
	@Test
	public void should_return_the_inspections_returned_from_CreateInspectionsMethodObject() throws Exception {
		List<Event> savedEvents = new FluentArrayList<Event>(anEvent().build(), anEvent().build());
		
		CreateEventsMethodObject mockCreateEventsMethod = createMock(CreateEventsMethodObject.class);
		expect(mockCreateEventsMethod.createEvents(transactionGUID, events)).andReturn(savedEvents);
		replay(mockCreateEventsMethod);
		
		inspectionPersistenceFactory.createEventsMethodObject = mockCreateEventsMethod;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		List<Event> actualSavedEvents = sut.create(transactionGUID, events, nextInspectionDates);
		
		Assert.assertThat(actualSavedEvents, equalTo(savedEvents));
	}
	
	
	
	@Test
	public void should_send_a_success_audit_for_each_inspection_saved_after_everything_has_been_saved() throws Exception {
		
		AuditLogger auditLogger = createMock(AuditLogger.class);
		for (Event event : events) {
			auditLogger.audit((String)anyObject(), same(event), (Throwable)isNull());
		}
		
		replay(auditLogger);
		
		inspectionPersistenceFactory.auditLogger = auditLogger;

		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, events, nextInspectionDates);
				
		verify(auditLogger);
	}
	
	
	@Test
	public void should_send_a_failure_audit_for_each_inspection_after_everything_has_failed() throws Exception {
		
		AuditLogger auditLogger = createMock(AuditLogger.class);
		for (Event event : events) {
			auditLogger.audit(isA(String.class), same(event), isA(TransactionAlreadyProcessedException.class));
		}
		replay(auditLogger);
		
		
		inspectionPersistenceFactory.createEventsMethodObject = new CreateInspectionsMethodObjectSabatour();
		inspectionPersistenceFactory.auditLogger = auditLogger;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		try {
			sut.create(transactionGUID, events, nextInspectionDates);
		} catch(Exception e) {}
				
		verify(auditLogger);
	}
	
	
	@Test
	public void should_create_all_schedules_for_the_next_inspeciton_date_map() throws Exception {
		
		NextInspectionScheduleSerivce nextInspectionScheduleSerivce = createMock(NextInspectionScheduleSerivce.class);
		expect(nextInspectionScheduleSerivce.createNextSchedule(isA(EventSchedule.class)))
			.andAnswer(new IAnswer<EventSchedule>() {	@Override public EventSchedule answer() throws Throwable { return (EventSchedule)getCurrentArguments()[0]; } })
			.times(nextInspectionDates.size());
		replay(nextInspectionScheduleSerivce);
		
		inspectionPersistenceFactory.nextInspectionScheduleSerivce = nextInspectionScheduleSerivce;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, events, nextInspectionDates);
		
		verify(nextInspectionScheduleSerivce);
	}
	
}
