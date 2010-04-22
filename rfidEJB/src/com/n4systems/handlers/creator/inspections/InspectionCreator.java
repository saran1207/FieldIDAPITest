package com.n4systems.handlers.creator.inspections;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;

public class InspectionCreator  {

	private final TransactionManager transactionManager;
	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	private AuditLogger auditLogger;

	public InspectionCreator(TransactionManager transactionManager, InspectionPersistenceFactory inspectionPersistenceFactory) {
		this.transactionManager = transactionManager;
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
	}

	public Inspection create(CreateInspectionParameter parameter) throws ProcessFailureException {
		Transaction transaction = transactionManager.startTransaction();
		auditLogger = inspectionPersistenceFactory.createCreateInspectionAuditLogger();
		try {
			return doAuditedCreate(parameter, transaction);
		} catch (Exception e) {
			transactionManager.rollbackTransaction(transaction);
			throw new ProcessFailureException(e);
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	private Inspection doAuditedCreate(CreateInspectionParameter parameter, Transaction transaction) throws Exception {
		try {
			Inspection savedInspection = doCreate(parameter, transaction);
			recordSuccess(parameter);
			return savedInspection;
		} catch (Exception e) {
			recordFailure(parameter, e);
			throw e;
		}
	}


	private void recordFailure(CreateInspectionParameter parameter, Exception e) {
		audit(parameter, auditLogger, e);
	}

	private void audit(CreateInspectionParameter parameter, AuditLogger auditLogger, Exception e) {
		auditLogger.audit("createInspection", parameter.inspection, e);
	}
	
	private void recordSuccess(CreateInspectionParameter parameter) {
		audit(parameter, auditLogger, null);
	}

	private Inspection doCreate(CreateInspectionParameter parameter, Transaction transaction) throws ProcessingProofTestException, UnknownSubProduct {
		InspectionSaver createInspectionSaver = inspectionPersistenceFactory.createInspectionSaver(transaction);
		return createInspectionSaver.createInspection(parameter);
	}


	
}
