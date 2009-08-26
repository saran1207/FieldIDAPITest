package com.n4systems.handlers.creator.signup.model;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public class AccountPlaceHolder {

	private final Tenant tenant;
	private final PrimaryOrg primaryOrg;
	private final UserBean systemUser;
	private final UserBean adminUser;
	
	public AccountPlaceHolder(Tenant tenant, PrimaryOrg primaryOrg, UserBean systemUser, UserBean adminUser) {
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

	public UserBean getSystemUser() {
		return systemUser;
	}

	public UserBean getAdminUser() {
		return adminUser;
	}
	
	
	
}
