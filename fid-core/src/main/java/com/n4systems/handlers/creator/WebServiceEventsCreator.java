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
import com.n4systems.services.NextEventScheduleSerivce;

public class WebServiceEventsCreator extends BasicTransactionManagement implements EventsInAGroupCreator {

	private final EventPersistenceFactory eventPersistenceFactory;
	private Map<Event, Date> nextEventDates;
	private List<Event> events;
	private String transactionGUID;
	private List<Event> results = null;
	private NextEventScheduleSerivce createNextEventScheduleService;
	private CreateEventsMethodObject createEventsMethod;

	public WebServiceEventsCreator(TransactionManager transactionManager, EventPersistenceFactory eventPersistenceFactory) {
		super(transactionManager);
		this.eventPersistenceFactory = eventPersistenceFactory;
	}

	public List<Event> create(String transactionGUID, List<Event> events, Map<Event, Date> nextEventDates) throws TransactionAlreadyProcessedException,
			ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
		this.transactionGUID = transactionGUID;
		this.events = events;
		this.nextEventDates = nextEventDates;
		
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
		createTransactionDependencies(transaction);
		createEvents(transaction);
		createSchedules(transaction);
		
	}

	private void createTransactionDependencies(Transaction transaction) {
		createEventsMethod = eventPersistenceFactory.createCreateEventsMethodObject(transaction);
		createNextEventScheduleService = eventPersistenceFactory.createNextEventScheduleService(transaction);
	}

	private void createEvents(Transaction transaction) {
		results = createEventsMethod.createEvents(transactionGUID, events);
	}

	private void createSchedules(Transaction transaction) {
		for (Entry<Event, Date> nextEventDate : nextEventDates.entrySet()) {
			Event eventCreatingSchedule = nextEventDate.getKey();
			
			if (nextEventDate.getValue() != null) {
                Event openEvent = new Event();
                openEvent.setAsset(eventCreatingSchedule.getAsset());
                openEvent.setType(eventCreatingSchedule.getType());
				createNextEventScheduleService.createNextSchedule(openEvent);
			}
		}
	}


	private void logFailure(List<Event> events, Exception e) {
		auditLogEvent(events, e);
	}

	private void auditLogEvent(List<Event> events, Exception e) {
		AuditLogger auditLogger = eventPersistenceFactory.createCreateEventAuditLogger();
		for (Event event : events) {
			auditLogger.audit("Create Events", event, e);
		}
	}

	private void logSuccess(List<Event> events) {
		auditLogEvent(events, null);
	}

	

}
