package com.n4systems.commandprocessors;


import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.ConnectionInvitationAcceptedNotification;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class CreateSafetyNetworkConnectionCommandProcessor extends CommandProcessor<CreateSafetyNetworkConnectionMessageCommand> {
	
	private final ConfigContext currentContext;
	private final Notifier notifier;
	
	public CreateSafetyNetworkConnectionCommandProcessor(ConfigContext currentContext, Notifier notifier) {
		super(CreateSafetyNetworkConnectionMessageCommand.class);
		this.currentContext = currentContext;
		this.notifier = notifier;
	}
	

	@Override
	protected void execute(CreateSafetyNetworkConnectionMessageCommand command) {
		if (isCommandStillValid(command)) {
			OrgConnection connection = buildConnection(command);
			saveConnection(connection);
			sendNotfication(command, connection);
		}
	}

	
	private void sendNotfication(CreateSafetyNetworkConnectionMessageCommand command, OrgConnection orgConnection) {
		ConnectionInvitationAcceptedNotification notification = new ConnectionInvitationAcceptedNotification();
		notification.notifiyUser(command.getCreatedBy());
		InternalOrg org = (command.getCreatedBy().getTenant().equals(orgConnection.getVendor().getTenant())) ? orgConnection.getCustomer() : orgConnection.getVendor();
		notification.setAcceptingCompanyName(org.getDisplayName());
		notifier.notify(notification);
	}


	private void saveConnection(OrgConnection connection) {
		OrgConnectionSaver saver = new OrgConnectionSaver(currentContext.getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
		saver.save(transaction, connection);
	}

	
	private OrgConnection buildConnection(CreateSafetyNetworkConnectionMessageCommand command) {
		PrimaryOrg vendor =  fetchOrg(command.getVendorOrgId());
		PrimaryOrg customer = fetchOrg(command.getCustomerOrgId());
		
		OrgConnection connection = new OrgConnection(vendor, customer);

		connection.setModifiedBy(actor);
		
		return connection;
	}

	private PrimaryOrg fetchOrg(Long orgId) {
		return (PrimaryOrg)nonSecureLoaderFactory.createNonSecureIdLoader(PrimaryOrg.class).setId(orgId).load(transaction);
	}

	@Override
	protected boolean isCommandStillValid(CreateSafetyNetworkConnectionMessageCommand command) {
		OrgConnectionExistsLoader orgConnectionExistsLoader = nonSecureLoaderFactory.createOrgConnectionExistsLoader();
		orgConnectionExistsLoader.setCustomerId(command.getCustomerOrgId()).setVendorId(command.getVendorOrgId());
		
		return !orgConnectionExistsLoader.load(transaction);
	}
}
