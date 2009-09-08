package com.n4systems.model.builders;

import static com.n4systems.model.builders.TenantBuilder.*;
import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

public class UserBuilder extends BaseLegacyBuilder<UserBean> {

	private final Tenant tenantOrganization;
	private final BaseOrg owner;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(aTenant().build(), OrgBuilder.aPrimaryOrg().build());
	}
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(aTenant().build(), OrgBuilder.aCustomerOrg().build());
	}
	
	public UserBuilder(Tenant tenantOrganization) {
		this(tenantOrganization, null);
	}
	
	public UserBuilder(Tenant tenantOrganization, BaseOrg owner) {
		super();
		this.tenantOrganization = tenantOrganization;
		this.owner = owner;
	}
	
	@Override
	public UserBean build() {
		UserBean user = new UserBean();
		user.setUniqueID(uniqueId);
		user.setTenant(tenantOrganization);
		user.setOwner(owner);
		return user;
	}
}
