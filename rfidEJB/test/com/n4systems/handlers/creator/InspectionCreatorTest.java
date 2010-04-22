package com.n4systems.handlers.creator;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.util.persistence.TestingTransaction;


public class InspectionCreatorTest {

	private TransactionManager transactionManager  = new TestDoubleTransactionManager();
	private NullObjectDefaultedInspectionPersistenceFactory inspectionPersistenceFactory = new NullObjectDefaultedInspectionPersistenceFactory();

	private final class InspectionSaverSabatour implements InspectionSaver {
		
		
		@Override public Inspection createInspection(CreateInspectionParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {
			throw new ProcessingProofTestException();
		}

		public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
			return null;
		}
	}


	@Test
	public void should_start_and_finish_a_transaction_from_the_transaction_manager_when_about_to_create() throws Exception {
		TestingTransaction transaction = new TestingTransaction();
		
		transactionManager = createMock(TransactionManager.class);
		expect(transactionManager.startTransaction()).andReturn(transaction);
		transactionManager.finishTransaction(transaction);
		replay(transactionManager);
		
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory );
		
		sut.create(new CreateInspectionParameterBuilder(anInspection().build(), 1L).build());
		
		verify(transactionManager);
	}
	
	
	
	
	@Test
	public void should_create_saver_from_the_factory_with_the_entity_manager_from_the_created_transaction() throws Exception {
		Transaction transaction = transactionManager.startTransaction();
		
		InspectionPersistenceFactory inspectionPersistenceFactory = createMock(InspectionPersistenceFactory.class);
		expect(inspectionPersistenceFactory.createInspectionSaver(transaction)).andReturn(new NullInspectionSaver());
		expect(inspectionPersistenceFactory.createCreateInspectionAuditLogger()).andReturn(new NullAuditLogger());
		replay(inspectionPersistenceFactory);
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(new CreateInspectionParameterBuilder(anInspection().build(), 1L).build());
		
		verify(inspectionPersistenceFactory);
	}
	
	
	@Test
	public void should_have_saver_perform_inspection_save_with_the_passed_in_parameter() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anInspection().build(), 1L).build();
		
		final InspectionSaver inspectionSaver = createMock(InspectionSaver.class);
		expect(inspectionSaver.createInspection(parameter)).andReturn(parameter.inspection);
		replay(inspectionSaver);
		
		inspectionPersistenceFactory.inspectionSaver = inspectionSaver;
		
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
		
		verify(inspectionSaver);
	}
	
	
	@Test
	public void should_rollback_the_transaction_if_the_saver_throws_an_exception() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anInspection().build(), 1L).build();
		
		inspectionPersistenceFactory.inspectionSaver = new InspectionSaverSabatour();
		
		TestingTransaction transaction = new TestingTransaction();
		
		transactionManager = createMock(TransactionManager.class);
		expect(transactionManager.startTransaction()).andReturn(transaction);
		transactionManager.rollbackTransaction(transaction);
		transactionManager.finishTransaction(transaction);
		replay(transactionManager);
		
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		try {
			sut.create(parameter);
		} catch (Exception e) {
			// don't do anything with an exception
		}
		
		verify(transactionManager);
	}
	
	
	@Test(expected=ProcessFailureException.class)
	public void should_throw_a_wrapping_exception_when_saver_throws_an_exception() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anInspection().build(), 1L).build();
		
		inspectionPersistenceFactory.inspectionSaver = new InspectionSaverSabatour();
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
		
	}
	
	
	
	@Test
	public void should_call_the_audit_with_the_saved_inspection_on_success() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anInspection().build(), 1L).build();
		AuditLogger auditLogger = createMock(AuditLogger.class);
		auditLogger.audit((String)anyObject(), same(parameter.inspection), (Throwable)isNull());
		replay(auditLogger);
		
		inspectionPersistenceFactory.auditLogger = auditLogger;
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		sut.create(parameter);
		
		verify(auditLogger);
	}
	
	@Test
	public void should_call_the_audit_with_the_unsaved_inspection_and_the_failure_exception_failure() throws Exception {
		CreateInspectionParameter parameter = new CreateInspectionParameterBuilder(anInspection().build(), 1L).build();
		AuditLogger auditLogger = createMock(AuditLogger.class);
		auditLogger.audit(isA(String.class), same(parameter.inspection), isA(ProcessingProofTestException.class));
		replay(auditLogger);
		
		inspectionPersistenceFactory.auditLogger = auditLogger;
		inspectionPersistenceFactory.inspectionSaver = new InspectionSaverSabatour();
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		try {
			sut.create(parameter);
		} catch (Exception e) {}
		
		verify(auditLogger);
	}
	
	
	
	@Test
	public void should_get_audit_logger_from_factory() throws Exception {
		InspectionPersistenceFactory inspectionPersistenceFactory = createMock(InspectionPersistenceFactory.class);
		expect(inspectionPersistenceFactory.createInspectionSaver((Transaction)anyObject())).andReturn(new NullInspectionSaver());
		expect(inspectionPersistenceFactory.createCreateInspectionAuditLogger()).andReturn(new NullAuditLogger());
		replay(inspectionPersistenceFactory);
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		try {
			sut.create(new CreateInspectionParameterBuilder(anInspection().build(), 1L).build());
		} catch (Exception e) {	}
		
		verify(inspectionPersistenceFactory);
	
	}
	
	@Test
	public void should_return_the_saved_inspection_on_success() throws Exception {
		final Inspection savedInspection = anInspection().build();
		
		inspectionPersistenceFactory.inspectionSaver = new InspectionSaver() {
			public Inspection createInspection(CreateInspectionParameter parameterObject) throws ProcessingProofTestException ,FileAttachmentException ,UnknownSubProduct {
				return savedInspection;
			}

			@Override
			public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
				return null;
			}
		};
		
		InspectionCreator sut = new InspectionCreator(transactionManager, inspectionPersistenceFactory);
		
		Inspection actualReturnedInspection = sut.create(new CreateInspectionParameterBuilder(anInspection().build(), 1L).build());
		
		assertThat(actualReturnedInspection, sameInstance(savedInspection));
	}


	
}
