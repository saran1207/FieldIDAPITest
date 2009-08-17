package com.n4systems.taskscheduling.task;

import com.n4systems.handlers.remover.InspectionTypeArchiveHandler;
import com.n4systems.handlers.remover.RemovalHandlerFactory;
import com.n4systems.model.InspectionType;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;


public class InspectionTypeArchiveTask implements Runnable {

	private final InspectionType inspectionType;
	private final RemovalHandlerFactory handlerFactory;
	
	private Transaction transaction;
	private InspectionTypeArchiveHandler archiveHandler;
	
	
	public InspectionTypeArchiveTask(InspectionType inspectionType, RemovalHandlerFactory handlerFactory) {
		super();
		this.inspectionType = inspectionType;
		this.handlerFactory = handlerFactory;
	}

	
	public void run() {
		try {
			setUp();
			executeArchiving();
			sendEmailResponse();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			//TODO:  send failure!
		}
	}


	private void setUp() {
		transaction = PersistenceManager.startTransaction();
		archiveHandler = handlerFactory.getInspectionTypeArchiveHandler();
	}


	private void executeArchiving() {
		archiveHandler.forInspectionType(inspectionType).remove(transaction);
	}


	private void sendEmailResponse() {
		//TODO Send email.
	}
	
	


}
