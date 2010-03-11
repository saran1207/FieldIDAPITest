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
	private final String password;
	private final boolean resetPasswordKey;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some name", "user_id", false, "user@example.com", false, null, false);
	}
	
	public static UserBuilder aSystemUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some name", "user_id", false, "user@example.com", true, null, false);
	}
	
	public static UserBuilder anAdminUser() {
		return anEmployee().withAdministratorAccess();
	}
	
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some name", "user_id", false, "user@example.com", false, null, false);
	}
	
	private UserBuilder(BaseOrg owner, String firstName, String userId, boolean administratorAccess, String emailAddress, boolean systemAccess, String password, boolean resetPasswordKey) {
		super();
		this.owner = owner;
		this.firstName = firstName;
		this.userId = userId;
		this.administratorAccess = administratorAccess;
		this.emailAddress = emailAddress;
		this.systemAccess = systemAccess;
		this.password = password;
		this.resetPasswordKey = resetPasswordKey;
	}
	
	public UserBuilder withOwner(BaseOrg baseOrg) {
		return new UserBuilder(baseOrg, firstName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey);
	}
	
	public UserBuilder withFirstName(String firstName) {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey);
	}
	
	public UserBuilder withUserId(String userId) {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey);
	}
	
	public UserBuilder withAdministratorAccess() {
		return new UserBuilder(owner, firstName, userId, true, emailAddress, false, password, resetPasswordKey);
	}
	
	public UserBuilder withEmailAddress(String emailAddress) {
		return  new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey);
	}
	
	public UserBuilder withNoPassword() {
		return  new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, null, resetPasswordKey);
	}
	public UserBuilder withPassword(String password) {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey);
	}

	public UserBuilder withResetPasswordKey() {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, password, true);
	}

	public UserBuilder withOutResetPasswordKey() {
		return new UserBuilder(owner, firstName, userId, administratorAccess, emailAddress, false, password, false);
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
		if (password != null) {
			user.assignPassword(password);
		}
		
		if (resetPasswordKey) {
			user.createResetPasswordKey();
		} else {
			user.clearResetPasswordKey();
		}
		
		return user;
	}

	

	
}
