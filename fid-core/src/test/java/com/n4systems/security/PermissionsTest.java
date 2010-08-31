package com.n4systems.security;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.user.User;



public class PermissionsTest {

	@Test
	public void should_allow_access_to_with_no_permission_specified() throws Exception {
		User user = new User();
		user.setPermissions(Permissions.ALL);
		
		assertFalse(Permissions.hasOneOf(user, Permissions.NO_PERMISSIONS));
	}
}
