package com.n4systems.fieldid.actions.users;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import com.n4systems.security.PasswordComplexityChecker;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;


public class ChangePasswordCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger( ChangePasswordCrud.class );
	
	protected UserManager userManager;
	protected User user;

	@Autowired
	private TenantSettingsService tenantSettingsService;
	
		
	
	private String originalPassword;
	private String newPassword;
	private String confirmPassword;
	
	public ChangePasswordCrud( UserManager userManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
		

	}

	@Override
	protected void loadMemberFields( Long uniqueId ) {

	}

	public String getOriginalPassword() {
		return originalPassword;
	}

	@SkipValidation
	public String doEdit() {
		return SUCCESS;
	}	

	public String doUpdate() {
		user = persistenceManager.find(User.class, getSessionUserId(), getTenantId() );
		if( getSession().get( "passwordReset" ) == null ) {
			if( !user.matchesPassword( originalPassword ) ) {
				addFieldError("originalPassword", getText("error.incorrectpassword"));
				return INPUT;
			}
		}
		
		String status = updatePassword();
		refreshSessionUser();
		return status;
	}

	protected final String updatePassword() {
		if( user == null ) {
			return SUCCESS;
		}
			
		PasswordPolicy passwordPolicy = getPasswordPolicy();
		PasswordComplexityChecker passwordChecker = new PasswordComplexityChecker(passwordPolicy.getMinLength(),
															0, 
															passwordPolicy.getMinCapitals(),
															passwordPolicy.getMinNumbers(), 
															passwordPolicy.getMinSymbols());
		
		if (passwordUniquenessIsValid(user, newPassword, passwordPolicy)) {
			addActionErrorText("error.password_unique", passwordPolicy.getUniqueness()+"" );										
			return ERROR;			
		}
		if ( !passwordChecker.isValid(newPassword) ) { 
			addActionErrorText("error.password_policy", passwordChecker.getMinLength()+"",
					passwordChecker.getMinUpperAlpha()+"",
					passwordChecker.getMinLowerAlpha()+"", 
					passwordChecker.getMinNumeric()+"",
					passwordChecker.getMinPunctuation()+"" );										
			return ERROR;
		}
		
		userManager.updatePassword( user.getId(), newPassword, passwordPolicy);
		logger.info( "password updated for " + getSessionUser().getUserID() );
		addFlashMessageText( "message.passwordupdated" );			
		getSession().remove( "passwordReset" );
		return SUCCESS;			
	}	
	
	private boolean passwordUniquenessIsValid(User user, String newPassword, PasswordPolicy passwordPolicy) {
		if (passwordPolicy.getUniqueness()==null) { 
			return true;
		}
		List<String> previousPasswords = user.getPreviousPasswords();
		int start = Math.max(0, previousPasswords.size()-passwordPolicy.getUniqueness());
		previousPasswords = previousPasswords.subList(start, previousPasswords.size());
		
		return previousPasswords.contains(User.hashPassword(newPassword));
	}

	private PasswordPolicy getPasswordPolicy() {
		return tenantSettingsService.getTenantSettings().getPasswordPolicy();
	}

	public void setOriginalPassword( String originalPassword ) {
		this.originalPassword = originalPassword;
	}

	@RequiredStringValidator( message="", key="error.newpasswordrequired")
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword( String newPassword ) {
		this.newPassword = newPassword;
	}

	@RequiredStringValidator( message="", key="error.confirmasswordrequired")
	@FieldExpressionValidator( expression="confirmPassword == newPassword", message="", key="error.passwordsmustmatch" )
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword( String confirmPassword ) {
		this.confirmPassword = confirmPassword;
	}

}
