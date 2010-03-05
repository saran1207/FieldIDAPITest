package com.n4systems.fieldid.actions;

import com.n4systems.fieldid.utils.CookieFactory;


public class SignIn {
	private String userName;
	private String password;
	private String secureRfid;
	private boolean rememberMe;
	private boolean normalLogin = true;

	public SignIn() {
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName != null ? userName.trim() : userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password != null ? password.trim() : password;
	}

	public String getSecureRfid() {
		return secureRfid;
	}

	public void setSecureRfid(String secureRfid) {
		this.secureRfid = secureRfid != null ? secureRfid.trim() : secureRfid;
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

	public void setNormalLogin(boolean normalLogin) {
		this.normalLogin = normalLogin;
	}

	public boolean isValid(WebAction action) {
		boolean passed = true;
		if (isNormalLogin()) {
			if (userName == null) {
				action.addFieldError("userName", action.getText("error.usernamerequired"));
				passed = false;
			}

			if (getPassword() == null) {
				action.addFieldError("password", action.getText("error.passwordrequired"));
				passed = false;
			}
		} else {
			if (getSecureRfid() == null) {
				action.addFieldError("secureRfid", action.getText("error.securerfidrequired"));
				passed = false;
			}
		}
		return passed;
	}
	
	protected boolean isBlank( String testString ) {
		return ( testString == null || testString.trim().length() == 0  );
	}

	public void populateFromRememberMe(CookieFactory cookieFactroy) {
		if (userName == null) {
			userName = cookieFactroy.findCookieValue("userName");
			if (cookieFactroy.findCookieValue("loginTypeSecurityCard") != null) {
				normalLogin = false;
			}
			if (userName != null) {
				rememberMe = true;
			}
		}
		
	}

	public void storeRememberMe(CookieFactory cookieFacotry) {
		if (rememberMe) {
			cookieFacotry.addCookie(CookieFactory.createCookie("userName", getUserName(), CookieFactory.TTL_DEFAULT));
			if (normalLogin) {
				cookieFacotry.removeCookie("loginTypeSecurityCard");
			} else {
				cookieFacotry.addCookie(CookieFactory.createCookie("loginTypeSecurityCard", "useSecurityCard", CookieFactory.TTL_DEFAULT));
			}
		} else {
			cookieFacotry.removeCookie("userName");
			cookieFacotry.removeCookie("loginTypeSecurityCard");
		}
		
	}
}