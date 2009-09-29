package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.InternalOrgListableLoader;
import com.n4systems.model.safetynetwork.ConnectionListLoader;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.TenantCache;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;

public class TempLinkCreateAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TempLinkCreateAction.class);
	
	public enum ConnectionType { 
		VENDOR("label.connection_type.vendor"),
		CUSTOMER("label.connection_type.customer");
		
		public final String label;
		
		ConnectionType(String label) {
			this.label = label;
		}
	};
	
	private final OrgConnectionSaver saver;
	
	private List<OrgConnection> vendorConnections;
	private List<OrgConnection> customerConnections;
	private List<StringListingPair> connectionTypes;
	private List<ListingPair> tenants;
	private List<ListingPair> remoteOrgs;
	private Tenant remoteTenant;
	private InternalOrg localOrg;
	private InternalOrg remoteOrg;
	private ConnectionType connectionType = ConnectionType.VENDOR;
	
	public TempLinkCreateAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
		saver = new OrgConnectionSaver();
	}
	
	public String doList() {
		return SUCCESS;
	}
	
	public String doAdd() {
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			
			OrgConnection connection = new OrgConnection();
			connection.setModifiedBy(getUser());
			
			switch (connectionType) {
				case CUSTOMER:
					connection.setCustomer(remoteOrg);
					connection.setVendor(localOrg);
					break;
				case VENDOR:
					connection.setCustomer(localOrg);
					connection.setVendor(remoteOrg);
					break;
			}
			
			saver.save(connection);
			addActionMessageText("message.connection_created");
		} catch(RuntimeException e) {
			logger.error("Failed saving OrgConnection", e);
			addActionErrorText("error.failed_creating_connection");
			return ERROR;
		}
		
		return SUCCESS;
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

	public List<OrgConnection> getVendorConnections() {
		if (vendorConnections == null) {
			vendorConnections = getLoaderFactory().createVendorOrgConnectionsListLoader().load();
		}
		return vendorConnections;
	}

	public List<OrgConnection> getCustomerConnections() {
		if (customerConnections == null) {
			customerConnections = getLoaderFactory().createCustomerOrgConnectionsListLoader().load();
		}
		return customerConnections;
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

	public String getConnectionType() {
		return connectionType.name();
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = ConnectionType.valueOf(connectionType);
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
	
	public String getConnectionTypeLabel() {
		return connectionType.label;
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
	
	public Pager<BaseOrg> getConnections() {
		
			return new ConnectionListLoader(getSecurityFilter(), ConfigContext.getCurrentContext()).setPage(1).load();
	}
	
	public boolean hasAPublishedCatalog(BaseOrg org) {
		
		try {
			CatalogService catalogService = new CatalogServiceImpl(persistenceManager, org.getTenant());
			return catalogService.hasCatalog();
		} catch (Exception e) {
			
			return false;
		}
	}
	
}
