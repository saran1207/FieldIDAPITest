package com.n4systems.fieldid.actions.subscriptions.view.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.tenant.TenantNameAvailabilityChecker;

public class SignUpRequestDecoratorTest {

	@Test
	public void uses_tenant_name_availability_checker_on_duplicate_value_validation() {
		final String tenantName = "test";
		
		SignUpRequestDecorator signupRequest = new SignUpRequestDecorator(new TenantNameAvailabilityChecker(null, null, null) {
			@Override
			public boolean isAvailable(String name) {
				assertEquals(tenantName, name);
				return true;
			}
		}, null);
		
		assertFalse(signupRequest.duplicateValueExists(tenantName));
	}
	
}
