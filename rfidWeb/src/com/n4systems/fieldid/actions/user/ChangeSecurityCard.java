package com.n4systems.fieldid.actions.user;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.users.ChangePasswordCrud;
import com.n4systems.model.user.User;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class ChangeSecurityCard extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger( ChangePasswordCrud.class );
	
	protected UserManager userManager;
	protected User user;
	
	private String securityCardNumber;

	public ChangeSecurityCard(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}
	
	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	@SkipValidation
	public String doEdit() {
		return SUCCESS;
	}
	
	
	public String doUpdate() {
		 user = persistenceManager.find(User.class, getSessionUserId(), getTenantId() );
		
		try {
			updateSecurityCard();
		} catch (DuplicateUserException e) {}
					
		logger.info("security card number updated for " + getSessionUser().getUserID());
		addFlashMessageText("message.securityupdated");
		
		return SUCCESS;
	}

	public String getSecurityCardNumber() {
		return securityCardNumber;
	}

	protected void updateSecurityCard() {
		if (user!=null){
			user.assignSecruityCardNumber(securityCardNumber);
			userManager.updateUser(user);
		}
	}
	
	@StringLengthFieldValidator(type=ValidatorType.FIELD, message = "" , key = "errors.securitycardlength", minLength="16")
	public void setSecurityCardNumber(String securityCardNumber) {
		if (securityCardNumber == null || securityCardNumber.trim().length() == 0) {
			this.securityCardNumber = null;
		} else {
			this.securityCardNumber = securityCardNumber;
		}
	}
	
	public User getUser() {
		return user;
	}
}
