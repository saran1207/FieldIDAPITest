package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.orgs.OrgWithNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;
public class OwnerExistsValidatorTest {

	@Test
	public void test_validation_passes_when_value_is_null() {
		OwnerExistsValidator validator = new OwnerExistsValidator();
		
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_passes_when_org_exists() {
		final String orgName = "my org";
		
		OwnerExistsValidator validator = new OwnerExistsValidator() {
			@Override
			protected OrgWithNameExistsLoader createOrgExistsLoader(SecurityFilter filter) {
				return new OrgWithNameExistsLoader(filter) {
					@Override
					public Boolean load() {
						assertEquals(orgName, name);
						return true;
					}
				};
			}
		};
		
		assertTrue(validator.validate(orgName, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_fails_when_org_does_not_exist() {
		final String orgName = "my org";
		
		OwnerExistsValidator validator = new OwnerExistsValidator() {
			@Override
			protected OrgWithNameExistsLoader createOrgExistsLoader(SecurityFilter filter) {
				return new OrgWithNameExistsLoader(filter) {
					@Override
					public Boolean load() {
						assertEquals(orgName, name);
						return false;
					}
				};
			}
		};
		
		assertTrue(validator.validate(orgName, null, null, null, null).isFailed());
	}
	
}
