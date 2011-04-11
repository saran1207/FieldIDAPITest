package com.n4systems.taskscheduling.task;

import org.apache.log4j.Logger;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.user.User;
import com.n4systems.services.customer.CustomerMerger;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;

public class CustomerMergeTask implements Runnable {
	private static final Logger logger = Logger.getLogger(CustomerMergeTask.class);
	
	private final CustomerOrg winningCustomer;
	private final CustomerOrg losingCustomer;
	private final User user;
	
	private EventManager eventManager;
	private EventScheduleManager eventScheduleManager;
	
	public CustomerMergeTask(CustomerOrg winningCustomer, CustomerOrg losingCustomer, User user) {
		this.winningCustomer = winningCustomer;
		this.losingCustomer = losingCustomer;
		this.user = user;
	}

	@Override
	public void run() {
		setUp();
		executeMerge();
		sendEmailResponse();
	}

	public void setUp() {
		eventManager = ServiceLocator.getEventManager();
		eventScheduleManager = ServiceLocator.getEventScheduleManager();
	}


	private void executeMerge() {
		try {
			CustomerMerger merger = new CustomerMerger(eventManager, eventScheduleManager, user);
			merger.merge(winningCustomer, losingCustomer);
		} catch (Exception e) {
			logger.error("could not merge customers", e);
		}
	}

	private void sendEmailResponse() {
		try {
			 ServiceLocator.getMailManager().sendMessage(getMessage());					
		} catch (Exception e) {
			logger.error("Unable to send customer merge completed email");
		}
	}

	private MailMessage getMessage() {
		String subject = "Customer Merge Completed";
		TemplateMailMessage message = new TemplateMailMessage(subject , "customerMerge");
		message.getToAddresses().add(user.getEmailAddress());
		message.getTemplateMap().put("winner", winningCustomer.getName());
		message.getTemplateMap().put("loser", losingCustomer.getName());
		return message;
	}



}
