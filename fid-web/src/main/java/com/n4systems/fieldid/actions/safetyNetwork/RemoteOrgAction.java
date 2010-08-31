package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Tenant;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ConfigEntry;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class RemoteOrgAction extends AbstractAction {

	private String name;
	private Tenant tenant;
	
	
	public RemoteOrgAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		tenant = TenantCache.getInstance().findTenant(getName().trim());
		
		if (tenant != null) {
			if (tenant.getName().equalsIgnoreCase(getConfigContext().getString(ConfigEntry.HOUSE_ACCOUNT_NAME))) {
				return ERROR;
			}
			
			if (tenant.equals(getPrimaryOrg().getTenant())) {
				return ERROR;
			}
			
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
