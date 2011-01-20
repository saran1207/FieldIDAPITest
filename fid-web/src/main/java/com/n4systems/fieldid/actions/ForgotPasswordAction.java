package com.n4systems.fieldid.actions;

import javax.mail.MessagingException;

import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.user.User;
import org.apache.struts2.interceptor.validation.SkipValidation;

public class ForgotPasswordAction extends SignInAction {

	private static final Logger logger = Logger.getLogger(ForgotPasswordAction.class);
	private static final long serialVersionUID = 1L;

	private String userName;
	private String loginKey;
    private String newPassword;
    private String confirmPassword;
	private Long uniqueID;

	public ForgotPasswordAction(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}

    @SkipValidation
	public String doAdd() {
		return SUCCESS;
	}

    @SkipValidation
	public String doCreate() {
		if (isBlank(userName)) {
			addFieldError("userName", getText("error.usernamerequired"));
			return INPUT;
		}

		User user = userManager.findUserBeanByID(getSecurityGuard().getTenantName(), userName);
		if (user != null) {
			try {
				userManager.createAndEmailLoginKey(user, getBaseURI());
			} catch (MessagingException e) {
				logger.error("could not send reset email", e);
				return ERROR;
			}

		}
		
		if (uniqueID != null) {
			addFlashMessageText("message.email_sent");
		}
		return SUCCESS;
	}

	public String doReset() {
        User user = userManager.findAndClearResetKey(getSecurityGuard().getTenantName(), userName, loginKey);

        if (user == null) {
            return MISSING;
        }

        userManager.updatePassword( user.getId(), newPassword );
        addFlashMessageText("message.passwordresetsuccess");
        return SUCCESS;
	}

    @SkipValidation
    public String doPrepare() {
        boolean resetKeyValid = userManager.resetKeyIsValid(getSecurityGuard().getTenantName(), userName, loginKey);

        if (!resetKeyValid) {
            return MISSING;
        }

        return SUCCESS;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}
	
	public String getU() {
		return getUserName();
	}

	public void setU(String userName) {
		setUserName(userName);
	}

	public String getK() {
		return getLoginKey();
	}

	public void setK(String loginKey) {
		setLoginKey(loginKey);
	}

	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	@RequiredStringValidator( message="", key="error.newpasswordrequired")
	@StringLengthFieldValidator( type= ValidatorType.FIELD, message = "" , key = "errors.passwordlength", minLength="5")
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
