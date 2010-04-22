package com.n4systems.handlers.creator;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.persistence.TestingTransaction;


public class WebServiceInspectionsCreatorTest {
	private final class CreateInspectionsMethodObjectSabatour implements CreateInspectionsMethodObject {
		@Override
		public List<Inspection> createInspections(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws TransactionAlreadyProcessedException, ProcessingProofTestException,
				FileAttachmentException, UnknownSubProduct {
			throw new TransactionAlreadyProcessedException();
		}
	}


	private TransactionManager transactionManager  = new TestDoubleTransactionManager();
	private NullObjectDefaultedInspectionPersistenceFactory inspectionPersistenceFactory = new NullObjectDefaultedInspectionPersistenceFactory();

	
	private String transactionGUID = "301001-aas-df-as-dl2kajd";
	private List<Inspection> inspections = new FluentArrayList<Inspection>(anInspection().build(), anInspection().build());
	private Map<Inspection, Date> nextInspectionDates = ImmutableMap.of(inspections.get(0), new Date(), inspections.get(1), new Date());
	
	
	@Test
	public void should_start_and_finish_a_transaction_from_the_transaction_manager_when_about_to_create() throws Exception {
		TestingTransaction transaction = new TestingTransaction();
		
		transactionManager = createMock(TransactionManager.class);
		expect(transactionManager.startTransaction()).andReturn(transaction);
		transactionManager.finishTransaction(transaction);
		replay(transactionManager);
		
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, inspections, nextInspectionDates);
		
		verify(transactionManager);
	}
	
	
	@Test
	public void should_create_CreateInspectionsMethodObject_from_the_factory_with_the_entity_manager_from_the_created_transaction() throws Exception {
		Transaction transaction = transactionManager.startTransaction();
		
		InspectionPersistenceFactory inspectionPersistenceFactory = createMock(InspectionPersistenceFactory.class);
		expect(inspectionPersistenceFactory.createCreateInspectionsMethodObject(transaction)).andReturn(new NullCreateInspectionsMethodObject());
		expect(inspectionPersistenceFactory.createCreateInspectionAuditLogger()).andReturn(new NullAuditLogger());
		replay(inspectionPersistenceFactory);
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, inspections, nextInspectionDates);
		
		verify(inspectionPersistenceFactory);
	}
	
	@Test
	public void should_start_rollback_and_finish_a_transaction_when_there_is_an_exception_thrown_from_the_CreateInspectionsMethodObject() throws Exception {
		TestingTransaction transaction = new TestingTransaction();
		
		transactionManager = createMock(TransactionManager.class);
		expect(transactionManager.startTransaction()).andReturn(transaction);
		transactionManager.rollbackTransaction(transaction);
		transactionManager.finishTransaction(transaction);
		replay(transactionManager);
		
		inspectionPersistenceFactory.createInspectionsMethodObject = new CreateInspectionsMethodObjectSabatour();
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		try {
			sut.create(transactionGUID, inspections, nextInspectionDates);
		} catch (Exception e) {	}
		
		verify(transactionManager);
	}
	
	
	@Test(expected=TransactionAlreadyProcessedException.class)
	public void should_not_wrap_an_exception_thrown_by_the_create_inspections_method() throws Exception {
		inspectionPersistenceFactory.createInspectionsMethodObject = new CreateInspectionsMethodObjectSabatour();
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, inspections, nextInspectionDates);
		
	}	
	
	
	@Test
	public void should_call_the_with_the_inspections_sent_in_CreateInspectionsMethodObject() throws Exception {
		
		CreateInspectionsMethodObject mockCreateInspectionsMethod = createMock(CreateInspectionsMethodObject.class);
		expect(mockCreateInspectionsMethod.createInspections(transactionGUID, inspections, nextInspectionDates)).andReturn(inspections);
		replay(mockCreateInspectionsMethod);
		
		inspectionPersistenceFactory.createInspectionsMethodObject = mockCreateInspectionsMethod;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, inspections, nextInspectionDates);
		
		verify(mockCreateInspectionsMethod);
	}
	
	
	
	@Test
	public void should_return_the_inspections_returned_from_CreateInspectionsMethodObject() throws Exception {
		List<Inspection> savedInspections = new FluentArrayList<Inspection>(anInspection().build(), anInspection().build());
		
		CreateInspectionsMethodObject mockCreateInspectionsMethod = createMock(CreateInspectionsMethodObject.class);
		expect(mockCreateInspectionsMethod.createInspections(transactionGUID, inspections, nextInspectionDates)).andReturn(savedInspections);
		replay(mockCreateInspectionsMethod);
		
		inspectionPersistenceFactory.createInspectionsMethodObject = mockCreateInspectionsMethod;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		List<Inspection> actualSavedInspections = sut.create(transactionGUID, inspections, nextInspectionDates);
		
		Assert.assertThat(actualSavedInspections, equalTo(savedInspections));
	}
	
	
	
	@Test
	public void should_send_a_success_audit_for_each_inspection_saved_after_everything_has_been_saved() throws Exception {
		
		AuditLogger auditLogger = createMock(AuditLogger.class);
		for (Inspection inspection : inspections) {
			auditLogger.audit((String)anyObject(), same(inspection), (Throwable)isNull());
		}
		
		replay(auditLogger);
		
		inspectionPersistenceFactory.auditLogger = auditLogger;

		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(transactionGUID, inspections, nextInspectionDates);
				
		verify(auditLogger);
	}
	
	
	@Test
	public void should_send_a_failure_audit_for_each_inspection_after_everything_has_failed() throws Exception {
		
		AuditLogger auditLogger = createMock(AuditLogger.class);
		for (Inspection inspection : inspections) {
			auditLogger.audit(isA(String.class), same(inspection), isA(TransactionAlreadyProcessedException.class));
		}
		replay(auditLogger);
		
		
		inspectionPersistenceFactory.createInspectionsMethodObject = new CreateInspectionsMethodObjectSabatour();
		inspectionPersistenceFactory.auditLogger = auditLogger;
		
		
		WebServiceInspectionsCreator sut = new WebServiceInspectionsCreator(transactionManager, inspectionPersistenceFactory);
		
		try {
			sut.create(transactionGUID, inspections, nextInspectionDates);
		} catch(Exception e) {}
				
		verify(auditLogger);
	}
	
	
}
