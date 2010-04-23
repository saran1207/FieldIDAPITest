package com.n4systems.handlers.creator;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public class WebServiceInspectionsCreator extends BasicTransactionManagement implements InspectionsInAGroupCreator {

	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	private Map<Inspection, Date> nextInspectionDates;
	private List<Inspection> inspections;
	private String transactionGUID;
	private List<Inspection> results = null;
	private NextInspectionScheduleSerivce createNextInspectionScheduleService;
	private CreateInspectionsMethodObject createInspectionsMethod;

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
		logSuccess(results);
		
	}

	protected void failure(Exception e) {
		logFailure(inspections, e);
	}

	protected void doProcess(Transaction transaction) {
		createTransactionDepenancies(transaction);
		createInspections(transaction);
		createSchedules(transaction);
		
	}

	private void createTransactionDepenancies(Transaction transaction) {
		createInspectionsMethod = inspectionPersistenceFactory.createCreateInspectionsMethodObject(transaction);
		createNextInspectionScheduleService = inspectionPersistenceFactory.createNextInspectionScheduleService(transaction);
	}

	private void createInspections(Transaction transaction) {
		results = createInspectionsMethod.createInspections(transactionGUID, inspections);
	}

	private void createSchedules(Transaction transaction) {
		for (Entry<Inspection, Date> nextInspectionDate : nextInspectionDates.entrySet()) {
			Inspection inspectionCreatingSchedule = nextInspectionDate.getKey();
			InspectionSchedule schedule = new InspectionSchedule(inspectionCreatingSchedule.getProduct(), inspectionCreatingSchedule.getType(), nextInspectionDate.getValue());
			createNextInspectionScheduleService.createNextSchedule(schedule);
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
