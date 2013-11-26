package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventParameter;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.handlers.creator.events.EventCreator;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextEventScheduleSerivce;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;


public class EventCreatorTest {

	private TransactionManager transactionManager  = new TestDoubleTransactionManager();
	private NullObjectDefaultedEventPersistenceFactory eventPersistenceFactory = new NullObjectDefaultedEventPersistenceFactory();

	private final class EventSaverSaboteur implements EventSaver {
		
		@Override public ThingEvent createEvent(CreateEventParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
			throw new ProcessingProofTestException();
		}

		public ThingEvent attachFilesToSubEvent(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
			return null;
		}
	}

	@Test
	public void should_use_basic_transaction_management() throws Exception {
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		Assert.assertThat(sut, instanceOf(BasicTransactionManagement.class));
	}
	
	@Test
	public void should_create_dependancies_from_the_factory_with_the_entity_manager_from_the_created_transaction() throws Exception {
		Transaction transaction = transactionManager.startTransaction();
		EventPersistenceFactory eventPersistenceFactory = createMock(EventPersistenceFactory.class);
		
		// dependency creation.
		expect(eventPersistenceFactory.createEventSaver(transaction)).andReturn(new NullEventSaver());
		expect(eventPersistenceFactory.createNextEventScheduleService(transaction)).andReturn(new NullNextEventScheduleSerivce());
		expect(eventPersistenceFactory.createCreateEventAuditLogger()).andReturn(new NullAuditLogger());
		
		replay(eventPersistenceFactory);

		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(new CreateEventParameterBuilder(anEvent().build(), 1L).build());
		
		verify(eventPersistenceFactory);
	}
	
	@Test
	public void should_have_saver_perform_event_save_with_the_passed_in_parameter() throws Exception {
		CreateEventParameter parameter = new CreateEventParameterBuilder(anEvent().build(), 1L).build();
		
		final EventSaver eventSaver = createMock(EventSaver.class);
		expect(eventSaver.createEvent(parameter)).andReturn(parameter.event);
		replay(eventSaver);
		
		eventPersistenceFactory.eventSaver = eventSaver;
		
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(parameter);
		
		verify(eventSaver);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_throw_a_wrapping_exception_when_saver_throws_an_exception() throws Exception {
		CreateEventParameter parameter = new CreateEventParameterBuilder(anEvent().build(), 1L).build();
		
		eventPersistenceFactory.eventSaver = new EventSaverSaboteur();
		
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(parameter);
	}
	
	@Test
	public void should_call_the_audit_with_the_saved_event_on_success() throws Exception {
		CreateEventParameter parameter = new CreateEventParameterBuilder(anEvent().build(), 1L).build();
		AuditLogger auditLogger = createMock(AuditLogger.class);
		auditLogger.audit((String)anyObject(), same(parameter.event), (Throwable)isNull());
		replay(auditLogger);
		
		eventPersistenceFactory.auditLogger = auditLogger;
		
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(parameter);
		
		verify(auditLogger);
	}
	
	@Test
	public void should_call_the_audit_with_the_unsaved_event_and_the_failure_exception_failure() throws Exception {
		CreateEventParameter parameter = new CreateEventParameterBuilder(anEvent().build(), 1L).build();
		AuditLogger auditLogger = createMock(AuditLogger.class);
		auditLogger.audit(isA(String.class), same(parameter.event), isA(ProcessingProofTestException.class));
		replay(auditLogger);
		
		eventPersistenceFactory.auditLogger = auditLogger;
		eventPersistenceFactory.eventSaver = new EventSaverSaboteur();
		
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		try {
			sut.create(parameter);
		} catch (Exception e) {}
		
		verify(auditLogger);
	}
	
	@Test
	public void should_return_the_saved_event_on_success() throws Exception {
		final ThingEvent savedEvent = anEvent().build();
		
		eventPersistenceFactory.eventSaver = new EventSaver() {
			public ThingEvent createEvent(CreateEventParameter parameterObject) throws ProcessingProofTestException ,FileAttachmentException , UnknownSubAsset {
				return savedEvent;
			}

			@Override
			public ThingEvent attachFilesToSubEvent(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
				return null;
			}
		};
		
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		ThingEvent actualReturnedEvent = sut.create(new CreateEventParameterBuilder(anEvent().build(), 1L).build());
		
		assertThat(actualReturnedEvent, sameInstance(savedEvent));
	}
	
	@Test
	public void should_save_the_set_event_schedules_given() throws Exception {
        ThingEvent event = anEvent().build();
		CreateEventParameter parameter = new CreateEventParameterBuilder(event, 1L)
			.addSchedule(new EventScheduleBundle(event.getAsset(), EventTypeBuilder.anEventType().build(), null, new Date()))
			.addSchedule(new EventScheduleBundle(event.getAsset(), EventTypeBuilder.anEventType().build(), null, new Date(2000)))
			.build();
		
		NextEventScheduleSerivce nextScheduleService = createMock(NextEventScheduleSerivce.class);
		expect(nextScheduleService.createNextSchedule(isA(ThingEvent.class))).andReturn(null).times(2);
		replay(nextScheduleService);
		
		eventPersistenceFactory.nextEventScheduleSerivce = nextScheduleService;
		
		EventCreator sut = new EventCreator(transactionManager, eventPersistenceFactory);
		
		sut.create(parameter);
		
		verify(nextScheduleService);
	}
	
}
