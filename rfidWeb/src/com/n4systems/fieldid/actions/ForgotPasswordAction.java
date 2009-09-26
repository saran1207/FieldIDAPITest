package com.n4systems.fieldid.actions;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;

public class ForgotPasswordAction extends LoginAction {

	private static final Logger logger = Logger.getLogger(ForgotPasswordAction.class);
	private static final long serialVersionUID = 1L;

	private String userName;

	private String loginKey;
	private Long uniqueID;

	
	public ForgotPasswordAction(User userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
		
	}

	public String doAdd() {
		return SUCCESS;
	}

	public String doCreate() {
		if (isBlank(userName)) {
			addFieldError("userName", getText("error.usernamerequired"));
			return INPUT;
		}

		UserBean user = userManager.findUserBeanByID(getSecurityGuard().getTenantName(), userName);
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
		UserBean user = userManager.findUserByResetKey(getSecurityGuard().getTenantName(), userName, loginKey);

		if (user == null) {
			return MISSING;
		}

		logUserIn(user);
		getSession().put("passwordReset", true);
		addFlashMessageText("message.passwordresetkeyaccpeted");
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

}
