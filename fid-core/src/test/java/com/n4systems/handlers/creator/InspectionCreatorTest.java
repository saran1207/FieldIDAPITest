package com.n4systems.handlers.creator;

import static com.n4systems.model.builders.EventBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import org.junit.Assert;
import org.junit.Test;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;


public class InspectionCreatorTest {

	private TransactionManager transactionManager  = new TestDoubleTransactionManager();
	private NullObjectDefaultedInspectionPersistenceFactory inspectionPersistenceFactory = new NullObjectDefaultedInspectionPersistenceFactory();

	private final class EventSaverSaboteur implements EventSaver {
		
		@Override public Event createEvent(CreateInspectionParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
			throw new ProcessingProofTestException();
		}

		public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
			return null;
		}
	}

	@Test
	public void should_use_basic_transaction_management() throws Exception {
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory );
		
		Assert.assertThat(sut, instanceOf(BasicTransactionManagement.class));
	}
	
	@Test
	public void should_create_dependancies_from_the_factory_with_the_entity_manager_from_the_created_transaction() throws Exception {
		Transaction transaction = transactionManager.startTransaction();
		InspectionPersistenceFactory inspectionPersistenceFactory = createMock(InspectionPersistenceFactory.class);
		
		// dependency creation.
		expect(inspectionPersistenceFactory.createInspectionSaver(transaction)).andReturn(new NullEventSaver());
		expect(inspectionPersistenceFactory.createNextInspectionScheduleService(transaction)).andReturn(new NullNextInspectionScheduleSerivce());
		expect(inspectionPersistenceFactory.createCreateInspectionAuditLogger()).andReturn(new NullAuditLogger());
		
		replay(inspectionPersistenceFactory);

		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(new CreateInspectionParameterBuilder(anEvent().build(), 1L).build());
		
		verify(inspectionPersistenceFactory);
	}
	
	@Test
	public void should_have_saver_perform_inspection_save_with_the_passed_in_parameter() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anEvent().build(), 1L).build();
		
		final EventSaver eventSaver = createMock(EventSaver.class);
		expect(eventSaver.createEvent(parameter)).andReturn(parameter.event);
		replay(eventSaver);
		
		inspectionPersistenceFactory.eventSaver = eventSaver;
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
		
		verify(eventSaver);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_throw_a_wrapping_exception_when_saver_throws_an_exception() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anEvent().build(), 1L).build();
		
		inspectionPersistenceFactory.eventSaver = new EventSaverSaboteur();
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
	}
	
	@Test
	public void should_call_the_audit_with_the_saved_inspection_on_success() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anEvent().build(), 1L).build();
		AuditLogger auditLogger = createMock(AuditLogger.class);
		auditLogger.audit((String)anyObject(), same(parameter.event), (Throwable)isNull());
		replay(auditLogger);
		
		inspectionPersistenceFactory.auditLogger = auditLogger;
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
		
		verify(auditLogger);
	}
	
	@Test
	public void should_call_the_audit_with_the_unsaved_inspection_and_the_failure_exception_failure() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anEvent().build(), 1L).build();
		AuditLogger auditLogger = createMock(AuditLogger.class);
		auditLogger.audit(isA(String.class), same(parameter.event), isA(ProcessingProofTestException.class));
		replay(auditLogger);
		
		inspectionPersistenceFactory.auditLogger = auditLogger;
		inspectionPersistenceFactory.eventSaver = new EventSaverSaboteur();
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		try {
			sut.create(parameter);
		} catch (Exception e) {}
		
		verify(auditLogger);
	}
	
	@Test
	public void should_return_the_saved_inspection_on_success() throws Exception {
		final Event savedEvent = anEvent().build();
		
		inspectionPersistenceFactory.eventSaver = new EventSaver() {
			public Event createEvent(CreateInspectionParameter parameterObject) throws ProcessingProofTestException ,FileAttachmentException , UnknownSubAsset {
				return savedEvent;
			}

			@Override
			public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
				return null;
			}
		};
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		Event actualReturnedEvent = sut.create(new CreateInspectionParameterBuilder(anEvent().build(), 1L).build());
		
		assertThat(actualReturnedEvent, sameInstance(savedEvent));
	}
	
	@Test
	public void should_save_the_set_inspection_schedules_given() throws Exception {
		Event event = anEvent().build();
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(event, 1L)
			.addSchedule(new InspectionScheduleBundle(event.getAsset(), InspectionTypeBuilder.anInspectionType().build(), null, new Date()))
			.addSchedule(new InspectionScheduleBundle(event.getAsset(), InspectionTypeBuilder.anInspectionType().build(), null, new Date(2000)))
			.build();
		
		NextInspectionScheduleSerivce nextScheduleService = createMock(NextInspectionScheduleSerivce.class);
		expect(nextScheduleService.createNextSchedule(isA(EventSchedule.class))).andReturn(null).times(2);
		replay(nextScheduleService);
		
		inspectionPersistenceFactory.nextInspectionScheduleSerivce = nextScheduleService;
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
		
		verify(nextScheduleService);
	}
	
}
