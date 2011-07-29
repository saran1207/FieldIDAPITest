package com.n4systems.fieldidadmin.managers;


import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;

public class UserSecurityManagerImpl implements UserSecurityManager {
	
	
	public boolean login(String username, String password) {
		ConfigurationProvider configContext = ConfigContext.getCurrentContext();
		String realUserName = configContext.getString(ConfigEntry.SYSTEM_USER_USERNAME);
		String realPassword = configContext.getString(ConfigEntry.SYSTEM_USER_PASSWORD);
		
		String hashedPassword = User.hashPassword(password);
		
		return realPassword.equals(hashedPassword) && realUserName.equals(username);
	}	

}
