package com.n4systems.handlers.creator.inspections;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.creator.BasicTransactionManagement;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public class InspectionCreator extends BasicTransactionManagement {

	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	private AuditLogger auditLogger;
	private CreateInspectionParameter parameter;
	private Event result;
	private NextInspectionScheduleSerivce nextScheduleSerivce;
	private EventSaver createEventSaver;

	public InspectionCreator(TransactionManager transactionManager, InspectionPersistenceFactory inspectionPersistenceFactory) {
		super(transactionManager);
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
	}

	public Event create(CreateInspectionParameter parameter) throws ProcessFailureException {
		this.parameter = parameter;
		auditLogger = inspectionPersistenceFactory.createCreateInspectionAuditLogger();
		
		run();
		return result;
	}

	@Override
	protected RuntimeException exceptionWrapper(Exception e) {
		return new ProcessFailureException(e);
	}

	private void recordFailure(CreateInspectionParameter parameter, Exception e) {
		audit(parameter, auditLogger, e);
	}

	private void audit(CreateInspectionParameter parameter, AuditLogger auditLogger, Exception e) {
		auditLogger.audit("createEvent", parameter.event, e);
	}
	
	private void recordSuccess(CreateInspectionParameter parameter) {
		audit(parameter, auditLogger, null);
	}



	@Override
	protected void doProcess(Transaction transaction) {
		createTransactionDependentServices(transaction);
		createInspection(transaction);
		createSchedules(transaction);
	}

	private void createTransactionDependentServices(Transaction transaction) {
		createEventSaver = inspectionPersistenceFactory.createInspectionSaver(transaction);
		nextScheduleSerivce = inspectionPersistenceFactory.createNextInspectionScheduleService(transaction);
	}

	private void createSchedules(Transaction transaction) {
		for (InspectionScheduleBundle inspectionScheduleBundle : parameter.schedules) {
			EventSchedule eventSchedule = new EventSchedule(inspectionScheduleBundle.getAsset(), inspectionScheduleBundle.getType(), inspectionScheduleBundle.getScheduledDate());
			eventSchedule.setProject(inspectionScheduleBundle.getJob());
			nextScheduleSerivce.createNextSchedule(eventSchedule);
		}
	}

	private void createInspection(Transaction transaction) {
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
