package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.message.MessageDecorator;
import com.n4systems.model.Tenant;
import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageCommandSaver;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.InternalOrgListableLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class ConnectionInvitationAction extends AbstractAction {
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
	private InternalOrg localOrg;
	private InternalOrg remoteOrg;
	private ConnectionType connectionType = ConnectionType.VENDOR;
	
	private MessageDecorator message = new MessageDecorator(new Message());
	
	
	public ConnectionInvitationAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	

	@SkipValidation
	public String doRemoteOrgs() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		setRemoteTenantId(getTenants().get(0).getId());
		return SUCCESS;
	}
	
	public String doCreate() {
		try {
			CreateSafetyNetworkConnectionMessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
			
			switch (connectionType) {
				case CUSTOMER:
					command.setCustomerOrgId(remoteOrg.getId());
					command.setVendorOrgId(localOrg.getId());
					break;
				case VENDOR:
					command.setCustomerOrgId(localOrg.getId());
					command.setVendorOrgId(remoteOrg.getId());
					break;
			}
			
			new MessageCommandSaver().save(command);
						
			Message realMessage = message.realMessage();
				
			realMessage.setSender(localOrg);
			realMessage.setReceiver(remoteOrg);
				
			realMessage.setCommand(command);
				
			new MessageSaver().save(realMessage);
						
			addActionMessageText("message.invitation_sent");
		} catch(Exception e) {
			logger.error("Failed while sending OrgConnection request", e);
			addActionErrorText("error.failed_send_invitation");
			
			return ERROR;
		}
		
		return SUCCESS;
	}
	


	public String getConnectionType() {
		return connectionType.name();
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = ConnectionType.valueOf(connectionType);
	}
	
	public InternalOrg getLocalOrg() {
		return localOrg;
	}
	
	public Long getLocalOrgId() {
		return (localOrg != null) ? localOrg.getId() : null;
	}

	public void setLocalOrgId(Long id) {
		if (id == null) {
			localOrg = null;
		} else if (localOrg == null || !localOrg.getId().equals(id)) {
			localOrg = (InternalOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(BaseOrg.class).setId(id).load();
		}
	}
	
	public Long getRemoteOrgId() {
		return (remoteOrg != null) ? remoteOrg.getId() : null;
	}

	public void setRemoteOrgId(Long id) {
		if (id == null) {
			remoteOrg = null;
		} else if (remoteOrg == null || !remoteOrg.getId().equals(id)) {
			remoteOrg = (InternalOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(BaseOrg.class).setId(id).load();
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
		return new InternalOrgListableLoader(new TenantOnlySecurityFilter(getRemoteTenantId()));
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
	

	@VisitorFieldValidator(message="")
	public MessageDecorator getMessage() {
		return message;
	}

}
