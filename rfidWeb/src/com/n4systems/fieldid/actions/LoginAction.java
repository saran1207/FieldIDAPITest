package com.n4systems.fieldid.actions;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.fieldid.utils.UrlArchive;

public class LoginAction extends AbstractAction {

	private static final Logger logger = Logger.getLogger(LoginAction.class);
	private static final long serialVersionUID = 1L;

	protected User userManager;

	private String userName;
	private String password;
	private String secureRfid;
	private boolean rememberMe;

	private String previousUrl;

	private boolean normalLogin = true;

	public LoginAction(User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@SkipValidation
	public String doTest() {
		if (isLoggedIn()) {
			return "loggedIn";
		}
		return "noSession";
	}

	@SkipValidation
	public String doAdd() {
		if (isLoggedIn()) {
			return "alreadyLoggedIn";
		}

		checkRememberMe();

		return SUCCESS;
	}

	private void checkRememberMe() {
		if (userName == null) {
			userName = CookieFactory.findCookieValue("userName", getServletRequest());
			if (CookieFactory.findCookieValue("loginTypeSecurityCard", getServletRequest()) != null) {
				normalLogin = false;
			}
			if (userName != null) {
				rememberMe = true;
			}
		}
	}

	public String doCreate() {
		UserBean loginUser = null;

		if (validateForm()) {
			if (normalLogin) {
				loginUser = userManager.findUser(getSecurityGuard().getTenantName(), userName, password);
			} else {
				loginUser = userManager.findUser(getSecurityGuard().getTenantName(), secureRfid);
			}
			if (loginUser != null) {
				logUserIn(loginUser);
				
				if (loginUser.isAdmin()) {
					return "eula";
				}
				
				if (previousUrl != null) {
					return "redirect";
				}
				return SUCCESS;
			}
			addActionError(getText("error.loginfailure"));
		}
		return INPUT;
	}

	private boolean validateForm() {
		boolean passed = true;
		if (normalLogin) {
			if (isBlank(userName)) {
				addFieldError("userName", getText("error.usernamerequired"));
				passed = false;
			}

			if (isBlank(password)) {
				addFieldError("password", getText("error.passwordrequired"));
				passed = false;
			}
		} else {
			if (isBlank(secureRfid)) {
				addFieldError("secureRfid", getText("error.securerfidrequired"));
				passed = false;
			}
		}
		return passed;
	}

	@SkipValidation
	public String doDelete() {
		clearSession();
		return SUCCESS;
	}

	private void clearSession() {
		// grab SecuirtyGuard
		SystemSecurityGuard securityGuard = getSecurityGuard();
		getSession().clear();
		// if the security guard gets cleared here we will no longer know the tenant context.
		
		// restore securityGuard
		getSession().setSecurityGuard(securityGuard);		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	protected void logUserIn(UserBean loginUser) {
		fetchPerviousUrl();
		clearSession();
		loadSessionUser(loginUser.getUniqueID());
		rememberMe();
		logger.info(getLogLinePrefix() + "Login: " + userName + " of " + getSecurityGuard().getTenantName());
	}

	private void fetchPerviousUrl() {
		UrlArchive urlArchive =  new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession());
		previousUrl = urlArchive.fetchUrl();
		urlArchive.clearUrl();
	}

	private void rememberMe() {
		if (rememberMe) {
			getServletResponse().addCookie(CookieFactory.createCookie("userName", userName, CookieFactory.TTL_DEFAULT));
			if (normalLogin) {
				getServletResponse().addCookie(CookieFactory.createDeleteCookie("loginTypeSecurityCard"));
			} else {
				getServletResponse().addCookie(CookieFactory.createCookie("loginTypeSecurityCard", "useSecurityCard", CookieFactory.TTL_DEFAULT));
			}
		} else {
			getServletResponse().addCookie(CookieFactory.createDeleteCookie("userName"));
			getServletResponse().addCookie(CookieFactory.createDeleteCookie("loginTypeSecurityCard"));
		}
	}

	

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public boolean isNormalLogin() {
		return normalLogin;
	}

	public String getSecureRfid() {
		return secureRfid;
	}

	public void setSecureRfid(String secureRfid) {
		this.secureRfid = secureRfid;
	}

	public void setNormalLogin(boolean normalLogin) {
		this.normalLogin = normalLogin;
	}

	public String getPreviousUrl() {
		return previousUrl;
	}

}
