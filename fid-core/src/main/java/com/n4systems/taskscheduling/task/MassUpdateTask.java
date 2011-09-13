package com.n4systems.taskscheduling.task;

import org.apache.log4j.Logger;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public abstract class MassUpdateTask implements Runnable {

	private static final Logger logger = Logger.getLogger(MassUpdateTask.class);
	private Transaction transaction;
	protected MassUpdateManager massUpdateManager;
	
	public MassUpdateTask(MassUpdateManager massUpdateManager) {
		this.massUpdateManager = massUpdateManager;
	}
	
	@Override
	public void run() {
		try {
			setup();
            logger.info("Starting mass update: " + getClass() + " " + getExecutionDetails());
			executeMassUpdate();
            logger.info("Completed mass update: " + getClass() + " " + getExecutionDetails());
			sendSuccessEmailResponse();
			transaction.commit();
		} catch (Exception e) {
			failure(e);
		}
	}

    protected String getExecutionDetails() { return ""; }

	private void setup() {
		transaction = PersistenceManager.startTransaction();
	}

	protected abstract void executeMassUpdate() throws UpdateFailureException, UpdateConatraintViolationException;

	protected abstract void sendSuccessEmailResponse();

	protected abstract void sendFailureEmailResponse();

	private void failure(Exception e) {
		logger.error("Failed to perform mass update", e);
		transaction.rollback();

		PersistenceManager.shutdown();

		sendFailureEmailResponse();
	}

	protected void sendEmailResponse(String subject, String body, User currentUser) {		
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, currentUser.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Event Type removal email", e);
        }
	}
	
}