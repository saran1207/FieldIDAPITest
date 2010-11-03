package com.n4systems.taskscheduling.task;

import com.n4systems.handlers.remover.EventTypeArchiveHandler;
import com.n4systems.model.EventType;
import org.apache.log4j.Logger;


import com.n4systems.handlers.remover.RemovalHandlerFactory;
import com.n4systems.model.inspectiontype.EventTypeSaver;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;


public class EventTypeArchiveTask implements Runnable {
	private static final Logger logger = Logger.getLogger(EventTypeArchiveTask.class);
	
	
	private final EventType eventType;
	private final RemovalHandlerFactory handlerFactory;
	private final User currentUser;
	
	private Transaction transaction;
	private EventTypeArchiveHandler archiveHandler;
	
	
	public EventTypeArchiveTask(EventType eventType, User user, RemovalHandlerFactory handlerFactory) {
		super();
		this.eventType = eventType;
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
		eventType.activateEntity();
		new EventTypeSaver().update(transaction, eventType);
		transaction.commit();
	}


	private void setUp() {
		transaction = PersistenceManager.startTransaction();
		archiveHandler = handlerFactory.getInspectionTypeArchiveHandler();
	}


	private void executeArchiving() {
		archiveHandler.forEventType(eventType).remove(transaction);
	}


	private void sendFailureEmailResponse() {
		String subject = "Event Type Removal Failed To Complete";
		String body = "<h2>Event Type Removed " + eventType.getArchivedName() + "</h2>" +
				"The event type and all other associated elements have been restored.  You may try to delete the event type again or contact support@n4systems for more information.";
		
		logger.info("Sending failure email [" + currentUser.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, currentUser.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Event Type removal email", e);
        }
	}
	
	private void sendSuccessEmailResponse() {
		String subject = "Event Type Removal Completed";
		String body = "<h2>Event Type Removed " +  eventType.getArchivedName() + "</h2>";
		
		logger.info("Sending email [" + currentUser.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, currentUser.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Event Type removal email", e);
        }
	}
	
	


}
