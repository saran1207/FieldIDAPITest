package com.n4systems.fieldid.permissions;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.security.Permissions;

public class UserSecurityGuardTest extends AbstractUserSecurityTestCase {

	@Test
	public void should_have_a_method_for_every_visible_permission() {
		Class<UserSecurityGuard> interfaceDefinition = UserSecurityGuard.class;
		
		for (int permission : Permissions.getVisibleSystemUserEventPermissions()) {
			if (resolvePermissionName(permission) == null) {
				fail("No permission name setup for permission number "+permission);
			} else {
				try {
					interfaceDefinition.getMethod(constructMethodName(permission));
				} catch (NoSuchMethodException e) {
					fail("No method defined for permission "+resolvePermissionName(permission));
				}
			}
		}
	}
	
}
