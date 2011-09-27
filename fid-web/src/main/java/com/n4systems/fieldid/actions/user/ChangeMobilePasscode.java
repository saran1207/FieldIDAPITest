package com.n4systems.fieldid.actions.user;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.validators.HasDuplicateRfidValidator;
import com.n4systems.model.user.User;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class ChangeMobilePasscode extends AbstractCrud implements HasDuplicateRfidValidator {

	private static final long serialVersionUID = 1L;

	protected static final Logger logger = Logger.getLogger( ChangeMobilePasscode.class );
	
	protected UserManager userManager;
	
	protected User user;
	
	protected String securityCardNumber;
	
	public ChangeMobilePasscode(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}
	
	@Override
	protected void initMemberFields() {
		user = persistenceManager.find(User.class, getSessionUserId(), getTenantId() );
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		return SUCCESS;
	}
	
	public String doUpdate() {		
		if( user != null ) {
			
			user.assignSecruityCardNumber(securityCardNumber);
			try {
				userManager.updateUser(user);
			} catch (DuplicateUserException e) {}
						
			logger.info("mobile passcode updated for " + getSessionUser().getUserID());
			addFlashMessageText("message.passcodeupdated");
		}
		
		return SUCCESS;
	}

	public String doRemove() {
		if( user != null ) {
			
			user.assignSecruityCardNumber(null);
			try {
				userManager.updateUser(user);
			} catch (DuplicateUserException e) {}
						
			logger.info("mobile passcode number removed for " + getSessionUser().getUserID());
			addFlashMessageText("message.passcoderemoved");
		}
		
		return SUCCESS;
	}
	
	public String getSecurityCardNumber() {
		return securityCardNumber;
	}
	
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key="error.securitycardnumberrequired")
	@StringLengthFieldValidator(type=ValidatorType.FIELD, message = "" , key = "errors.securitycardlength", minLength="4")
	@CustomValidator(type = "duplicateRfidValidator", message = "", key = "errors.data.passcodeduplicate")
	public void setSecurityCardNumber(String securityCardNumber) {
		if (securityCardNumber == null || securityCardNumber.trim().length() == 0) {
			this.securityCardNumber = null;
		} else {
			this.securityCardNumber = securityCardNumber;
		}
	}

	@Override
	public boolean validateRfid(String formValue) {
		return !userManager.userRfidIsUnique(getTenantId(), formValue, getSessionUserId());
	}
	
	public boolean isMobilePasscodeSet() {
		return user.getHashSecurityCardNumber() != null && !user.getHashSecurityCardNumber().isEmpty();
	}
}
