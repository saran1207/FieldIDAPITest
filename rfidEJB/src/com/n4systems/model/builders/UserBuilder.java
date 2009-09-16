package com.n4systems.model.builders;

import static com.n4systems.model.builders.TenantBuilder.*;
import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

public class UserBuilder extends BaseLegacyBuilder<UserBean> {

	private final Tenant tenantOrganization;
	private final BaseOrg owner;
	private final String firstName;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(aTenant().build(), OrgBuilder.aPrimaryOrg().build(), "some name");
	}
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(aTenant().build(), OrgBuilder.aCustomerOrg().build(), "some name");
	}
	
	public UserBuilder(Tenant tenantOrganization) {
		this(tenantOrganization, null, "some name");
	}
	
	public UserBuilder(Tenant tenantOrganization, BaseOrg owner, String firstName) {
		super();
		this.tenantOrganization = tenantOrganization;
		this.owner = owner;
		this.firstName = firstName;
	}
	
	public UserBuilder withOwner(BaseOrg baseOrg) {
		return new UserBuilder(tenantOrganization, baseOrg, firstName);
	}
	
	public UserBuilder withFirstName(String firstName) {
		return new UserBuilder(tenantOrganization, owner, firstName);
	}
	
	
	@Override
	public UserBean build() {
		UserBean user = new UserBean();
		user.setUniqueID(uniqueId);
		user.setFirstName(firstName);
		user.setTenant(tenantOrganization);
		user.setOwner(owner);
		return user;
	}
}
