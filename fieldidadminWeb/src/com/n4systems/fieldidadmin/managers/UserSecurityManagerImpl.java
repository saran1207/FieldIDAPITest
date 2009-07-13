package com.n4systems.fieldidadmin.managers;

public class UserSecurityManagerImpl implements UserSecurityManager {
	// XXX - ... wow .... 
	private static final String PASSWORD = "makesome$";
	private static final String USER_NAME = "n4systems";
	
	public boolean login(String username, String password) {
		return (USER_NAME.equals(username) && PASSWORD.equals(password));
	}	

}
