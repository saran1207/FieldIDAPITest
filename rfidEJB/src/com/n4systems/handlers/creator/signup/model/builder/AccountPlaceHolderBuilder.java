package com.n4systems.handlers.creator.signup.model.builder;

import static com.n4systems.model.builders.TenantBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;


import rfid.ejb.entity.UserBean;


import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public class AccountPlaceHolderBuilder {

	public static AccountPlaceHolderBuilder anAccountPlaceHolder() {
		return new AccountPlaceHolderBuilder();
	}
	
	public AccountPlaceHolder build() {
		Tenant tenant = aTenant().build();
		PrimaryOrg primaryOrg = aPrimaryOrg().onTenant(tenant).build();
		UserBean systemUser = anEmployee().build();
		UserBean adminUser = anEmployee().build();
		return new AccountPlaceHolder(tenant, primaryOrg, systemUser, adminUser);
	}
}
