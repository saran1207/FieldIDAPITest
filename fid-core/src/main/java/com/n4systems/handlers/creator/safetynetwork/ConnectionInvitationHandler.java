package com.n4systems.handlers.creator.safetynetwork;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.CreateHandler;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.uri.ActionURLBuilder;

public class ConnectionInvitationHandler implements CreateHandler {

	private final MessageSaver saver;
	private final Notifier notifier;
	private final AdminUserListLoader adminLoader;
	private final ActionURLBuilder urlBuilder;
	
	private boolean notificationSent = false;
	
	private Message message;
	
	public ConnectionInvitationHandler(MessageSaver saver, Notifier notifier, AdminUserListLoader adminLoader, ActionURLBuilder urlBuilder) {
		this.saver = saver;
		this.notifier = notifier;
		this.adminLoader = adminLoader;
		this.urlBuilder = urlBuilder;
	}

	@Override
	public void create(Transaction transaction) {
		guard();
		saveMessage(transaction, message);
		
		sendNotification(transaction, message);
	}

	private void guard() {
		if (message == null) { 
			throw new InvalidArgumentException("you must give a message to create an invitation.");
		}
	}

	private void saveMessage(Transaction transaction, Message message) {
		saver.save(transaction, message);
	}
	
	
	private void sendNotification(Transaction transaction, Message message) {
		notificationSent = notifier.notify(createNotification(transaction, message));
	}

	private Notification createNotification(Transaction transaction, Message message) {
		ConnectionInviteReceivedNotification notification = new ConnectionInviteReceivedNotification();
		notification.setSubject(message.getSubject());
		notification.setCompanyName(message.getSender().getName());
		notification.setMessage(message.getBody());
		notification.setMessageUrl(urlBuilder.setAction("message")
				.setEntity(message)
				.setCompany(message.getRecipient().getTenant())
				.build());
		notification.notifiyUser(adminLoader.load(transaction).get(0));
		
		return notification;
	}

	public ConnectionInvitationHandler withMessage(Message message) {
		this.message = message;
		
		return this;
	}

	public boolean wasNotificationSent() {
		return notificationSent;
	}	

}
