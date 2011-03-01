package com.n4systems.fieldid.actions.users;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.user.UserPasswordWelcomeNotificationProducer;
import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.actions.utils.PasswordEntry;
import com.n4systems.fieldid.validators.HasDuplicateRfidValidator;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserPaginatedLoader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.UserGroup;
import com.n4systems.util.UserType;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

abstract public class UserCrud extends AbstractCrud implements HasDuplicateValueValidator, HasDuplicateRfidValidator {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserCrud.class);

	protected User user;
	protected OwnerPicker ownerPicker;

	protected UserManager userManager;

	private PasswordEntry passwordEntry = new PasswordEntry();
	private boolean assignPassword = true;
	protected Pager<User> page;

	private String listFilter;

	private String userType = UserType.ALL.name();
	private String userGroup = UserGroup.ALL.name();
	private ArrayList<StringListingPair> userTypes;
	private List<User> userList = new ArrayList<User>();
	private String securityCardNumber;
	private Country country;
	private Region region;

	private WelcomeMessage welcomeMessage = new WelcomeMessage();
	private UploadedImage signature = new UploadedImage();

	protected List<ListingPair> litePermissions;

	protected UserCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
		user = new User();
		user.setTimeZoneID(getSessionUserOwner().getInternalOrg().getDefaultTimeZone());
		initializeTimeZoneLists();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		if (user == null) {
			user = persistenceManager.find(User.class, uniqueId, getTenantId());
			setUserType(user.getUserType().toString());

			initializeTimeZoneLists();
		}
	}

	public abstract boolean isEmployee();

	public abstract boolean isLiteUser();

	public abstract boolean isReadOnlyUser();

	public abstract boolean isFullUser();

	private void initializeTimeZoneLists() {
		country = CountryList.getInstance().getCountryByFullName(user.getTimeZoneID());
		region = CountryList.getInstance().getRegionByFullId(user.getTimeZoneID());
	}

	@Override
	protected void postInit() {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), user);
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doArchivedList() {
		return SUCCESS;
	}

	protected void testRequiredEntities(boolean existing) {
		testUserEntity(existing);
	}

	private void testUserEntity(boolean existing) {
		if (user == null || (existing && user.getId() == null)) {
			addActionErrorText("error.no_user");
			throw new MissingEntityException("user is required.");
		}
	}

	@SkipValidation
	public String doShow() {
		testUserEntity(true);
		loadCurrentSignature();
		return SUCCESS;
	}

	@SkipValidation
	public String doUpgrade() {
		testUserEntity(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		save();
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		loadCurrentSignature();
		if(user.getOwner().isArchived()) {
			user.setOwner(null);			
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doSendWelcomeMessage() {
		testUserEntity(true);
		welcomeMessage.setSendEmail(true);
		sendWelcomeEmail();
		return SUCCESS;
	}

	private void loadCurrentSignature() {
		File signatureImage = PathHandler.getSignatureImage(user);
		if (signatureImage.exists()) {
			signature.setImage(signatureImage);
		}
	}

	public String doUpdate() {
		testRequiredEntities(true);
		save();
		return SUCCESS;
	}

	@SkipValidation
	public String doArchive() {
		testRequiredEntities(true);

		try {
			user.archiveUser();
			new UserSaver().update(user); 
		} catch (Exception e) {
			addFlashErrorText("error.failedtoarchive");
			logger.error("failed to archive user ", e);
			return ERROR;
		}
		addFlashMessageText("message.userarchived");
		return SUCCESS;
	}
	
	@SkipValidation
	public String doUnarchive() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	protected String save() {
		clearErrorsAndMessages();

		user.setPermissions(processPermissions());

		user.setTenant(getTenant());
		user.setRegistered(true);
		user.setModifiedBy(super.getUser());
		user.setCreatedBy(super.getUser());

		try {

			if (user.getId() == null) {
				user.assignPassword(passwordEntry.getPassword());
				user.assignSecruityCardNumber(securityCardNumber);
				new UserSaver().save(user);
				uniqueID = user.getId();
				if(isAssignPassword())
					sendWelcomeEmail();
				else
					sendCreatePasswordEmail();
			} else {
				if(user.isArchived()) {
					user.activateEntity();
				}
				new UserSaver().update(user);
			}
		} catch (Exception e) {
			addActionErrorText("error.saving_user");
			logger.error("failed to save user ", e);
			return ERROR;
		}

		if (user.getId().equals(getSessionUserId())) {
			refreshSessionUser();
		}
		addFlashMessageText("message.user_saved");
		processSignature();
		return "saved";
	}

	private void processSignature() {
		try {
			signatureFileProcess();
		} catch (Exception e) {
			logger.error("Failed to upload signature", e);
			addFlashErrorText("error.uploading_signature");
		}

	}

	private void signatureFileProcess() throws Exception {
		new FileSystemUserSignatureFileProcessor(PathHandler.getSignatureImage(user)).process(signature);
	}

	protected void sendWelcomeEmail() {
		if (welcomeMessage.isSendEmail()) {
			if (welcomeMessage.isPersonalMessageProvided()) {
				getWelcomeNotifier().sendPersonalizedWelcomeNotificationTo(user, welcomeMessage.getPersonalMessage());
			} else {
				getWelcomeNotifier().sendWelcomeNotificationTo(user);
			}
			addFlashMessageText("label.welcome_message_sent");
		}
	}

	private void sendCreatePasswordEmail() {
		user.createResetPasswordKey();
		new UserSaver().update(user);
		if (welcomeMessage.isSendEmail()) {
			if (welcomeMessage.isPersonalMessageProvided()) {
				getPasswordWelcomeNotifier().sendPersonalizedWelcomeNotificationTo(user, welcomeMessage.getPersonalMessage());
			} else {
				getPasswordWelcomeNotifier().sendWelcomeNotificationTo(user);
			}
			addFlashMessageText("label.welcome_message_sent");
		}
	}

	private UserWelcomeNotificationProducer getWelcomeNotifier() {
		UserWelcomeNotificationProducer userWelcomeNotificationProducer = new UserWelcomeNotificationProducer(getDefaultNotifier(), createActionUrlBuilder());
		return userWelcomeNotificationProducer;
	}

	private UserPasswordWelcomeNotificationProducer getPasswordWelcomeNotifier() {
		UserPasswordWelcomeNotificationProducer userWelcomeNotificationProducer = new UserPasswordWelcomeNotificationProducer(getDefaultNotifier(), createActionUrlBuilder());
		return userWelcomeNotificationProducer;
	}
	
	protected abstract int processPermissions();

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.useridrequired")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "errors.useridlength", maxLength = "15")
	@CustomValidator(type = "uniqueValue", message = "", key = "errors.data.userduplicate")
	public String getUserId() {
		return user.getUserID();
	}

	public void setUserId(String userId) {
		user.setUserID(userId);
	}

	public String getFirstName() {
		return user.getFirstName();
	}

	@RequiredStringValidator(message = "", key = "error.firstnameisrequired")
	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}

	public String getLastName() {
		return user.getLastName();
	}

	@RequiredStringValidator(message = "", key = "error.lastnameisrequired")
	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	public String getInitials() {
		return user.getInitials();
	}

	public void setInitials(String initials) {
		user.setInitials(initials);
	}

	public String getTimeZoneID() {
		return region.getId();
	}

	public String getTimeZoneName() {
		return region.getDisplayName();
	}

	public void setTimeZoneID(String regionId) {
		if (country != null) {
			region = country.getRegionById(regionId);
			user.setTimeZoneID(country.getFullName(region));
		}
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.emailrequired")
	@EmailValidator(type = ValidatorType.FIELD, message = "", key = "error.emailformat")
	public String getEmailAddress() {
		return user.getEmailAddress();
	}

	public void setEmailAddress(String emailAddress) {
		user.setEmailAddress(emailAddress);
	}

	public String getPosition() {
		return user.getPosition();
	}

	public void setPosition(String position) {
		user.setPosition(position);
	}

	@CustomValidator(type = "duplicateRfidValidator", message = "", key = "errors.data.rfidduplicate")
	public String getSecurityRfidNumber() {
		return securityCardNumber;
	}

	public void setSecurityRfidNumber(String securityRfidNumber) {
		securityCardNumber = securityRfidNumber;
	}

	public String getCountryId() {
		return country.getId();
	}

	public String getCountryName() {
		return country.getDisplayName();
	}

	public void setCountryId(String countryId) {
		country = CountryList.getInstance().getCountryById(countryId);
	}

	public Pager<User> getPage() {
		if (page == null) {
			UserType typeFilter = UserType.valueOf(userType);
			page = new UserPaginatedLoader(getSecurityFilter())
			               .withUserType(typeFilter)
			               .withNameFilter(listFilter)
			               .setPage(getCurrentPage().intValue())
			               .setPageSize(Constants.PAGE_SIZE)
			               .load();
		}
		return page;
	}
	
	public Pager<User> getArchivedPage() {
		return new UserPaginatedLoader(new TenantOnlySecurityFilter(getSecurityFilter()).setShowArchived(true))
		                   .withArchivedOnly()
			               .setPage(getCurrentPage().intValue())
			               .setPageSize(Constants.PAGE_SIZE)
			               .load();
	}
	
	public SortedSet<? extends Listable<String>> getCountries() {
		return CountryList.getInstance().getCountries();
	}

	public SortedSet<? extends Listable<String>> getTimeZones() {
		return (country != null) ? country.getRegions() : new TreeSet<Listable<String>>();
	}

	public boolean duplicateValueExists(String formValue) {
		return !userManager.userIdIsUnique(getTenantId(), formValue, uniqueID);
	}

	public boolean validateRfid(String formValue) {
		return !userManager.userRfidIsUnique(getTenantId(), formValue, uniqueID);
	}

	public List<ListingPair> getLitePermissions() {
		if (litePermissions == null) {
			litePermissions = ListHelper.intListableToListingPair(Permissions.getLiteUserPermissions());
		}
		return litePermissions;
	}

	public List<StringListingPair> getUserTypes() {
		if (userTypes == null) {
			userTypes = new ArrayList<StringListingPair>();
			UserType[] allUserTypes = UserType.values();
			for (int i = 0; i < allUserTypes.length; i++) {
				if (!allUserTypes[i].equals(UserType.SYSTEM) && !allUserTypes[i].equals(UserType.ADMIN)) {	
					
					if(allUserTypes[i].equals(UserType.LITE) && getPrimaryOrg().isLiteUsersEnabled() 
							|| !allUserTypes[i].equals(UserType.LITE)) {					
						userTypes.add(new StringListingPair(allUserTypes[i].name(), allUserTypes[i].getLabel()));
					}
				}
			}
		}
		return userTypes;
	}

	public List<StringListingPair> getUserGroups() {
		ArrayList<StringListingPair> userGroups = new ArrayList<StringListingPair>();
		for (int i = 0; i < UserGroup.values().length; i++) {
			userGroups.add(new StringListingPair(UserGroup.values()[i].name(), UserGroup.values()[i].getLabel()));
		}
		return userGroups;
	}

	public String getListFilter() {
		return listFilter;
	}

	public void setListFilter(String listFilter) {
		this.listFilter = listFilter;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public User getUser() {
		return user;
	}

	@RequiredFieldValidator(message = "", key = "error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	@CustomValidator(type = "conditionalVisitorFieldValidator", message = "", parameters = { @ValidationParameter(name = "expression", value = "checkPasswords == true") })
	public PasswordEntry getPasswordEntry() {
		return passwordEntry;
	}

	public boolean isCheckPasswords() {
		return user.isNew() && assignPassword;
	}

	public UploadedImage getSignature() {
		return signature;
	}

	public void setSignature(UploadedImage signature) {
		this.signature = signature;
	}

	public WelcomeMessage getWelcomeMessage() {
		return welcomeMessage;
	}

	public boolean isAssignPassword() {
		return assignPassword;
	}

	public void setAssignPassword(boolean assignPassword) {
		this.assignPassword = assignPassword;
	}

	public Date dateCreated(User user) {
		ActiveSession session = new ActiveSessionLoader().setId(user.getId()).load();
		return (session != null) ? session.getDateCreated() : null;
	}

	public boolean isEmployeeLimitReached() {
		return getLimits().isEmployeeUsersMaxed();
	}

	public boolean isLiteUserLimitReached() {
		return getLimits().isLiteUsersMaxed();
	}

	public List<User> getUserList() {

		if (userGroup.equals(UserGroup.CUSTOMER.toString())) {
			for (User user : getPage().getList()) {
				if (user != null && user.getOwner().isCustomer()) {
					userList.add(user);
				}
			}
		} else if (userGroup.equals(UserGroup.EMPLOYEE.toString())) {
			for (User user : getPage().getList()) {
				if (user != null && !user.getOwner().isCustomer()) {
					userList.add(user);
				}
			}
		} else {
			for (User user : getPage().getList()) {
				if (user != null) {
					userList.add(user);
				}
			}
		}
		return userList;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public void setUserTypes(ArrayList<StringListingPair> userTypes) {
		this.userTypes = userTypes;
	}

}
