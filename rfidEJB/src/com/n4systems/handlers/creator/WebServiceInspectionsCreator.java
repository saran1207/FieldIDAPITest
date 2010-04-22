package com.n4systems.handlers.creator;

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

public class WebServiceInspectionsCreator extends BasicTransactionManagement implements InspectionsInAGroupCreator {

	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	private Map<Inspection, Date> nextInspectionDates;
	private List<Inspection> inspections;
	private String transactionGUID;
	private List<Inspection> results = null;

	public WebServiceInspectionsCreator(TransactionManager transactionManager, InspectionPersistenceFactory inspectionPersistenceFactory) {
		super(transactionManager);
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
	}

	public List<Inspection> create(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws TransactionAlreadyProcessedException, 
			ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {
		this.transactionGUID = transactionGUID;
		this.inspections = inspections;
		this.nextInspectionDates = nextInspectionDates;
		
		run();
		
		return results; 
	}

	protected void finished() {
	}

	protected void success() {
		
		
	}

	protected void failure(Exception e) {
		logFailure(inspections, e);
	}

	protected void doProcess(Transaction transaction) {
		List<Inspection> savedInspections = inspectionPersistenceFactory.createCreateInspectionsMethodObject(transaction).createInspections(transactionGUID, inspections, nextInspectionDates);
		logSuccess(savedInspections);
		results = savedInspections;
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
