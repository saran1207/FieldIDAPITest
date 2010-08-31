package com.n4systems.taskscheduling.task;

import org.apache.log4j.Logger;


import com.n4systems.handlers.remover.InspectionTypeArchiveHandler;
import com.n4systems.handlers.remover.RemovalHandlerFactory;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.InspectionTypeSaver;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;


public class InspectionTypeArchiveTask implements Runnable {
	private static final Logger logger = Logger.getLogger(InspectionTypeArchiveTask.class);	
	
	
	private final InspectionType inspectionType;
	private final RemovalHandlerFactory handlerFactory;
	private final User currentUser;
	
	private Transaction transaction;
	private InspectionTypeArchiveHandler archiveHandler;
	
	
	public InspectionTypeArchiveTask(InspectionType inspectionType, User user, RemovalHandlerFactory handlerFactory) {
		super();
		this.inspectionType = inspectionType;
		this.handlerFactory = handlerFactory;
		this.currentUser = user;
	}

	
	public void run() {
		try {
			setUp();
			executeArchiving();
			sendSuccessEmailResponse();
			transaction.commit();
		} catch (Exception e) {
			failure(e);
		}
	}


	private void failure(Exception e) {
		logger.error("failed to archive inspection type", e);
		transaction.rollback();
		
		PersistenceManager.shutdown();
		
		sendFailureEmailResponse();
		
		revertInspectionTypeToNonArchivedState();
	}


	private void revertInspectionTypeToNonArchivedState() {
		transaction = PersistenceManager.startTransaction();
		inspectionType.activateEntity();
		new InspectionTypeSaver().update(transaction, inspectionType);
		transaction.commit();
	}


	private void setUp() {
		transaction = PersistenceManager.startTransaction();
		archiveHandler = handlerFactory.getInspectionTypeArchiveHandler();
	}


	private void executeArchiving() {
		archiveHandler.forInspectionType(inspectionType).remove(transaction);
	}


	private void sendFailureEmailResponse() {
		String subject = "Inspection Type Removal Failed To Complete";
		String body = "<h2>Inspection Type Removed " + inspectionType.getArchivedName() + "</h2>" +
				"The inspection type and all other associated elements have been restored.  You may try to delete the inspection type again or contact support@n4systems for more insformations.";
		
		logger.info("Sending failure email [" + currentUser.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, currentUser.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Inspection Type removal email", e);
        }
	}
	
	private void sendSuccessEmailResponse() {
		String subject = "Inspection Type Removal Completed";
		String body = "<h2>Inspection Type Removed " +  inspectionType.getArchivedName() + "</h2>";
		
		logger.info("Sending email [" + currentUser.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, currentUser.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Inspection Type removal email", e);
        }
	}
	
	


}
