package com.n4systems.handlers.creator.events;

import com.n4systems.ejb.impl.CreateEventParameter;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.creator.BasicTransactionManagement;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextEventScheduleSerivce;

public class EventCreator extends BasicTransactionManagement {

	private final EventPersistenceFactory eventPersistenceFactory;
	private AuditLogger auditLogger;
	private CreateEventParameter parameter;
	private Event result;
	private NextEventScheduleSerivce nextScheduleSerivce;
	private EventSaver createEventSaver;

	public EventCreator(TransactionManager transactionManager, EventPersistenceFactory eventPersistenceFactory) {
		super(transactionManager);
		this.eventPersistenceFactory = eventPersistenceFactory;
	}

	public Event create(CreateEventParameter parameter) throws ProcessFailureException {
		this.parameter = parameter;
		auditLogger = eventPersistenceFactory.createCreateEventAuditLogger();
		
		run();
		return result;
	}

	@Override
	protected RuntimeException exceptionWrapper(Exception e) {
		return new ProcessFailureException(e);
	}

	private void recordFailure(CreateEventParameter parameter, Exception e) {
		audit(parameter, auditLogger, e);
	}

	private void audit(CreateEventParameter parameter, AuditLogger auditLogger, Exception e) {
		auditLogger.audit("createEvent", parameter.event, e);
	}
	
	private void recordSuccess(CreateEventParameter parameter) {
		audit(parameter, auditLogger, null);
	}



	@Override
	protected void doProcess(Transaction transaction) {
		createTransactionDependentServices(transaction);
		createEvent(transaction);
		createSchedules(transaction);
	}

	private void createTransactionDependentServices(Transaction transaction) {
		createEventSaver = eventPersistenceFactory.createEventSaver(transaction);
		nextScheduleSerivce = eventPersistenceFactory.createNextEventScheduleService(transaction);
	}

	private void createSchedules(Transaction transaction) {
		for (EventScheduleBundle eventScheduleBundle : parameter.schedules) {
			EventSchedule eventSchedule = new EventSchedule(eventScheduleBundle.getAsset(), eventScheduleBundle.getType(), eventScheduleBundle.getScheduledDate());
			eventSchedule.setProject(eventScheduleBundle.getJob());
			nextScheduleSerivce.createNextSchedule(eventSchedule);
		}
	}

	private void createEvent(Transaction transaction) {
		result = createEventSaver.createEvent(parameter);
	}

	@Override
	protected void failure(Exception e) {
		recordFailure(parameter, e);
	}

	@Override
	protected void finished() {
	}

	@Override
	protected void success() {
		recordSuccess(parameter);
	}

}
