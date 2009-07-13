package com.n4systems.model.builders;

import static com.n4systems.model.builders.TenantBuilder.*;
import rfid.ejb.entity.UserBean;

import com.n4systems.model.TenantOrganization;

public class UserBuilder extends BaseLegacyBuilder<UserBean> {

	private final TenantOrganization tenantOrganization;
	private final Long customer;
	
	public static UserBuilder aUser() {
		return new UserBuilder(aTenant().build());
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(aTenant().build(), null);
	}
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(aTenant().build(), 1L);
	}
	
	public UserBuilder(TenantOrganization tenantOrganization) {
		this(tenantOrganization, null);
	}
	
	public UserBuilder(TenantOrganization tenantOrganization, Long customer) {
		super();
		this.tenantOrganization = tenantOrganization;
		this.customer = customer;
	}
	
	@Override
	public UserBean build() {
		UserBean user = new UserBean();
		user.setUniqueID(uniqueId);
		user.setTenant(tenantOrganization);
		user.setR_EndUser(customer);
		return user;
	}
}
