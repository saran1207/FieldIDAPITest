package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.safetynetwork.ConnectionInvitationHandler;
import com.n4systems.handlers.creator.safetynetwork.CreateConnectionHandler;
import com.n4systems.model.Tenant;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.InternalOrgListableLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgListableLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantFinder;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.uri.ActionURLBuilder;


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
		Message message = createMessage();
		if (remoteOrg.getPrimaryOrg().isAutoAcceptConnections()) {
			autoAcceptConnection(transaction, message);
		} else {
			sendInvitationMessage(transaction, message);
		}
	}

	private Message createMessage() {
		Message message = new Message();
		
		message.setSender(getPrimaryOrg());
		message.setRecipient(remoteOrg);
		message.setVendorConnection(connectionType.equals(ConnectionType.VENDOR));
		message.setCreatedBy(fetchCurrentUser());
		message.setSubject(getDefaultMessageSubject());
		message.setBody(getBody());
		
		return message;
	}

	private String getBody() {
		return getPersonalizedBody().trim().isEmpty() ? getDefautMessageBody() : getPersonalizedBody();
	}

	private void autoAcceptConnection(Transaction transaction, Message message) {
		OrgConnectionSaver saver = new OrgConnectionSaver(getConfigContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
		CreateConnectionHandler handler = new CreateConnectionHandler(saver, getDefaultNotifier(), getNonSecureLoaderFactory());
		
		handler.withMessage(message).create(transaction);
	}

	private void sendInvitationMessage(Transaction transaction, Message message) {
		ConnectionInvitationHandler connectionCreator = new ConnectionInvitationHandler(new MessageSaver(),	getDefaultNotifier(), 
				new AdminUserListLoader(new TenantOnlySecurityFilter(remoteOrg.getTenant())), new ActionURLBuilder(getBaseURI(), getConfigContext()));
		
		connectionCreator.withMessage(message).create(transaction);
	}

	public String getDefaultMessageSubject() {
		return getText("label.invite_connection_subject.default");
	}
	
	private String getDefautMessageBody() {
		return getText("label.invite_connection_body.default", new String[]{getSessionUser().getName()});
	}

	public String getConnectionType() {
		return connectionType.name();
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = ConnectionType.valueOf(connectionType.toUpperCase());
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
	
	public PrimaryOrg getRemoteOrg(){
		return remoteOrg;
	}

	public void setRemoteOrgId(Long id) {
		if (id == null) {
			remoteOrg = null;
		} else if (remoteOrg == null || !remoteOrg.getId().equals(id)) {
			remoteOrg = (PrimaryOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(PrimaryOrg.class).setId(id).load();
		}
	}

	public PrimaryOrg getRemoteOrg(Long id){
		if (id == null) {
			return null;
		} else  {
			return (PrimaryOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(PrimaryOrg.class).setId(id).load();
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
			tenants = ListHelper.longListableToListingPair(TenantFinder.getInstance().findAllTenants());
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
			remoteTenant = TenantFinder.getInstance().findTenant(id);
		}
	}

	public String getPersonalizedBody() {
		return personalizedBody;
	}

	public void setPersonalizedBody(String personalizedBody) {
		this.personalizedBody = personalizedBody;
	}
	


}
