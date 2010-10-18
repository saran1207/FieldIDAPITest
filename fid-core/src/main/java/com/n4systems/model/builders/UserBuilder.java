package com.n4systems.model.builders;


import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class UserBuilder extends BaseBuilder<User> {

	private final BaseOrg owner;
	private final String firstName;
	private final String lastName;
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
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null);
	}
	
	public static UserBuilder aSystemUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", false, "user@example.com", true, null, false, null);
	}
	
	public static UserBuilder anAdminUser() {
		return anEmployee().withAdministratorAccess();
	}
	
	public static UserBuilder aSecondaryUser() {
		return new UserBuilder(OrgBuilder.aSecondaryOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null);
	}
	
	public static UserBuilder aCustomerUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null);
	}
	
	public static UserBuilder aDivisionUser() {
		return new UserBuilder(OrgBuilder.aDivisionOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null);
	}
	
	private UserBuilder(BaseOrg owner, String firstName, String lastName, String userId, boolean administratorAccess, String emailAddress, boolean systemAccess, String password, boolean resetPasswordKey, Long id) {
		super(id);
		this.owner = owner;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.administratorAccess = administratorAccess;
		this.emailAddress = emailAddress;
		this.systemAccess = systemAccess;
		this.password = password;
		this.resetPasswordKey = resetPasswordKey;
	}
	
	public UserBuilder withOwner(BaseOrg baseOrg) {
		return new UserBuilder(baseOrg, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, id);
	}
	
	public UserBuilder withFirstName(String firstName) {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, id);
	}
	
	public UserBuilder withLastName(String lastName) {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, id);
	}
	
	public UserBuilder withUserId(String userId) {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, id);
	}
	
	public UserBuilder withAdministratorAccess() {
		return new UserBuilder(owner, firstName, lastName, userId, true, emailAddress, false, password, resetPasswordKey, id);
	}
	
	public UserBuilder withEmailAddress(String emailAddress) {
		return  new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, id);
	}
	
	public UserBuilder withNoPassword() {
		return  new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, null, resetPasswordKey, id);
	}
	public UserBuilder withPassword(String password) {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, id);
	}

	public UserBuilder withResetPasswordKey() {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, true, id);
	}

	public UserBuilder withOutResetPasswordKey() {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, false, id);
	}
	
	public UserBuilder withId(long id) {
		return new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, false, id);
	}
	
	@Override
	public User createObject() {
		User user = new User();
		user.setId(id);
		user.setFirstName(firstName);
		user.setLastName(lastName);
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
