package com.n4systems.model.builders;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.BaseOrg;

public class UserBuilder extends BaseLegacyBuilder<UserBean> {

	private final BaseOrg owner;
	private final String firstName;
	private final String userId;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some name", "user_id");
	}
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some name", "user_id");
	}
	
	public UserBuilder(BaseOrg owner, String firstName, String userId) {
		super();
		this.owner = owner;
		this.firstName = firstName;
		this.userId = userId;
	}
	
	public UserBuilder withOwner(BaseOrg baseOrg) {
		return new UserBuilder(baseOrg, firstName, userId);
	}
	
	public UserBuilder withFirstName(String firstName) {
		return new UserBuilder(owner, firstName, userId);
	}
	
	public UserBuilder withUserId(String userId) {
		return new UserBuilder(owner, firstName, userId);
	}
	
	@Override
	public UserBean build() {
		UserBean user = new UserBean();
		user.setUniqueID(uniqueId);
		user.setFirstName(firstName);
		user.setUserID(userId);
		user.setTenant(owner.getTenant());
		user.setOwner(owner);
		return user;
	}
}
