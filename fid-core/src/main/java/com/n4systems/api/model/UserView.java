package com.n4systems.api.model;

import com.n4systems.api.validation.validators.AccountTypeValidator;
import com.n4systems.api.validation.validators.EmailValidator;
import com.n4systems.api.validation.validators.ExternalUserGlobalIdValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.api.validation.validators.OrgNameValidator;
import com.n4systems.api.validation.validators.PasswordValidator;
import com.n4systems.api.validation.validators.PermissionValidator;
import com.n4systems.api.validation.validators.YNValidator;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.security.Permissions;

public class UserView extends ExternalModelView {
	public static final long serialVersionUID = 1L;

	public static final String USER_NAME_FIELD = "User Name";
	public static final String MANAGE_JOBS_FIELD = "Manage Jobs (Y/N)";
	public static final String MANAGE_SAFETY_NETWORK_FIELD = "Manage Safety Network (Y/N)";
	public static final String ACCESS_WEB_STORE_FIELD = "Access Web Store (Y/N)";
	public static final String SYSTEM_ID_FIELD = "System ID";
	public static final String EDIT_EVENTS_FIELD = "Edit Events (Y/N)";
	public static final String CREATE_EVENTS_FIELD = "Create Events (Y/N)";
	public static final String MANAGE_JOB_SITES_FIELD = "Manage Job Sites (Y/N)";
	public static final String MANAGE_SYSTEM_USERS_FIELD = "Manage System Users (Y/N)";
	public static final String MANAGE_SYSTEM_CONFIGURATION_FIELD = "Manage System Configuration (Y/N)";
	public static final String IDENTIFY_ASSETS_FIELD = "Identify Assets";
	public static final String TIME_ZONE_FIELD = "Time Zone";
	public static final String COUNTRY_FIELD = "Country";
	public static final String SEND_WELCOME_EMAIL_FIELD = "Send Welcome Email (Y/N)";
	public static final String SECURITY_RFID_NUMBER_FIELD = "Security RFID Number";
	public static final String POSTITION_FIELD = "Postition";
	public static final String INITIALS_FIELD = "Initials";
	public static final String DIVISION_FIELD = "Division";
	public static final String ACCOUNT_TYPE_FIELD = "Account Type";
	public static final String PASSWORD_FIELD = "Password";
	public static final String ASSIGN_PASSWORD_FIELD = "Assign Password";
	public static final String LAST_NAME_FIELD = "Last Name";
	public static final String FIRST_NAME_FIELD = "First Name";
	public static final String EMAIL_ADDRESS_FIELD = "Email Address";
	public static final String ORGANIZATION_FIELD = "Organization";

	
	
	public static UserView newUser() {
		UserView view = new UserView();		
		return view;
	}	
	
	@ExportField(title=ORGANIZATION_FIELD, order = 0, validators = {OrgNameValidator.class}) 
	private String organization;
	
	@ExportField(title=EMAIL_ADDRESS_FIELD, order = 100, validators = {NotNullValidator.class, EmailValidator.class})
	private String contactEmail;
	
	@ExportField(title=FIRST_NAME_FIELD, order = 200, validators = {NotNullValidator.class})
	private String firstName;

	@ExportField(title=LAST_NAME_FIELD, order = 250, validators = {NotNullValidator.class})
	private String lastName;

	@ExportField(title=ASSIGN_PASSWORD_FIELD, order = 300, validators = {YNValidator.class})
	private String assignPassword;
	
	@ExportField(title=PASSWORD_FIELD, order = 305, validators = {PasswordValidator.class})
	private String password;
	
	@ExportField(title=USER_NAME_FIELD, order = 325, validators = {NotNullValidator.class})  
	private String userName;

	@ExportField(title=ACCOUNT_TYPE_FIELD, order = 350, validators = {NotNullValidator.class, AccountTypeValidator.class})
	private String accountType;
	
	@ExportField(title=DIVISION_FIELD, order = 400, validators = {})
	private String division;
	
	@ExportField(title=INITIALS_FIELD, order = 410, validators = {})
	private String initials;
	
	@ExportField(title=POSTITION_FIELD, order = 450, validators = {})
	private String position;

	@ExportField(title=SECURITY_RFID_NUMBER_FIELD, order = 500, validators = {})
	private String securityRfidNumber;
	
	@ExportField(title=SEND_WELCOME_EMAIL_FIELD, order = 520, validators = {YNValidator.class})		
	private String sendWelcomeEmail;
	
	@ExportField(title=COUNTRY_FIELD, order = 530, validators = {})
	private String country;
	
	@ExportField(title=TIME_ZONE_FIELD, order = 550, validators = {})
	private String timeZone;
	
	@ExportField(title=IDENTIFY_ASSETS_FIELD, order = 560, validators = {PermissionValidator.class})
	private String identifyAssets;
	
	@ExportField(title=MANAGE_SYSTEM_CONFIGURATION_FIELD, order = 570, validators = {PermissionValidator.class})
	private String manageSystemConfiguration;
	
	@ExportField(title=MANAGE_SYSTEM_USERS_FIELD, order = 580, validators = {PermissionValidator.class})
	private String manageSystemUsers;
	
	@ExportField(title=MANAGE_JOB_SITES_FIELD, order = 590, validators = {PermissionValidator.class})
	private String manageJobSites;
	
	@ExportField(title=CREATE_EVENTS_FIELD, order = 600, validators = {PermissionValidator.class})
	private String createEvents;
	
	@ExportField(title=EDIT_EVENTS_FIELD, order = 610, validators = {PermissionValidator.class})
	private String editEvents;
	
	@ExportField(title=MANAGE_JOBS_FIELD, order = 620, validators = {PermissionValidator.class})
	private String manageJobs;
	
	@ExportField(title=MANAGE_SAFETY_NETWORK_FIELD, order = 630, validators = {PermissionValidator.class})
	private String manageSafetyNetwork;
	
	@ExportField(title=ACCESS_WEB_STORE_FIELD, order = 640, validators = {PermissionValidator.class})
	private String accessWebStore;
	
	@ExportField(title=SYSTEM_ID_FIELD, order = 9999999, validators = {ExternalUserGlobalIdValidator.class})
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

	public int getPermissions() {
		// CAVEAT : this defines the permissions as defined in the excel columns but they aren't validated. 
		int permissions = 0;
		if (isManageSystemConfiguration()) {
			permissions |= Permissions.ManageSystemConfig;		
		}
		// FIXME DD : do rest of permissions....
		return permissions;
	}

	private boolean isManageSystemConfiguration() {
		return YNField.Y.toString().equals(getManageSystemConfiguration());
	}

}
