package com.n4systems.api.model;

import com.n4systems.api.validation.validators.AccountTypeValidator;
import com.n4systems.api.validation.validators.EmailValidator;
import com.n4systems.api.validation.validators.ExternalOrgGlobalIdValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.api.validation.validators.OrgNameValidator;
import com.n4systems.api.validation.validators.PasswordValidator;
import com.n4systems.exporting.beanutils.ExportField;

public class UserView extends ExternalModelView {
	private static final long serialVersionUID = 1L;
	
	public static UserView newUser() {
		UserView view = new UserView();		
		return view;
	}	
	
	@ExportField(title="Organization", order = 0, validators = {OrgNameValidator.class}) 
	private String organization;
	
	@ExportField(title="Email Address", order = 100, validators = {EmailValidator.class})
	private String contactEmail;
	
	@ExportField(title="First Name", order = 200, validators = {NotNullValidator.class})
	private String firstName;

	@ExportField(title="Last Name", order = 250, validators = {NotNullValidator.class})
	private String lastName;

	@ExportField(title="Assign Password", order = 300, validators = {})
	private String assignPassword;
	
	@ExportField(title="Password", order = 310, validators = {PasswordValidator.class})  
	private String userName;

	// FIXME DD : need to provide permission validation...not sure what that entails... 
	
	@ExportField(title="Account Type", order = 350, validators = {AccountTypeValidator.class})
	private String accountType;
	
	@ExportField(title="Division", order = 400, validators = {})
	private String division;
	
	@ExportField(title="Initials", order = 410, validators = {})
	private String initials;
	
	@ExportField(title="Postition", order = 450, validators = {})
	private String position;
	
	@ExportField(title="Password", order = 470, validators = {})
	private String password;

	@ExportField(title="Security RFID Number", order = 500, validators = {})
	private String securityRfidNumber;
	
	@ExportField(title="Send Welcome Email (Y/N)", order = 520, validators = {})		
	private String sendWelcomeEmail;
	
	@ExportField(title="Country", order = 530, validators = {})
	private String country;
	
	@ExportField(title="Time Zone", order = 550, validators = {})
	private String timeZone;
	
	@ExportField(title="Identify Assets", order = 560, validators = {})
	private String identifyAssets;
	
	@ExportField(title="Manage System Configuration (Y/N)", order = 570, validators = {})
	private String manageSystemConfiguration;
	
	@ExportField(title="Manage System Users (Y/N)", order = 580, validators = {})
	private String manageSystemUsers;
	
	@ExportField(title="Manage Job Sites (Y/N)", order = 590, validators = {})
	private String manageJobSites;
	
	@ExportField(title=" Create Events (Y/N)", order = 600, validators = {})
	private String createEvents;
	
	@ExportField(title="Edit Events (Y/N)", order = 610, validators = {})
	private String editEvents;
	
	@ExportField(title="Manage Jobs (Y/N)", order = 620, validators = {})
	private String manageJobs;
	
	@ExportField(title="Manage Safety Network (Y/N)", order = 630, validators = {})
	private String manageSafetyNetwork;
	
	@ExportField(title="Access Web Store (Y/N)", order = 640, validators = {})
	private String accessWebStore;
	
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
	
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAssignPassword() {
		return assignPassword;
	}

	public void setAssignPassword(String assignPassword) {
		this.assignPassword = assignPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecurityRfidNumber() {
		return securityRfidNumber;
	}

	public void setSecurityRfidNumber(String securityRfidNumber) {
		this.securityRfidNumber = securityRfidNumber;
	}

	public String getSendWelcomeEmail() {
		return sendWelcomeEmail;
	}

	public void setSendWelcomeEmail(String sendWelcomeEmail) {
		this.sendWelcomeEmail = sendWelcomeEmail;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getIdentifyAssets() {
		return identifyAssets;
	}

	public void setIdentifyAssets(String identifyAssets) {
		this.identifyAssets = identifyAssets;
	}

	public String getManageSystemConfiguration() {
		return manageSystemConfiguration;
	}

	public void setManageSystemConfiguration(String manageSystemConfiguration) {
		this.manageSystemConfiguration = manageSystemConfiguration;
	}

	public String getManageSystemUsers() {
		return manageSystemUsers;
	}

	public void setManageSystemUsers(String manageSystemUsers) {
		this.manageSystemUsers = manageSystemUsers;
	}

	public String getManageJobSites() {
		return manageJobSites;
	}

	public void setManageJobSites(String manageJobSites) {
		this.manageJobSites = manageJobSites;
	}

	public String getCreateEvents() {
		return createEvents;
	}

	public void setCreateEvents(String createEvents) {
		this.createEvents = createEvents;
	}

	public String getEditEvents() {
		return editEvents;
	}

	public void setEditEvents(String editEvents) {
		this.editEvents = editEvents;
	}

	public String getManageJobs() {
		return manageJobs;
	}

	public void setManageJobs(String manageJobs) {
		this.manageJobs = manageJobs;
	}

	public String getManageSafetyNetwork() {
		return manageSafetyNetwork;
	}

	public void setManageSafetyNetwork(String manageSafetyNetwork) {
		this.manageSafetyNetwork = manageSafetyNetwork;
	}

	public String getAccessWebStore() {
		return accessWebStore;
	}

	public void setAccessWebStore(String accessWebStore) {
		this.accessWebStore = accessWebStore;
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
