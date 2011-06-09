package com.n4systems.api.model;

import com.n4systems.api.validation.validators.EmailValidator;
import com.n4systems.api.validation.validators.ExternalOrgGlobalIdValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.exporting.beanutils.ExportField;

public class UserView extends ExternalModelView {
	private static final long serialVersionUID = 1L;
	
	public static UserView newUser() {
		UserView view = new UserView();		
		return view;
	}	
	
	@ExportField(title="Organization", order = 0, validators = {}) // FIXME DD : need to add this once it is made more generic.  -->  validators = {ParentOrgResolutionValidator.class})
	private String organization;

	// FIXME DD : need to provide permission validation...not sure what that entails... 
	
	@ExportField(title="Email Address", order = 100, validators = {EmailValidator.class})
	private String contactEmail;
	
	@ExportField(title="First Name", order = 200, validators = {NotNullValidator.class})
	private String firstName;

	@ExportField(title="Last Name", order = 250, validators = {NotNullValidator.class})
	private String lastName;

	@ExportField(title="Password", order = 300)   // FIXME DD : need password validator. 
	private String userName;
	
	@ExportField(title="System ID", order = 9999999, validators = {ExternalOrgGlobalIdValidator.class})
	private String globalId;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	} 
	
	@Override
	public String getGlobalId() {		
		return globalId;
	}

	@Override
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}


}
