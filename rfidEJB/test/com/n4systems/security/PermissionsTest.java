package com.n4systems.security;

import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;


public class PermissionsTest {

	@Test
	public void should_allow_access_to_with_no_permission_specified() throws Exception {
		UserBean user = new UserBean();
		user.setPermissions(Permissions.ALL);
		
		//assertTrue(Permissions.hasOneOf(user, Permissions.NO_PERMISSIONS));
	}
}
