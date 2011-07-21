package com.n4systems.fieldid.actions.users;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.wicket.pages.setup.PasswordPolicy;
import com.n4systems.model.user.User;
import com.n4systems.security.PasswordComplexityChecker;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


public class ChangePasswordCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger( ChangePasswordCrud.class );
	
	protected UserManager userManager;
	protected User user;
	
	
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
			
		// FIXME DD : add password checking here.  refactor use of PasswordPolicy object - merge with mark's config modeling changes. 
		PasswordPolicy passwordPolicy = getPasswordPolicy();
		PasswordComplexityChecker passwordChecker = new PasswordComplexityChecker(passwordPolicy.getMinLength(),
															0, 
															passwordPolicy.getMinCapitals(),
															passwordPolicy.getMinNumbers(), 
															passwordPolicy.getMinSymbols());
		if ( passwordChecker.isValid(newPassword) ) { 
			userManager.updatePassword( user.getId(), newPassword );
			logger.info( "password updated for " + getSessionUser().getUserID() );
			addFlashMessageText( "message.passwordupdated" );			
			getSession().remove( "passwordReset" );
			return SUCCESS;
		} else {		
			addActionErrorText("error.password_policy", passwordChecker.getMinLength()+"",
					passwordChecker.getMinUpperAlpha()+"",
					passwordChecker.getMinLowerAlpha()+"", 
					passwordChecker.getMinNumeric()+"",
					passwordChecker.getMinPunctuation()+"" );										
			return ERROR;
		}
			
	}	
	
	private PasswordPolicy getPasswordPolicy() {
		PasswordPolicy passwordPolicy = new PasswordPolicy();
		passwordPolicy.setMinCapitals(1);
		passwordPolicy.setMinLength(6);
		passwordPolicy.setMinNumbers(1);
		passwordPolicy.setMinSymbols(0);		
		return passwordPolicy;
	}

	public void setOriginalPassword( String originalPassword ) {
		this.originalPassword = originalPassword;
	}

	@RequiredStringValidator( message="", key="error.newpasswordrequired")
	@StringLengthFieldValidator( type=ValidatorType.FIELD, message = "" , key = "errors.passwordlength", minLength="5")
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
