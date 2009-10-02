package com.n4systems.commandprocessors;


import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class CreateSafetyNetworkConnectionCommandProcessor extends CommandProcessor<CreateSafetyNetworkConnectionMessageCommand> {
	
	public CreateSafetyNetworkConnectionCommandProcessor() {
		super(CreateSafetyNetworkConnectionMessageCommand.class);
	}
	
	protected void execute(CreateSafetyNetworkConnectionMessageCommand command) {
		OrgConnection connection = buildConnection(command);
		saveConnection(connection);
	}

	
	
	private void saveConnection(OrgConnection connection) {
		OrgConnectionSaver saver = new OrgConnectionSaver(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_ID));
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
}
