package com.n4systems.model.builders;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;

public class UserBuilder extends BaseLegacyBuilder<UserBean> {

	private final BaseOrg owner;
	private final String firstName;
	private final String userId;
	private final boolean administratorAccess;
	private final String emailAddress;
	private final boolean systemAccess;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some name", "user_id", false, "user@example.com", false);
	}
	
	public static UserBuilder aSystemUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some name", "user_id", false, "user@example.com", true);
	}
	
	public static UserBuilder anAdminUser() {
		return anEmployee().withAdministratorAccess();
	}
	
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some name", "user_id", false, "user@example.com", false);
	}
	
	private UserBuilder(BaseOrg owner, String firstName, String userId, boolean administratorAccess, String emailAddress, boolean systemAccess) {
		super();
		this.owner = owner;
		this.firstName = firstName;
		this.userId = userId;
		this.administratorAccess = administratorAccess;
		this.emailAddress = emailAddress;
		this.systemAccess = systemAccess;
	}
	
	public UserBuilder withOwner(BaseOrg baseOrg) {
		return new UserBuilder(baseOrg, firstName, userId, administratorAccess, emailAddress, false);
	}
	
	public UserBuilder withFirstName(String firstName) {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false);
	}
	
	public UserBuilder withUserId(String userId) {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false);
	}
	
	public UserBuilder withAdministratorAccess() {
		return new UserBuilder(owner, firstName, userId, true, emailAddress, false);
	}
	
	public UserBuilder withEmailAddress(String emailAddress) {
		return  new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false);
	}
	
	@Override
	public UserBean build() {
		UserBean user = new UserBean();
		user.setUniqueID(uniqueId);
		user.setFirstName(firstName);
		user.setUserID(userId);
		user.setTenant(owner.getTenant());
		user.setOwner(owner);
		user.setTimeZoneID("Canada:Ontario - Toronto");
		user.setAdmin(administratorAccess);
		if (administratorAccess) {
			user.setPermissions(Permissions.ADMIN);
		}
		user.setSystem(systemAccess);
		if (systemAccess) {
			user.setPermissions(Permissions.SYSTEM);
		}
		user.setEmailAddress(emailAddress);
		return user;
	}

	

	
}
