package com.n4systems.handlers.creator.safetynetwork;

import com.n4systems.handlers.creator.CreateHandler;
import com.n4systems.model.messages.Message;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.ConnectionInvitationAcceptedNotification;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class CreateConnectionHandler implements CreateHandler{
	
	private final ConfigContext currentContext;
	private final Notifier notifier;
	private NonSecureLoaderFactory nonSecureLoaderFactory;
	private Message message;
	private PrimaryOrg customer;
	private PrimaryOrg vendor;
	
	public CreateConnectionHandler(ConfigContext currentContext, 
			Notifier notifier, NonSecureLoaderFactory nonSecureLoaderFactory) {
		this.currentContext = currentContext;
		this.notifier = notifier;
		this.nonSecureLoaderFactory = nonSecureLoaderFactory;
	}

	@Override
	public void create(Transaction transaction) {
		if (!connectionExists(transaction)) {
			OrgConnection connection = buildConnection();
			saveConnection(connection, transaction);
			sendNotification(connection);
		}
	}

	private boolean connectionExists(Transaction transaction) {
		OrgConnectionExistsLoader orgConnectionExistsLoader = nonSecureLoaderFactory.createOrgConnectionExistsLoader();
			orgConnectionExistsLoader.setCustomerId(customer.getId()).setVendorId(vendor.getId());
		return orgConnectionExistsLoader.load(transaction);
	}

	private OrgConnection buildConnection() {
		OrgConnection connection = new OrgConnection(vendor, customer);
		connection.setModifiedBy(message.getModifiedBy());
		return connection;
	}

	private void saveConnection(OrgConnection connection, Transaction transaction) {
		OrgConnectionSaver saver = new OrgConnectionSaver(currentContext.getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
		saver.save(transaction, connection);
	}

	private void sendNotification(OrgConnection orgConnection) {
		ConnectionInvitationAcceptedNotification notification = new ConnectionInvitationAcceptedNotification();
		notification.notifiyUser(message.getCreatedBy());
		notification.setAcceptingCompanyName(message.getRecipient().getName());
		notifier.notify(notification);
	}

	public CreateConnectionHandler withMessage(Message message) {
		this.message = message;
		if(message.isVendorConnection()) {
			this.customer = message.getSender();
			this.vendor = message.getRecipient();
		}else {
			this.customer = message.getRecipient();
			this.vendor = message.getSender();
		}
		return this;
	}

}
