package com.n4systems.commandprocessors;


import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class CreateSafetyNetworkConnectionCommandProcessor extends CommandProcessor<CreateSafetyNetworkConnectionMessageCommand> {
	
	private final ConfigContext currentContext;
	
	public CreateSafetyNetworkConnectionCommandProcessor(ConfigContext currentContext) {
		super(CreateSafetyNetworkConnectionMessageCommand.class);
		this.currentContext = currentContext;
	}
	
	@Override
	protected void execute(CreateSafetyNetworkConnectionMessageCommand command) {
		if (isCommandStillValid(command)) {
			OrgConnection connection = buildConnection(command);
			saveConnection(connection);
		}
	}

	
	private void saveConnection(OrgConnection connection) {
		OrgConnectionSaver saver = new OrgConnectionSaver(currentContext.getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
		saver.save(transaction, connection);
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

	@Override
	protected boolean isCommandStillValid(CreateSafetyNetworkConnectionMessageCommand command) {
		OrgConnectionExistsLoader orgConnectionExistsLoader = nonSecureLoaderFactory.createOrgConnectionExistsLoader();
		orgConnectionExistsLoader.setCustomerId(command.getCustomerOrgId()).setVendorId(command.getVendorOrgId());
		
		return !orgConnectionExistsLoader.load(transaction);
	}
}
