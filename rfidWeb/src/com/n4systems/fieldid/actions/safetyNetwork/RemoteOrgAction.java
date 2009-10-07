package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Tenant;
import com.n4systems.services.TenantCache;

public class RemoteOrgAction extends AbstractAction {

	private String name;
	private Tenant tenant;
	
	
	public RemoteOrgAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		tenant = TenantCache.getInstance().findTenant(getName().trim());
		
		if (tenant != null) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public String getName() {
		return (name != null) ? name : "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tenant getTenant() {
		return tenant;
	}

}
