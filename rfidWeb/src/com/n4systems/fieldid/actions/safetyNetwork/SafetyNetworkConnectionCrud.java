package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.ConnectionListLoader;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;

public class SafetyNetworkConnectionCrud extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	public enum ConnectionType { 
		VENDOR("label.connection_type.vendor"),
		CUSTOMER("label.connection_type.customer");
		
		public final String label;
		
		ConnectionType(String label) {
			this.label = label;
		}
	};
	
	
	private List<OrgConnection> vendorConnections;
	private List<OrgConnection> customerConnections;
	

	public SafetyNetworkConnectionCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
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
