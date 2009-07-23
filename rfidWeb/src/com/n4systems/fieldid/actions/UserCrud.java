package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.validators.HasDuplicateRfidValidator;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.Organization;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.BitField;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class UserCrud extends AbstractCrud implements HasDuplicateValueValidator, HasDuplicateRfidValidator {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger( UserCrud.class );
	
	protected UserBean user;
	private Map<String, Boolean> userPermissions = new HashMap<String, Boolean>();
		
	protected User userManager;
	protected CustomerManager customerManager;
	private String password;
	private String passwordConfirmation;
	
	private Collection<ListingPair> organizationalUnits;
	protected List<ListingPair> permissions;
	private List<StringListingPair> timeZones;
	private Collection<ListingPair> customers;
	protected Collection<ListingPair> divisions;
	
	protected Pager<UserBean> page;
	private String listFilter;
	private String userType = UserType.ALL.name();
	private String securityCardNumber;
	
	private File signature;
	
	public UserCrud( User userManager, PersistenceManager persistenceManager, CustomerManager customerManager ) {
		super(persistenceManager);
		this.userManager = userManager;
		this.customerManager = customerManager;
		
	}
	
	@Override
	protected void initMemberFields() {
		user = new UserBean();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		if( user == null ) {
			user = userManager.findUser( uniqueId, getTenantId() ); 
		}
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		if( user == null ) {
			addFlashErrorText( "error.unknownuser" );
			return ERROR;
		} 
		
		setupPermissions(); 
	
		
		return SUCCESS;
	}

	private void setupPermissions() {
		userPermissions = new HashMap<String, Boolean>();
		
		BitField permField = new BitField(user.getPermissions());
		for (ListingPair permission : getPermissions()) {
			userPermissions.put(permission.getId().toString(), permField.isSet(permission.getId().intValue()));		
		}
	}
	
	public String doSave() {
		clearErrorsAndMessages();
		if( user == null ) {
			addFlashErrorText( "error.unknownuser" );
			return ERROR;
		}

		if( userPermissions != null && !user.isAdmin()) {  // needed to handle when there is an empty list submitted.
			BitField perms = new BitField();
			/*
			 * we could do this by simply converting the id's back to strings and setting them on the field,
			 * however that would unsafe, since we can't trust what's coming back.  Instead, get the list of 
			 * permissions they were allowed to see in the first place, and ensure our key is from that set
			 */
			int permValue;
			String permStr;
			for (ListingPair allowedPerm: getPermissions()) {
				permValue = allowedPerm.getId().intValue();
				permStr = allowedPerm.getId().toString();
				
				// if our permission map contains the permission id, and it's value is true, add the permission
				if (userPermissions.containsKey(permStr) && userPermissions.get(permStr)) {
					perms.set(permValue);
				}
			}
			
			user.setPermissions(perms.getMask());
		}
		
		user.setTenant( getTenant() );
		user.setActive( true );
		user.setSystem(false);
		
		try {
			if( user.getUniqueID() == null ) {		
				user.assignPassword( password );
				user.assignSecruityCardNumber(securityCardNumber);
				userManager.createUser( user );
				uniqueID = user.getUniqueID();
			} else {
				userManager.updateUser( user );
			}
		} catch ( Exception e ) {
			addActionError( getText( "error.failedtosave" ) );
			logger.error( "failed to save user ", e);
			return INPUT;
		}
		
		if (user.getId().equals( getSessionUserId())) {
			refreshSessionUser();
		}
		addFlashMessageText( "message.saved" );
		return "saved";
	}
	
	@SkipValidation
	public String doRemove(){
		if( user == null ) {
			addFlashErrorText( "error.unknownuser" );
			return ERROR;
		}
		
		if( user.getUniqueID() != null ) {
			try {
				user.setDeleted(true); 
				userManager.updateUser( user );
			} catch( Exception e ) {
				addFlashErrorText( "error.failedtodelete" );
				logger.error( "failed to remove user ", e);
				return ERROR;
			}
		}
		addFlashMessageText( "message.userdeleted" );
		return SUCCESS;
	}
	
	@SkipValidation
	public String doUploadSignature() {
		if ( user == null ) {
			return ERROR;
		}
		// deal with the image file
		if(signature != null && signature.exists()) {
			try { 
				File userImagePath = PathHandler.getSignatureImage(user);
				
				// delete the old image file if one exists
				if (userImagePath.exists()) {
					userImagePath.delete();
				}
				
				// create the parent directories if needed
				if (!userImagePath.getParentFile().exists()) {
					userImagePath.getParentFile().mkdirs();
				}
				
				// copy our temp image to the private location
				FileUtils.copyFile(signature, userImagePath);
				
				addActionMessage("Image uploaded");
			} catch( Exception e ) {
				logger.error("Failed to upload signature", e);
				addActionError("Error uploading image.");
				return ERROR;
			}
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doSignature() {
		String status = null;
		
		if (user != null) {
			File signatureImage = PathHandler.getSignatureImage(user);
			if (!signatureImage.exists()) {
				return ERROR;
			}

			InputStream signatureIS = null; 
			try {
				getServletResponse().setContentLength((int)signatureImage.length());
				
				signatureIS = new FileInputStream(signatureImage);
			
				IOUtils.copy(signatureIS, getServletResponse().getOutputStream());
				
				IOUtils.closeQuietly(getServletResponse().getOutputStream());
				getServletResponse().flushBuffer();
				
			} catch (Exception e) {
				logger.error("Failed to load signature", e);
				status = ERROR;
			} finally {
				IOUtils.closeQuietly(signatureIS);
			}
			
		} else {
			status = ERROR;
		}
		
		return status;
	}
	
	@SkipValidation
	public String doRemoveSignature() {
		if (user != null) {
			try {
				File signatureImage = PathHandler.getSignatureImage(user);
				if (signatureImage.exists()) {
					signatureImage.delete();
				}
				
				addActionMessage("Image removed");
				return SUCCESS;
			} catch( Exception e ) {
				logger.error("Failed to remove signature", e );
			}
		}
		return ERROR;
	}
	
	public Long getCustomerId() {
		return user.getR_EndUser();
	}
	
	public void setCustomerId( Long customer ) {
		user.setR_EndUser( customer );
	}

	public Long getDivision() {
		return user.getR_Division();
	}
	
	public void setDivision( Long division ) {
		user.setR_Division( division );
	}
	
	@RequiredFieldValidator( type=ValidatorType.FIELD, message="", key="error.organizationrequired")
	public Long getOrganizationalUnit() {
		if( user.getOrganization() != null ) {
			return 	user.getOrganization().getId();
		} else {
			return null;
		}
	}
	
	public void setOrganizationalUnit( Long orgId ) {
		try {
			QueryBuilder<Organization> builder = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setDefaultTargets());
			Organization org = persistenceManager.find(builder.addSimpleWhere("id", orgId));
			user.setOrganization( org );
		} catch(InvalidQueryException e) {
			logger.error("Unable to load Organization", e);
		}
	}	

	public Collection<ListingPair> getCustomers() {
		if( customers == null ){ 
			customers = customerManager.findCustomersLP(getTenantId(), getSecurityFilter());
		}
		return customers;
	}


	public Collection<ListingPair> getDivisions() {
		if( divisions == null ) {
			divisions = new ArrayList<ListingPair>();
			if( getCustomerId() != null ) {
				divisions = customerManager.findDivisionsLP(getCustomerId(), getSecurityFilter());
			}
		}
		return divisions;
	}
	
	public Collection<ListingPair> getOrganizationalUnits() {
		if( organizationalUnits == null ) {
			try {
				QueryBuilder<Organization> builder = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setDefaultTargets());
				organizationalUnits = ListHelper.longListableToListingPair(persistenceManager.findAll(builder));
			} catch (InvalidQueryException e) {
				logger.error("Unable to load list of Organizations", e);
			}
		}
		return organizationalUnits;
	}
	
	

	@SuppressWarnings("unchecked")
	public Map getUserPermissions() {
		
		return userPermissions;
	}

	@SuppressWarnings("unchecked")
	public void setUserPermissions( Map permissions ) {
		this.userPermissions = permissions;
	}

	public List<ListingPair> getPermissions() {
		if( permissions == null ){
			if( user.getR_EndUser() == null ) {
				permissions = ListHelper.intListableToListingPair(Permissions.getSystemUserPermissions());
			} else {
				permissions = ListHelper.intListableToListingPair(Permissions.getCustomerUserPermissions());
			}
				
		}
		return permissions;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.useridrequired")
	@StringLengthFieldValidator( type=ValidatorType.FIELD, message = "" , key = "errors.useridlength", maxLength="15")
	@CustomValidator(type="uniqueValue", message = "", key="errors.data.userduplicate")
	public String getUserId() {
		return user.getUserID();
	}

	public void setUserId(String userId) {
		user.setUserID(userId);
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}

	@RequiredStringValidator(message="", key="error.firstnameisrequired")
	public void setFirstName(String firstName) {
		user.setFirstName( firstName );
	}

	public String getLastName() {
		return user.getLastName();
	}

	@RequiredStringValidator(message="", key="error.lastnameisrequired")
	public void setLastName(String lastName) {
		user.setLastName( lastName );
	}
	
	public String getInitials() {
		return user.getInitials();
	}

	public void setInitials(String initials) {
		user.setInitials(initials);
	}
	
	public String getTimeZoneID() {
		return user.getTimeZoneID();
	}

	public void setTimeZoneID(String timeZoneID) {
		user.setTimeZoneID( timeZoneID );
	}

	@RequiredStringValidator( type=ValidatorType.FIELD, message="", key="error.emailrequired" )
	@EmailValidator( type=ValidatorType.FIELD, message="", key="error.emailformat")
	public String getEmailAddress() {
		return user.getEmailAddress();
	}

	public void setEmailAddress(String emailAddress) {
		user.setEmailAddress( emailAddress );
	}

	public String getPosition() {
		return user.getPosition();
	}

	public void setPosition(String position) {
		user.setPosition( position );
	}

	@CustomValidator(type="duplicateRfidValidator", message = "", key="errors.data.rfidduplicate")
	public String getSecurityRfidNumber() {
		return securityCardNumber;
	}

	public void setSecurityRfidNumber(String securityRfidNumber) {
		securityCardNumber = securityRfidNumber;
	}
	
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.passwordrequired")
	@StringLengthFieldValidator( type=ValidatorType.FIELD, message = "" , key = "errors.passwordlength", minLength="5") 
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@FieldExpressionValidator( expression="passwordConfirmation == password", message="", key="error.passwordsmustmatch" )
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation( String passwordConfirmation ) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public Pager<UserBean> getPage() {
		if( page == null ) {
			UserType userTypeFilter = UserType.ALL;
			if( userType != null ) {
				try {
					userTypeFilter = UserType.valueOf( userType );
				} catch (IllegalArgumentException e) { }
			}
			page = userManager.getUsers( getSecurityFilter(), true, getCurrentPage().intValue(),	Constants.PAGE_SIZE, listFilter, userTypeFilter );
		}
		return page;
	}

	
	public List<StringListingPair> getTimeZones() {
		if (timeZones == null) {
			timeZones = DateHelper.getTimeZones();
		}
		return timeZones;
	}
	

	public boolean duplicateValueExists(String formValue) {
		return !userManager.userIdIsUnique( getTenantId() , formValue, uniqueID );
	}
	
	public boolean validateRfid(String formValue) {
		return !userManager.userRfidIsUnique( getTenantId() , formValue, uniqueID );
	}
	
	public File getSignature() {
		return signature;
	}

	public void setSignature(File signature) {
		this.signature = signature;
	}
	
	public List<StringListingPair> getUserTypes() {
		ArrayList<StringListingPair> userTypes = new ArrayList<StringListingPair>();
		for( int i = 0; i < UserType.values().length; i++ ) {
			userTypes.add(new StringListingPair( UserType.values()[i].name(), UserType.values()[i].getLabel() ) );
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
	
	

}
