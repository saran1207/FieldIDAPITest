package com.n4systems.fieldid.permissions;

import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.builders.UserBuilder;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;

public class SessionUserSecurityGuardTest extends AbstractUserSecurityTestCase {

	@Test
	public void ensure_each_method_protects_its_permission() {		
		UserBean user = UserBuilder.anEmployee().build();
		SessionUserSecurityGuard securityGuard = null;
		
		for (int permission : Permissions.getVisibleSystemUserPermissions()) {
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
