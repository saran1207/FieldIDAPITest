package com.n4systems.fieldidadmin.actions;

import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.model.admin.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdminUserService adminUserService;

	private String username;
	private String password;
	
	public String doAdd() {
		return SUCCESS;
	}
	
	public String doCreate() {
		AdminUser user = adminUserService.authenticateUser(username, password);
		if (user != null) {
			getSession().setAdminAuthenticated(true);
			getSession().setAdminUser(user);
		}
		return SUCCESS;
	}
	
	public String doDelete() {
		getSession().setAdminAuthenticated(false);
		getSession().setAdminUser(null);
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
	
}
