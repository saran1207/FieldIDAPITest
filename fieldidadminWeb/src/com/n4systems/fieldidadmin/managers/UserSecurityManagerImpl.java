package com.n4systems.fieldidadmin.managers;

import rfid.ejb.entity.UserBean;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class UserSecurityManagerImpl implements UserSecurityManager {
	
	
	public boolean login(String username, String password) {
		ConfigContext configContext = ConfigContext.getCurrentContext();
		String realUserName = configContext.getString(ConfigEntry.SYSTEM_USER_USERNAME);
		String realPassword = configContext.getString(ConfigEntry.SYSTEM_USER_PASSWORD);
		
		String hashedPassword = UserBean.hashPassword(password);
		
		return realPassword.equals(hashedPassword) && realUserName.equals(username);
	}	

}
