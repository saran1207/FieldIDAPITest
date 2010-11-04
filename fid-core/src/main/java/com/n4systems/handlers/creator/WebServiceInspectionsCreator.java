package com.n4systems.handlers.creator;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public class WebServiceInspectionsCreator extends BasicTransactionManagement implements InspectionsInAGroupCreator {

	private final EventPersistenceFactory eventPersistenceFactory;
	private Map<Event, Date> nextInspectionDates;
	private List<Event> events;
	private String transactionGUID;
	private List<Event> results = null;
	private NextInspectionScheduleSerivce createNextInspectionScheduleService;
	private CreateEventsMethodObject createEventsMethod;

	public WebServiceInspectionsCreator(TransactionManager transactionManager, EventPersistenceFactory eventPersistenceFactory) {
		super(transactionManager);
		this.eventPersistenceFactory = eventPersistenceFactory;
	}

	public List<Event> create(String transactionGUID, List<Event> events, Map<Event, Date> nextInspectionDates) throws TransactionAlreadyProcessedException,
			ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
		this.transactionGUID = transactionGUID;
		this.events = events;
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
		logFailure(events, e);
	}

	protected void doProcess(Transaction transaction) {
		createTransactionDepenancies(transaction);
		createInspections(transaction);
		createSchedules(transaction);
		
	}

	private void createTransactionDepenancies(Transaction transaction) {
		createEventsMethod = eventPersistenceFactory.createCreateEventsMethodObject(transaction);
		createNextInspectionScheduleService = eventPersistenceFactory.createNextEventScheduleService(transaction);
	}

	private void createInspections(Transaction transaction) {
		results = createEventsMethod.createEvents(transactionGUID, events);
	}

	private void createSchedules(Transaction transaction) {
		for (Entry<Event, Date> nextInspectionDate : nextInspectionDates.entrySet()) {
			Event eventCreatingSchedule = nextInspectionDate.getKey();
			
			if (nextInspectionDate.getValue() != null) {
				EventSchedule schedule = new EventSchedule(eventCreatingSchedule.getAsset(), eventCreatingSchedule.getType(), nextInspectionDate.getValue());
				createNextInspectionScheduleService.createNextSchedule(schedule);
			}
		}
	}


	private void logFailure(List<Event> events, Exception e) {
		auditLogInspection(events, e);
	}

	private void auditLogInspection(List<Event> events, Exception e) {
		AuditLogger auditLogger = eventPersistenceFactory.createCreateEventAuditLogger();
		for (Event event : events) {
			auditLogger.audit("Create Inspections", event, e);
		}
	}

	private void logSuccess(List<Event> events) {
		auditLogInspection(events, null);
	}

	

}
