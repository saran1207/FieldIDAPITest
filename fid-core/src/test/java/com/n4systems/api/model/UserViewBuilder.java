package com.n4systems.api.model;

import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.security.Permissions;

public class UserViewBuilder extends BaseBuilder<UserView> {

	private String organization;
	private String firstName;
	private String lastName;
	private String email;
	private String assignPassword;
	private String password;
	private String userId;
	private String guid;
	private String sendWelcomeEmail = "N";
	private int permissions = Permissions.ALL;
	
	public UserViewBuilder() { 		
	}
	
	public UserViewBuilder withDefaultValues() {
		organization = "N4";
		firstName = "john";
		lastName = "smith";
		email = "john@smith.com";
		assignPassword = "Y";
		password = "JOHNPASSWORD";
		userId = "JohnUserId";
		guid = "JohnGUID";
		return this;
	}
	
	public UserViewBuilder withGUID(String guid) {
		this.guid = guid;
		return this;
	}
	
	public UserViewBuilder withPermissions(int permissions) {
		this.permissions = permissions;
		return this;
	}	

	public UserViewBuilder withFirstName(String firstName) { 
		this.firstName = firstName;
		return this;
	}
	
	public UserViewBuilder withLastName(String firstName) { 
		this.lastName = firstName;
		return this;
	}
	
	@Override
	public UserView createObject() {
		return new UserView(organization, email, firstName, lastName, assignPassword, password, userId, sendWelcomeEmail, guid );
	}

	public BaseBuilder<UserView> withGuid(Object object) {
		this.guid = null;
		return this;
	}

	public UserViewBuilder withAssignPassword(String yn) {
		this.assignPassword = yn;
		return this;
	}

	public UserViewBuilder withPassword(String pw) {
		this.password = pw;
		return this;
	}

	public UserViewBuilder withSendWelcomeEmail(String sendWelcomeEmail ) {
		this.sendWelcomeEmail = sendWelcomeEmail;
		return this;
	}
	
}

