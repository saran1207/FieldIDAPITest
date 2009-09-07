package com.n4systems.model.api;

import rfid.ejb.entity.UserBean;

public interface HasUser extends HasTenant {
	public UserBean getUser();
	public void setUser(UserBean user);
}
