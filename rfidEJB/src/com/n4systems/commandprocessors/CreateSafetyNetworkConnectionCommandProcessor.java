package com.n4systems.commandprocessors;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.messages.MessageCommandSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class CreateSafetyNetworkConnectionCommandProcessor {

	private UserBean actor;
	
	private NonSecureLoaderFactory nonSecureLoaderFactory;
	
	private Transaction transaction;

	public void process(CreateSafetyNetworkConnectionMessageCommand command, Transaction transaction) {
		this.transaction = transaction;
		OrgConnection connection = buildConnection(command);
		
		saveConnection(connection);
		
		markCommandProcessed(command);
	}
	
	private void saveConnection(OrgConnection connection) {
		OrgConnectionSaver saver = new OrgConnectionSaver(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_ID));
		saver.save(transaction, connection);
	}

	private void markCommandProcessed(CreateSafetyNetworkConnectionMessageCommand command) {
		command.setProcessed(true);
		new MessageCommandSaver().update(transaction, command);
	}

	private OrgConnection buildConnection(CreateSafetyNetworkConnectionMessageCommand command) {
		OrgConnection connection = new OrgConnection();
		connection.setModifiedBy(actor);
		
		connection.setVendor(fetchOrg(command.getVendorOrgId()));
		connection.setCustomer(fetchOrg(command.getCustomerOrgId()));
		
		return connection;
	}

	private InternalOrg fetchOrg(Long orgId) {
		return (InternalOrg)nonSecureLoaderFactory.createNonSecureIdLoader(BaseOrg.class).setId(orgId).load(transaction);
	}

	
	public CreateSafetyNetworkConnectionCommandProcessor setActor(UserBean actor) {
		this.actor = actor;
		return this;
	}



	public CreateSafetyNetworkConnectionCommandProcessor setNonSecureLoaderFactory(NonSecureLoaderFactory nonSecureLoaderFactory) {
		this.nonSecureLoaderFactory = nonSecureLoaderFactory;
		return this;
	}
}
