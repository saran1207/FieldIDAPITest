package com.n4systems.handlers.creator;

import static com.n4systems.exceptions.Exceptions.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;

public class WebServiceInspectionsCreator implements InspectionsInAGroupCreator {

	private final TransactionManager transactionManager;
	private final InspectionPersistenceFactory inspectionPersistenceFactory;

	public WebServiceInspectionsCreator(TransactionManager transactionManager, InspectionPersistenceFactory inspectionPersistenceFactory) {
		this.transactionManager = transactionManager;
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
	}

	public List<Inspection> create(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws TransactionAlreadyProcessedException, 
			ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {
		Transaction transaction = transactionManager.startTransaction();
		
		try {
			List<Inspection> savedInspections = inspectionPersistenceFactory.createCreateInspectionsMethodObject(transaction).createInspections(transactionGUID, inspections, nextInspectionDates);
		
			logSuccess(savedInspections);
			
			return savedInspections;
			
		} catch (Exception e) {
			transactionManager.rollbackTransaction(transaction);
			logFailure(inspections, e);
			throw convertToRuntimeException(e);
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	private void logFailure(List<Inspection> inspections, Exception e) {
		auditLogInspection(inspections, e);
	}

	private void auditLogInspection(List<Inspection> inspections, Exception e) {
		AuditLogger auditLogger = inspectionPersistenceFactory.createCreateInspectionAuditLogger();
		for (Inspection inspection : inspections) {
			auditLogger.audit("Create Inspections", inspection, e);
		}
	}

	private void logSuccess(List<Inspection> inspections) {
		auditLogInspection(inspections, null);
	}

	

}
