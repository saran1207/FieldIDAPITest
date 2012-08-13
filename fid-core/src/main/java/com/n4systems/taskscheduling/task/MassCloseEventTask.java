package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;

import java.util.List;

public class MassCloseEventTask implements Runnable{

    private static final Logger logger = Logger.getLogger(MassCloseEventTask.class);
    private List<Long> ids;
    private Event event;
    private User modifiedBy;

    private Transaction transaction;
    private MassUpdateManager massUpdateManager;

    public MassCloseEventTask(MassUpdateManager massUpdateManager, List<Long> ids, Event event, User modifiedBy) {
        this.massUpdateManager = massUpdateManager;
        this.ids = ids;
		this.event = event;
		this.modifiedBy = modifiedBy;
    }

    @Override
    public void run() {
        try {
            setup();
            logger.info("Starting mass close events: " + getClass());
            executeMassUpdate();
            logger.info("Completed mass close events: " + getClass());
            sendSuccessEmailResponse();
        } catch (Exception e) {
            failure(e);
        }
    }

    private void setup() {
        transaction = PersistenceManager.startTransaction();
    }

    private void executeMassUpdate() throws UpdateFailureException {
        massUpdateManager.closeEvents(ids, event, modifiedBy);
	}

    private void failure(Exception e) {
        logger.error("Failed to perform mass update", e);
        transaction.rollback();

        PersistenceManager.shutdown();

        sendFailureEmailResponse();
    }

    private void sendEmailResponse(String subject, String body, User currentUser) {
        try {
            ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, currentUser.getEmailAddress()));
        } catch (Exception e) {
            logger.error("Unable to send Event Type removal email", e);
        }
    }

    private void sendFailureEmailResponse() {
        String subject="Mass closing of events failed";
        String body="Failed to close " + ids.size() + " events";
        sendEmailResponse(subject, body, modifiedBy);
    }

    private void sendSuccessEmailResponse() {
        String subject="Mass closing of events completed";
        String body="Closed " + ids.size() + " events successfully";
        sendEmailResponse(subject, body, modifiedBy);
    }
}
