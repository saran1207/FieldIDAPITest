package com.n4systems.model.builders;


import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.util.UserType;

public class UserBuilder extends BaseBuilder<User> {

	private final BaseOrg owner;
	private final String firstName;
	private final String lastName;
	private final String userId;
	private final String emailAddress;
	private final String password;
	private final boolean resetPasswordKey;
	private final int permissions;
	private final UserType userType;
	
	public static UserBuilder aUser() {
		return aFullUser();
	}
	
	public static UserBuilder anEmployee() {
		return aFullUser();
	}

	public static UserBuilder aFullUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", "user@example.com", null, false, null, 0, UserType.FULL);
	}

	public static UserBuilder anLiteUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", "user@example.com", null, false, null, 0, UserType.LITE);
	}
	
	public static UserBuilder aSystemUser() {
		return new UserBuilder(OrgBuilder.aPrimaryOrg().build(), "some first", "last name", "user_id", "user@example.com", null, false, null, 0, UserType.SYSTEM);
	}
	
	public static UserBuilder anAdminUser() {
		return aFullUser().withUserType(UserType.ADMIN);
	}
	
	public static UserBuilder aSecondaryUser() {
		return new UserBuilder(OrgBuilder.aSecondaryOrg().build(), "some first", "last name", "user_id", "user@example.com", null, false, null, 0, UserType.FULL);
	}
	
	public static UserBuilder aReadOnlyUser() {
		return new UserBuilder(OrgBuilder.aCustomerOrg().build(), "some first", "last name", "user_id", "user@example.com", null, false, null, 0, UserType.READONLY);
	}
	
	public static UserBuilder aDivisionUser() {
		return new UserBuilder(OrgBuilder.aDivisionOrg().build(), "some first", "last name", "user_id", "user@example.com", null, false, null, 0, UserType.FULL);
	}

	private UserBuilder(BaseOrg owner, String firstName, String lastName, String userId, 
			String emailAddress, String password, boolean resetPasswordKey, Long id, 
			int permissions, UserType userType) {
		super(id);
		this.owner = owner;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.emailAddress = emailAddress;
		this.password = password;
		this.resetPasswordKey = resetPasswordKey;
		this.permissions = permissions;
		this.userType = userType;
	}
	
	public UserBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, resetPasswordKey, getId(), permissions, userType));
	}
	
	public UserBuilder withFirstName(String firstName) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, resetPasswordKey, getId(), permissions, userType));
	}
	
	public UserBuilder withLastName(String lastName) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, resetPasswordKey, getId(), permissions, userType));
	}
	
	public UserBuilder withUserId(String userId) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, resetPasswordKey, getId(), permissions, userType));
	}
	
	public UserBuilder withEmailAddress(String emailAddress) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, resetPasswordKey, getId(), permissions, userType));
	}
	
	public UserBuilder withNoPassword() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, null, resetPasswordKey, getId(), permissions, userType));
	}
	
	public UserBuilder withPassword(String password) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, resetPasswordKey, getId(), permissions, userType));
	}

	public UserBuilder withResetPasswordKey() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, true, getId(), permissions, userType));
	}

	public UserBuilder withOutResetPasswordKey() {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, false, getId(), permissions, userType));
	}
	
	public UserBuilder withId(long id) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, false, id, permissions, userType));
	}
	
	public UserBuilder withPermissions(int permissions) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, false, getId(), permissions, userType));
	}

	public UserBuilder withUserType(UserType userType) {
		return makeBuilder(new UserBuilder(owner, firstName, lastName, userId, emailAddress, password, false, getId(), permissions, userType));
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
		user.setRegistered(true);
		user.setUserType(userType);
		
		if (userType.equals(UserType.ADMIN)) {
			user.setPermissions(Permissions.ADMIN);
		}else if (userType.equals(UserType.FULL) || userType.equals(UserType.LITE)) {
			user.setPermissions(permissions);
		}else if (userType.equals(UserType.SYSTEM)) {
			user.setPermissions(Permissions.SYSTEM);
		} else {
			user.setPermissions(Permissions.NO_PERMISSIONS);
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
