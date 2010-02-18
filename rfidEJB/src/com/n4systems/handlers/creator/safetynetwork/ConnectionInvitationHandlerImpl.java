package com.n4systems.handlers.creator.safetynetwork;

import com.n4systems.ejb.MailManager;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageCommand;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.uri.ActionURLBuilder;

public class ConnectionInvitationHandlerImpl implements ConnectionInvitationHandler {

	private final MessageSaver saver;
	private final MailManager mailManager;
	private final String defaultBody;
	private final String messageSubject;
	private final AdminUserListLoader adminLoader;
	private final ActionURLBuilder urlBuilder;
	
	private MessageCommand command;
	private InternalOrg localOrg;
	private InternalOrg remoteOrg;
	private String personalizedBody;
	private boolean notificationSent = false;
	
	
	
	public ConnectionInvitationHandlerImpl(MessageSaver saver, MailManager mailManager, String defaultMessageBody,	 String subject, AdminUserListLoader adminLoader, ActionURLBuilder urlBuilder) {
		this.saver = saver;
		this.defaultBody = defaultMessageBody;
		this.messageSubject = subject;
		this.mailManager = mailManager;
		this.adminLoader = adminLoader;
		this.urlBuilder = urlBuilder;
	}

	@Override
	public void create(Transaction transaction) {
		guard();
		Message message = createMessage();
		saveMessage(transaction, message);
		
		sendNotification(transaction, message);
	}

	private void sendNotification(Transaction transaction, Message message) {
		try {
			mailManager.sendMessage(createNotification(transaction, message));
			notificationSent = true;
		} catch (Exception e) {
			notificationSent = false;
		} 
	}

	private TemplateMailMessage createNotification(Transaction transaction, Message message) {
		TemplateMailMessage notificationMessage = new TemplateMailMessage(messageSubject, "safetyNetworkInvitaionNotification");
		notificationMessage.getToAddresses().add(adminLoader.load(transaction).get(0).getEmailAddress());
		notificationMessage.getTemplateMap().put("company_name", localOrg.getName());
		notificationMessage.getTemplateMap().put("message", personalizedBody);
		notificationMessage.getTemplateMap().put("messageUrl", urlBuilder.setAction("message")
																		.setEntity(message)
																		.setCompany(remoteOrg.getTenant())
																		.build());
		return notificationMessage;
	}

	private void guard() {
		if (command == null) { 
			throw new InvalidArgumentException("you must give a command to be created against the message.");
		}
		
		if (remoteOrg == null) {
			throw new InvalidArgumentException("You must give an org to send the message to");
		}
		
		if (localOrg == null) {
			throw new InvalidArgumentException("You must give an org to send the message from");
		}
	}



	private Message createMessage() {
		Message message = new Message();
		
		message.setSender(localOrg);
		message.setReceiver(remoteOrg);
		message.setSubject(messageSubject);
		
		message.setBody(createMessageBody());
		
		message.setCommand(command);
		
		return message;
	}

	private String createMessageBody() {
		return (personalizedBody != null) ? personalizedBody : defaultBody;
	}
	
	private void saveMessage(Transaction transaction, Message message) {
		saver.save(transaction, message);
	}

	public ConnectionInvitationHandlerImpl withCommand(MessageCommand command) {
		this.command = command;
		return this;
	}

	public ConnectionInvitationHandlerImpl from(InternalOrg localOrg) {
		this.localOrg = localOrg; 
		return this;
	}
	
	public ConnectionInvitationHandlerImpl to(InternalOrg remoteOrg) {
		this.remoteOrg = remoteOrg; 
		return this;
	}

	public ConnectionInvitationHandlerImpl personalizeBody(String personalizedBody) {
		this.personalizedBody = personalizedBody;
		return this;
	}

	public boolean wasNotificationSent() {
		return notificationSent;
	}
	
	

}
