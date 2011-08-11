package com.n4systems.fieldid.wicket.model.admin.tenants;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;

import java.io.Serializable;

public class AddTenantModel implements Serializable {
	private Tenant tenant = new Tenant();
	private User adminUser = new User();
	private PrimaryOrg primaryOrg = new PrimaryOrg();

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public User getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(User adminUser) {
		this.adminUser = adminUser;
	}

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

}
