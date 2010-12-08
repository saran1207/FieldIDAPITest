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
	private final boolean employee;
	private final int permissions;
	
	public static UserBuilder aUser() {
		return anEmployee();
	}
	
	public static UserBuilder anEmployee() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null, true, 0);
	}
	
	public static UserBuilder aSystemUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", false, "user@example.com", true, null, false, null, false, 0);
	}
	
	public static UserBuilder anAdminUser() {
		return anEmployee().withAdministratorAccess();
	}
	
	public static UserBuilder aSecondaryUser() {
		return new UserBuilder(OrgBuilder.aSecondaryOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null, false, 0);
	}
	
	public static UserBuilder aReadOnlyUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null, false, 0);
	}
	
	public static UserBuilder aDivisionUser() {
		return new UserBuilder(OrgBuilder.aDivisionOrg().build(), "some first", "last name", "user_id", false, "user@example.com", false, null, false, null, false, 0);
	}

	private UserBuilder(BaseOrg owner, String firstName, String lastName, String userId, boolean administratorAccess, 
			String emailAddress, boolean systemAccess, String password, boolean resetPasswordKey, Long id, boolean employee,
			int permissions) {
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
		this.employee = employee;
		this.permissions = permissions;
	}
	
	public UserBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}
	
	public UserBuilder withFirstName(String firstName) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}
	
	public UserBuilder withLastName(String lastName) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}
	
	public UserBuilder withUserId(String userId) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}
	
	public UserBuilder withAdministratorAccess() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, true, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}
	
	public UserBuilder withEmailAddress(String emailAddress) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}
	
	public UserBuilder withNoPassword() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, null, resetPasswordKey, getId(), employee, permissions));
	}
	public UserBuilder withPassword(String password) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, resetPasswordKey, getId(), employee, permissions));
	}

	public UserBuilder withResetPasswordKey() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, true, getId(), employee, permissions));
	}

	public UserBuilder withOutResetPasswordKey() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, false, getId(), employee, permissions));
	}
	
	public UserBuilder withId(long id) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, false, id, employee, permissions));
	}
	
	public UserBuilder withPermissions(int permissions) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, administratorAccess, emailAddress, false, password, false, getId(), employee, permissions));
	}
	
	@Override
	public User createObject() {
		User user = new User();
		user.setId(getId());
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserID(userId);
		user.setTenant(owner.getTenant());
		user.setOwner(owner);
		user.setTimeZoneID("Canada:Ontario - Toronto");
		user.setAdmin(administratorAccess);
		user.setEmployee(employee);
		user.setActive(true);
		
		if (administratorAccess) {
			user.setPermissions(Permissions.ADMIN);
			user.setAdmin(administratorAccess);
		} else if (systemAccess) {
			user.setPermissions(Permissions.SYSTEM);
		} else {
			user.setPermissions(permissions);
		}
		user.setSystem(systemAccess);
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
