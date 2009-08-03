package com.n4systems.fieldidadmin.actions;

import com.n4systems.fieldidadmin.managers.UserSecurityManager;
import com.n4systems.fieldidadmin.utils.Constants;

public class AuthenticationAction extends AbstractAdminAction {


	private static final long serialVersionUID = 1L;

	private UserSecurityManager userSecurityManager;
	
	
	private String username;
	private String password;
	
	public String add() {
		return SUCCESS;
	}
	
	public String doCreate() {
		
		if (userSecurityManager.login(username, password)) {
			getSession().put(Constants.SESSION_USER, "test");
		}
		
		return SUCCESS;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserSecurityManager getUserSecurityManager() {
		return userSecurityManager;
	}

	public void setUserSecurityManager(UserSecurityManager userSecurityManager) {
		this.userSecurityManager = userSecurityManager;
	}
	
}
