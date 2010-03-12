package com.n4systems.fieldid.actions.users;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.actions.utils.PasswordEntry;
import com.n4systems.fieldid.validators.HasDuplicateRfidValidator;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.UserSaver;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Pager;
import com.n4systems.util.StringListingPair;
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

	protected UserBean user;
	protected OwnerPicker ownerPicker;

	protected User userManager;
	
	private PasswordEntry passwordEntry = new PasswordEntry();
	private boolean assignPassword = true;
	
	protected Pager<UserBean> page;
	private String listFilter;
	private String userType = UserType.ALL.name();
	private String securityCardNumber;
	private Country country;
	private Region region;
	
	private WelcomeMessage welcomeMessage = new WelcomeMessage();
	private UploadedImage signature = new UploadedImage();

	protected UserCrud(User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
		user = new UserBean();
		user.setTimeZoneID(getSessionUserOwner().getInternalOrg().getDefaultTimeZone());		
		initializeTimeZoneLists();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		if (user == null) {
			user = userManager.findUser(uniqueId, getTenantId());
			initializeTimeZoneLists();
		}
	}

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

	protected void testRequiredEntities(boolean existing) {
		if (user == null || (existing && user.getId() == null)) {
			addActionErrorText("error.no_user");
			throw new MissingEntityException("user is required.");
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
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
	public String doDelete() {
		testRequiredEntities(true);
		
		try {
			user.archiveUser();
			userManager.updateUser(user); //TODO should use the saver like create and update do.
		} catch (Exception e) {
			addFlashErrorText("error.failedtodelete");
			logger.error("failed to remove user ", e);
			return ERROR;
		}
		addFlashMessageText("message.userdeleted");
		return SUCCESS;
	} 
	
	
	

	protected String save() {
		clearErrorsAndMessages();

		user.setPermissions(processPermissions());

		user.setTenant(getTenant());
		user.setActive(true);
		user.setSystem(false);

		
		
		try {
			if (user.getUniqueID() == null) {
				user.assignPassword(passwordEntry.getPassword());
				user.assignSecruityCardNumber(securityCardNumber);
				new UserSaver().save(user);
				uniqueID = user.getUniqueID();
				sendWelcomeEmail();
			} else {
				new UserSaver().update(user);
			}
		} catch (Exception e) {
			addActionErrorText("error.saving_user");
			logger.error("failed to save user ", e);
			return INPUT;
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

	private void sendWelcomeEmail() {
		if (welcomeMessage.isSendEmail()) {
			if (welcomeMessage.isPersonalMessageProvided()) {
				getWelcomeNotifier().sendPersonalizedWelcomeNotificationTo(user, welcomeMessage.getPersonalMessage());
			} else {
				getWelcomeNotifier().sendWelcomeNotificationTo(user);
			}
			addFlashMessageText("label.welcome_message_sent");
		}
		
		
	}

	private UserWelcomeNotificationProducer getWelcomeNotifier() {
		UserWelcomeNotificationProducer userWelcomeNotificationProducer = new UserWelcomeNotificationProducer(getDefaultNotifier(), createActionUrlBuilder());
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

	public void setCountryId(String countryId) {
		country = CountryList.getInstance().getCountryById(countryId);
	}


	public Pager<UserBean> getPage() {
		if (page == null) {
			UserType userTypeFilter = UserType.ALL;
			if (userType != null) {
				try {
					userTypeFilter = UserType.valueOf(userType);
				} catch (IllegalArgumentException e) {
				}
			}
			page = userManager.getUsers(getSecurityFilter(), true, getCurrentPage().intValue(), Constants.PAGE_SIZE, listFilter, userTypeFilter);
		}
		return page;
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

	

	public List<StringListingPair> getUserTypes() {
		ArrayList<StringListingPair> userTypes = new ArrayList<StringListingPair>();
		for (int i = 0; i < UserType.values().length; i++) {
			userTypes.add(new StringListingPair(UserType.values()[i].name(), UserType.values()[i].getLabel()));
		}
		return userTypes;
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

	public UserBean getUser() {
		return user;
	}

	@RequiredFieldValidator(message="", key="error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	public boolean isCustomerUser() {
		return false;
	}

	
	@CustomValidator(type = "conditionalVisitorFieldValidator", message = "", parameters = { @ValidationParameter(name = "expression", value = "checkPasswords == true") })
	public PasswordEntry getPasswordEntry() {
		return passwordEntry;
	}
	
	public boolean isCheckPasswords() {
		return user.isNew() && assignPassword;
	}
	
	public abstract boolean isEmployee();

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
	
		
}
