package com.n4systems.fieldid.actions.safetyNetwork;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import com.n4systems.tools.Pager;

public class SafetyNetworkConnectionCrud extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	public SafetyNetworkConnectionCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	
	public Pager<TypedOrgConnection> getConnections() {
		return getLoaderFactory().createPaginatedConnectionListLoader().load();
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
