package com.n4systems.api.model;

import com.n4systems.api.validation.validators.*;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.beanutils.MaskedSerializationHandler;
import com.n4systems.exporting.beanutils.OwnerSerializationHandler;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;

import java.util.Collection;

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
	public static final String SEND_WELCOME_EMAIL_FIELD = "Send Welcome Email (Y/N)";
	public static final String MOBILE_PASSCODE_FIELD = "Mobile Passcode";
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
	public static final String CUSTOMER_FIELD = "Customer/Job Site";
    public static final String USER_GROUP = "User Group";
    public static final String IDENTIFIER ="ID";
	
	public static UserView newUser() {
		UserView view = new UserView();		
		return view;
	}	

	public UserView() { }
	
	@Deprecated // used for testing only. 
	UserView(String organization, String email, String firstName, String lastName, String assignPassword, String password, String userId, String sendWelcomeEmail, String guid, String accountType) {
		this.emailAddress = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.assignPassword = assignPassword;
		this.password = password;
		this.userID = userId;
		this.globalId = guid;
		this.sendWelcomeEmail = sendWelcomeEmail;
		this.accountType = accountType;
		setOrganization(organization);
		setCustomer("customer"); 
		setDivision("division");
	}

	@SerializableField(title = "", order = 0, handler = OwnerSerializationHandler.class, validators = { NotNullValidator.class, OwnerExistsValidator.class })
	private final String[] owners = new String[3];

    @SerializableField(title=USER_GROUP, order = 50, validators = {UserGroupExistsValidator.class})
    private String userGroup;
	
	@SerializableField(title=EMAIL_ADDRESS_FIELD, order = 100, validators = {NotNullValidator.class, EmailValidator.class})
	private String emailAddress;
	
	@SerializableField(title=FIRST_NAME_FIELD, order = 200, validators = {NotNullValidator.class})
	private String firstName;

	@SerializableField(title=LAST_NAME_FIELD, order = 250, validators = {NotNullValidator.class})
	private String lastName;

	@SerializableField(title=INITIALS_FIELD, order = 310, validators = {})
	private String initials;

    @SerializableField(title=IDENTIFIER, order = 320, maxLength = 20, validators = {StringLengthValidator.class})
    private String identifier;

	@SerializableField(title=POSTITION_FIELD, order = 350, validators = {})
	private String position;

	// NOTE : this field is also tested in conjunction with GlobalId.  (ie. if globalId=null, userId must not exist in DB and vice versa).
	//   userId is limited (currently to 15) by schema.rb definition.  if schema changes, alter this meta-data.
	@SerializableField(title=USER_NAME_FIELD, order = 425, maxLength = 15, validators = {UserIdNotNullValidator.class, UserIdStringLengthValidator.class})
	private String userID;

	@SerializableField(title=ACCOUNT_TYPE_FIELD, order = 450, validators = {NotNullValidator.class, AccountTypeValidator.class})
	private String accountType;	
	
	@SerializableField(title=IDENTIFY_ASSETS_FIELD, order = 560, validators = {PermissionValidator.class})

	private String identifyAssets;
	
	@SerializableField(title=MANAGE_SYSTEM_CONFIGURATION_FIELD, order = 570, validators = {PermissionValidator.class})
	private String manageSystemConfiguration;
	
	@SerializableField(title=MANAGE_SYSTEM_USERS_FIELD, order = 580, validators = {PermissionValidator.class})
	private String manageSystemUsers;
	
	@SerializableField(title=MANAGE_JOB_SITES_FIELD, order = 590, validators = {PermissionValidator.class})
	private String manageJobSites;
	
	@SerializableField(title=CREATE_EVENTS_FIELD, order = 600, validators = {PermissionValidator.class})
	private String createEvents;
	
	@SerializableField(title=EDIT_EVENTS_FIELD, order = 610, validators = {PermissionValidator.class})
	private String editEvents;
	
	@SerializableField(title=MANAGE_JOBS_FIELD, order = 620, validators = {PermissionValidator.class})
	private String manageJobs;
	
	@SerializableField(title=MANAGE_SAFETY_NETWORK_FIELD, order = 630, validators = {PermissionValidator.class})
	private String manageSafetyNetwork;
	
    @SerializableField(title= MOBILE_PASSCODE_FIELD, order = 680, validators = {}, handler=MaskedSerializationHandler.class)
	private String securityRfidNumber;

	@SerializableField(title=ASSIGN_PASSWORD_FIELD, order = 700, validators = {YNValidator.class})
	private String assignPassword = "N";
	
	@SerializableField(title=PASSWORD_FIELD, order = 705, validators = {PasswordValidator.class}, handler=MaskedSerializationHandler.class)
	private String password;
	
	@SerializableField(title=SEND_WELCOME_EMAIL_FIELD, order = 750, validators = {YNValidator.class})		
	private String sendWelcomeEmail;
	
	@SerializableField(title=SYSTEM_ID_FIELD, order = 9999999, validators = {ExternalUserGlobalIdValidator.class})
	private String globalId;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
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
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getDivision() {
		return getOwners()[OwnerSerializationHandler.DIVISION_INDEX];
	}

	public void setDivision(String division) {
		this.getOwners()[OwnerSerializationHandler.DIVISION_INDEX]= division;
	}

	public String getOrganization() {
		return getOwners()[OwnerSerializationHandler.ORGANIZATION_INDEX];
	}

	public void setOrganization(String organization) {
		this.getOwners()[OwnerSerializationHandler.ORGANIZATION_INDEX] = organization;
	}

	public void setCustomer(String customer) {
		this.getOwners()[OwnerSerializationHandler.CUSTOMER_ID] = customer;
	}

	public String getCustomer() {
		return getOwners()[OwnerSerializationHandler.CUSTOMER_ID];
	}
	
	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSendWelcomeEmail() {
		return sendWelcomeEmail;
	}

	public void setSendWelcomeEmail(String sendWelcomeEmail) {
		this.sendWelcomeEmail = sendWelcomeEmail;
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

	private boolean isPermission(String yOrN) {
		return YNField.isYes(yOrN);
	}

	@Override
	public String getGlobalId() {		
		return globalId;
	}

	@Override
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	public void setSecurityRfidNumber(String securityRfidNumber) {
		this.securityRfidNumber = securityRfidNumber;
	}

	public String getSecurityRfidNumber() {
		return securityRfidNumber;
	}
	
	
	public int getPermissions() {
		// CAVEAT : this defines the permissions as defined in the excel columns but they aren't validated.
		// ie. it doesn't check to see if the account type actually allows for these permissions.
		int permissions = Permissions.NO_PERMISSIONS;
		if (isPermission(getManageSystemConfiguration())) {
			permissions |= Permissions.ManageSystemConfig;		
		}
		if (isPermission(getManageSystemUsers())) {
			permissions |= Permissions.ManageSystemUsers;		
		}
		if (isPermission(getManageJobs())) {
			permissions |= Permissions.ManageJobs;		
		}
		if (isPermission(getManageJobSites())) {
			permissions |= Permissions.ManageEndUsers;		
		}
		if (isPermission(getManageSafetyNetwork())) {
			permissions |= Permissions.ManageSafetyNetwork;		
		}
		if (isPermission(getIdentifyAssets())) {
			permissions |= Permissions.Tag;					
		}
		if (isPermission(getCreateEvents())) {
			permissions |= Permissions.CreateEvent;		
		}
		if (isPermission(getEditEvents())) {
			permissions |= Permissions.EditEvent;		
		}
		return permissions;
	}

	public String[] getOwners() {
		return owners;
	}

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public boolean isPerson() {
        return UserType.valueFromLabel(getAccountType()).equals(UserType.PERSON);
    }

    public static String getUserGroupNames(Collection<UserGroup> groups) {
        StringBuffer sb = new StringBuffer();
        for (UserGroup group : groups) {
            sb.append(group.getName()).append(",");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }
}
