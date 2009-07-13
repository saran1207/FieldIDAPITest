package com.n4systems.webservice.server.bundles;

import java.io.Serializable;

public class AuthBundle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tenantName;
	private String userName;	
	private String password;
	
	public AuthBundle() {}
	
	public AuthBundle(String tenantName, String userName, String userPassword) {
		this.tenantName = tenantName;
		this.userName = userName;
		this.password = userPassword;
	}
 
	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
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

	public void setPassword(String userPassword) {
		this.password = userPassword;
	}
	
}
