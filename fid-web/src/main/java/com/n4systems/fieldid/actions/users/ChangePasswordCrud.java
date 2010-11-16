package com.n4systems.fieldid.actions.users;

import com.n4systems.taskscheduling.task.EventScheduleNotificationTask;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.user.User;
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
		
		updatePassword();
		refreshSessionUser();
		addFlashMessageText( "message.passwordupdated" );
		return SUCCESS;
	}

	protected void updatePassword() {
		if( user != null ) {
			userManager.updatePassword( user.getId(), newPassword );
			logger.info( "password updated for " + getSessionUser().getUserID() );
			
			getSession().remove( "passwordReset" );
		}
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
