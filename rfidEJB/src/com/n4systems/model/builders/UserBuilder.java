package com.n4systems.model.builders;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.BaseOrg;

public class UserBuilder extends BaseLegacyBuilder<UserBean> {

	private final BaseOrg owner;
	private final String firstName;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some name");
	}
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some name");
	}
	
	
	
	public UserBuilder(BaseOrg owner, String firstName) {
		super();
		this.owner = owner;
		this.firstName = firstName;
	}
	
	public UserBuilder withOwner(BaseOrg baseOrg) {
		return new UserBuilder(baseOrg, firstName);
	}
	
	public UserBuilder withFirstName(String firstName) {
		return new UserBuilder(owner, firstName);
	}
	
	
	@Override
	public UserBean build() {
		UserBean user = new UserBean();
		user.setUniqueID(uniqueId);
		user.setFirstName(firstName);
		user.setTenant(owner.getTenant());
		user.setOwner(owner);
		return user;
	}
}
