package com.n4systems.api.model;

import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.security.Permissions;

public class UserViewBuilder extends BaseBuilder<UserView> {

	private String organization = "N4";
	private String firstName = "john";
	private String lastName = "smith";
	private String email = "john@smith.com";
	private String assignPassword = "Y";
	private String password = "JOHNPASSWORD";
	private String userId = "JohnUserId";
	private String guid = "JohnGUID";
	private int permissions = Permissions.ALL;
	
	private boolean useDefaultValues = false;
	
	public UserViewBuilder() { 		
	}
	
	public UserViewBuilder withDefaultInformation() {
		useDefaultValues = true;
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

	@Override
	public UserView createObject() {
		if (!useDefaultValues) {
			return new UserView();
		} else { 
			return new UserView(organization, email, firstName, lastName, password, password, userId, guid );
		}
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
	
}

