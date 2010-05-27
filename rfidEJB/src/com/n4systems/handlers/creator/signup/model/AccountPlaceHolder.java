package com.n4systems.handlers.creator.signup.model;


import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;

public class AccountPlaceHolder {

	private final Tenant tenant;
	private final PrimaryOrg primaryOrg;
	private final User systemUser;
	private final User adminUser;
	
	public AccountPlaceHolder(Tenant tenant, PrimaryOrg primaryOrg, User systemUser, User adminUser) {
		super();
		this.tenant = tenant;
		this.primaryOrg = primaryOrg;
		this.systemUser = systemUser;
		this.adminUser = adminUser;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public User getSystemUser() {
		return systemUser;
	}

	public User getAdminUser() {
		return adminUser;
	}
	
	
	
}
