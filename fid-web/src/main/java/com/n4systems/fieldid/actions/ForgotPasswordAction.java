package com.n4systems.fieldid.actions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.handler.password.PasswordHelper;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

public class ForgotPasswordAction extends LoginAction {

	private static final Logger logger = Logger.getLogger(ForgotPasswordAction.class);
	private static final long serialVersionUID = 1L;

	private String userName;
	private String loginKey;
    private String newPassword;
    private String confirmPassword;
	private Long uniqueID;
	
	@Autowired
	private TenantSettingsService tenantSettingsService;
	
	private PasswordPolicy passwordPolicy;
	
	public ForgotPasswordAction(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}

    @Override
	@SkipValidation
	public String doAdd() {
		return SUCCESS;
	}

    @Override
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
		User user = userManager.findUserToReset(getSecurityGuard().getTenantName(), userName, loginKey);

        if (user == null) {
            return MISSING;	
        }

        PasswordHelper passwordHelper = new PasswordHelper(getPasswordPolicy());
        if (!passwordHelper.isPasswordUnique(user, newPassword)) {
        	addFlashError(getText("error.password_unique", new String[] {getPasswordPolicy().getUniqueness()+""}) );
        	return INPUT; 
        }
        
		if (!passwordHelper.isValidPassword(newPassword)) {
			PasswordPolicy policy = passwordHelper.getPasswordPolicy();
			addFlashError(getText("error.password_policy", new String[] { 
					policy.getMinLength()+"",
					policy.getMinCapitals()+"",
					policy.getMinNumbers()+"",
					policy.getMinSymbols()+""} ));
			return INPUT;
		}
        
        userManager.updatePassword( user.getId(), newPassword, getPasswordPolicy());
        addFlashMessageText("message.passwordresetsuccess");
        return SUCCESS;
	}

	private PasswordPolicy getPasswordPolicy() {
		if (passwordPolicy==null) { 
			passwordPolicy = tenantSettingsService.getTenantSettings().getPasswordPolicy(); 
		}
		return passwordPolicy; 
	}

	@SkipValidation
	public String doExpirePasswordReset() {
        boolean resetKeyValid = userManager.resetKeyIsValid(getSecurityGuard().getTenantName(), userName, loginKey);

        if (!resetKeyValid) {
            return MISSING;
        }		
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

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
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
