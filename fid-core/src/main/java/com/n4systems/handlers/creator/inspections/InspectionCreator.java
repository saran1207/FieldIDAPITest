package com.n4systems.handlers.creator.inspections;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.creator.BasicTransactionManagement;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public class InspectionCreator extends BasicTransactionManagement {

	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	private AuditLogger auditLogger;
	private CreateInspectionParameter parameter;
	private Inspection result;
	private NextInspectionScheduleSerivce nextScheduleSerivce;
	private InspectionSaver createInspectionSaver;

	public InspectionCreator(TransactionManager transactionManager, InspectionPersistenceFactory inspectionPersistenceFactory) {
		super(transactionManager);
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
	}

	public Inspection create(CreateInspectionParameter parameter) throws ProcessFailureException {
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
		auditLogger.audit("createInspection", parameter.inspection, e);
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
		createInspectionSaver = inspectionPersistenceFactory.createInspectionSaver(transaction);
		nextScheduleSerivce = inspectionPersistenceFactory.createNextInspectionScheduleService(transaction);
	}

	private void createSchedules(Transaction transaction) {
		for (InspectionScheduleBundle inspectionScheduleBundle : parameter.schedules) {
			InspectionSchedule inspectionSchedule = new InspectionSchedule(inspectionScheduleBundle.getAsset(), inspectionScheduleBundle.getType(), inspectionScheduleBundle.getScheduledDate());
			inspectionSchedule.setProject(inspectionScheduleBundle.getJob());
			nextScheduleSerivce.createNextSchedule(inspectionSchedule);
		}
	}

	private void createInspection(Transaction transaction) {
		result = createInspectionSaver.createInspection(parameter);
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
