package com.n4systems.security;

import com.n4systems.model.user.User;
import org.junit.Test;

import static org.junit.Assert.assertFalse;



public class PermissionsTest {

	@Test
	public void should_allow_access_to_with_no_permission_specified() throws Exception {
		User user = new User();
		user.setPermissions(Permissions.ALL);
		
		assertFalse(Permissions.hasOneOf(user, Permissions.NO_PERMISSIONS));
	}
}
