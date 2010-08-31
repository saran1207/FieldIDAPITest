package com.n4systems.handlers.creator.safetynetwork;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageCommand;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.uri.ActionURLBuilder;

public class ConnectionInvitationHandlerImpl implements ConnectionInvitationHandler {

	private final MessageSaver saver;
	private final Notifier notifier;
	private final String messageSubject;
	private final AdminUserListLoader adminLoader;
	private final ActionURLBuilder urlBuilder;
	
	private MessageCommand command;
	private InternalOrg localOrg;
	private InternalOrg remoteOrg;
	private String personalizedBody;
	private boolean notificationSent = false;
	
	
	public ConnectionInvitationHandlerImpl(MessageSaver saver, Notifier notifier, String subject, AdminUserListLoader adminLoader, ActionURLBuilder urlBuilder) {
		this.saver = saver;
		this.messageSubject = subject;
		this.notifier = notifier;
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
		message.setBody(personalizedBody);
		message.setCommand(command);
		
		return message;
	}

	
	private void saveMessage(Transaction transaction, Message message) {
		saver.save(transaction, message);
	}
	
	
	private void sendNotification(Transaction transaction, Message message) {
		notificationSent = notifier.notify(createNotification(transaction, message));
	}

	private Notification createNotification(Transaction transaction, Message message) {
		ConnectionInviteReceivedNotification notification = new ConnectionInviteReceivedNotification();
		notification.setSubject(messageSubject);
		notification.setCompanyName(localOrg.getName());
		notification.setMessage(personalizedBody);
		notification.setMessageUrl(urlBuilder.setAction("message")
				.setEntity(message)
				.setCompany(remoteOrg.getTenant())
				.build());
		notification.notifiyUser(adminLoader.load(transaction).get(0));
		
		return notification;
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
