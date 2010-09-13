package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.commandprocessors.CreateSafetyNetworkConnectionCommandProcessor;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.safetynetwork.ConnectionInvitationHandlerImpl;
import com.n4systems.model.Tenant;
import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.messages.MessageCommandSaver;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.InternalOrgListableLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgListableLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.uri.ActionURLBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;


@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class ConnectionInvitationAction extends SafetyNetwork {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ConnectionInvitationAction.class);
	
	public enum ConnectionType { 
		VENDOR("label.connection_type.vendor"),
		CUSTOMER("label.connection_type.customer");
		
		public final String label;
		
		ConnectionType(String label) {
			this.label = label;
		}
	};
	
	private List<StringListingPair> connectionTypes;
	private List<ListingPair> tenants;
	private List<ListingPair> remoteOrgs;
	private Tenant remoteTenant;
	private PrimaryOrg remoteOrg;
	private ConnectionType connectionType = ConnectionType.VENDOR;

	private String personalizedBody;
	
	public ConnectionInvitationAction(com.n4systems.ejb.PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SkipValidation
	public String doRemoteOrgs() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		
		
		personalizedBody = getDefautMessageBody();
		
		return SUCCESS;
	}
	
	public String doCreate() {
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			invite(transaction);
			
			PersistenceManager.finishTransaction(transaction);
			addFlashMessageText("message.invitation_sent");
		} catch(Exception e) {
			PersistenceManager.rollbackTransaction(transaction);
			logger.error("Failed while sending OrgConnection request", e);
			addActionErrorText("error.failed_send_invitation");
			return ERROR;
		}
		
		return SUCCESS;
	}

	private void invite(Transaction transaction) {
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(transaction);
		if (remoteOrg.getPrimaryOrg().isAutoAcceptConnections()) {
			autoAcceptConnection(command, transaction);
		} else {
			
			sendInvitationMessage(command, transaction);
		}
	}

	private void autoAcceptConnection(CreateSafetyNetworkConnectionMessageCommand command, Transaction transaction) {
		CreateSafetyNetworkConnectionCommandProcessor processor = new CreateSafetyNetworkConnectionCommandProcessor(getConfigContext(), getDefaultNotifier());
		processor.setNonSecureLoaderFactory(getNonSecureLoaderFactory());
		processor.setActor(getUser());
		processor.process(command, transaction);
	}

	private void sendInvitationMessage(CreateSafetyNetworkConnectionMessageCommand command, Transaction transaction) {
		ConnectionInvitationHandlerImpl connectionCreator = new ConnectionInvitationHandlerImpl(new MessageSaver(), 
				getDefaultNotifier(), 
				getDefaultMessageSubject(), new AdminUserListLoader(new TenantOnlySecurityFilter(remoteOrg.getTenant())), new ActionURLBuilder(getBaseURI(), getConfigContext()));
		
		connectionCreator.withCommand(command).from(getPrimaryOrg()).to(remoteOrg).personalizeBody(personalizedBody);
		
		connectionCreator.create(transaction);
	}

	public String getDefaultMessageSubject() {
		return getText("label.invite_connection_subject.default");
	}

	private String getDefautMessageBody() {
		return getText("label.invite_connection_body.default", new String[]{getSessionUser().getName()});
	}

	private CreateSafetyNetworkConnectionMessageCommand createCommand(Transaction transaction) {
		CreateSafetyNetworkConnectionMessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
		
		switch (connectionType) {
			case CUSTOMER:
				command.setCustomerOrgId(remoteOrg.getId());
				command.setVendorOrgId(getPrimaryOrg().getId());
				break;
			case VENDOR:
				command.setCustomerOrgId(getPrimaryOrg().getId());
				command.setVendorOrgId(remoteOrg.getId());
				break;
		}
		command.setCreatedBy(fetchCurrentUser());
		
		new MessageCommandSaver().save(transaction, command);
		return command;
	}

	public String getConnectionType() {
		return connectionType.name();
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = ConnectionType.valueOf(connectionType);
	}
	
	public PrimaryOrg getLocalOrg() {
		return getPrimaryOrg();
	}
	
	public Long getLocalOrgId() {
		return getPrimaryOrg().getId();
	}


	
	public Long getRemoteOrgId() {
		return (remoteOrg != null) ? remoteOrg.getId() : null;
	}

	public void setRemoteOrgId(Long id) {
		if (id == null) {
			remoteOrg = null;
		} else if (remoteOrg == null || !remoteOrg.getId().equals(id)) {
			remoteOrg = (PrimaryOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(PrimaryOrg.class).setId(id).load();
		}
	}

	public PrimaryOrg getRemoteOrg(String id){
		if (id == null) {
			return null;
		} else  {
			return (PrimaryOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(PrimaryOrg.class).setId(Long.parseLong(id.trim())).load();
		}
	}
	
	public List<StringListingPair> getConnectionTypes() {
		if (connectionTypes == null) {
			connectionTypes = new ArrayList<StringListingPair>();
			for (ConnectionType type: ConnectionType.values()) {
				connectionTypes.add(new StringListingPair(type.name(), getText(type.label)));
			}
		}
		return connectionTypes;
	}
	
	protected InternalOrgListableLoader createRemoteOrgLoader() {
		return new PrimaryOrgListableLoader(new TenantOnlySecurityFilter(getRemoteTenantId()));
	}
	
	public List<ListingPair> getTenants() {
		if (tenants == null) {
			tenants = ListHelper.longListableToListingPair(TenantCache.getInstance().findAllTenants());
		}
		return tenants;
	}
	
	public List<ListingPair> getRemoteOrgs() {
		if (remoteOrgs == null) {
			remoteOrgs = ListHelper.longListableToListingPair(createRemoteOrgLoader().load());
		}
		return remoteOrgs;
	}
	
	public Long getRemoteTenantId() {
		return (remoteTenant != null) ? remoteTenant.getId() : null;
	}
	
	public void setRemoteTenantId(Long id) {
		if (id == null) {
			remoteTenant = null;
		} else if (remoteTenant == null || !remoteTenant.getId().equals(id)) {
			remoteTenant = TenantCache.getInstance().findTenant(id);
		}
	}

	public String getPersonalizedBody() {
		return personalizedBody;
	}

	@RequiredStringValidator(message="", key="error.body_is_required")
	public void setPersonalizedBody(String personalizedBody) {
		this.personalizedBody = personalizedBody;
	}
	


}
