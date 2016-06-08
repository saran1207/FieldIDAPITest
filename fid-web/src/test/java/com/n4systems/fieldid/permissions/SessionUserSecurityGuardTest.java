package com.n4systems.fieldid.permissions;

import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import org.junit.Test;

import static org.junit.Assert.*;

public class SessionUserSecurityGuardTest extends AbstractUserSecurityTestCase {

	@Test
	public void ensure_each_method_protects_its_permission() {		
		User user = UserBuilder.anEmployee().build();
		SessionUserSecurityGuard securityGuard = null;
		
		for (int permission : Permissions.getVisibleSystemUserInspectionPermissions()) {
			user.setPermissions(permission);
			securityGuard = new SessionUserSecurityGuard(user);			
			assertTrue("Permission "+resolvePermissionName(permission), invokePermissionMethod(securityGuard, permission));
				
			BitField bitField = new BitField(Permissions.ALL);
			bitField.set(permission, false);
			user.setPermissions(bitField.intValue());
			securityGuard = new SessionUserSecurityGuard(user);
			assertFalse("Permission "+resolvePermissionName(permission), invokePermissionMethod(securityGuard, permission));
			
		}
	}
	
	private boolean invokePermissionMethod(SessionUserSecurityGuard securityGuard, int permission) {
		Boolean returnValue = null;
		try {
			returnValue = (Boolean)SessionUserSecurityGuard.class.getMethod(constructMethodName(permission)).invoke(securityGuard);
		} catch (NoSuchMethodException e) {
			fail("No method defined for permission number "+permission);
	 	} catch (Exception e) {
			fail(e.getMessage());
		} 
		return returnValue;
	}
	
}
