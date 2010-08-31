package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.orgs.internal.InternalOrgWithNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class ParentOrgResolutionValidatorTest {

	@Test
	public void test_validation_passes_when_view_is_a_division() {
		ParentOrgResolutionValidator validator = new ParentOrgResolutionValidator();
		
		assertTrue(validator.validate(null, FullExternalOrgView.newDivision(), null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_fails_when_view_is_a_customer_and_parent_org_is_null() {
		ParentOrgResolutionValidator validator = new ParentOrgResolutionValidator();
		
		assertTrue(validator.validate(null, FullExternalOrgView.newCustomer(), null, null, null).isFailed());
	}
	
	@Test
	public void test_validation_passes_when_org_exists() {
		final String orgName = "My Org";
		
		FullExternalOrgView view = FullExternalOrgView.newCustomer();
		view.setParentOrg(orgName);
		
		ParentOrgResolutionValidator validator = new ParentOrgResolutionValidator() {
			protected InternalOrgWithNameExistsLoader createOrgExistsLoader(SecurityFilter filter) {
				return new InternalOrgWithNameExistsLoader(filter) {
					@Override
					public Boolean load() {
						assertEquals(orgName, this.name);
						return true;
					}
				};
			}
		};
		
		assertTrue(validator.validate(null, view, null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_fails_when_org_does_not_exist() {
		final String orgName = "My Org";
		
		FullExternalOrgView view = FullExternalOrgView.newCustomer();
		view.setParentOrg(orgName);
		
		ParentOrgResolutionValidator validator = new ParentOrgResolutionValidator() {
			protected InternalOrgWithNameExistsLoader createOrgExistsLoader(SecurityFilter filter) {
				return new InternalOrgWithNameExistsLoader(filter) {
					@Override
					public Boolean load() {
						assertEquals(orgName, this.name);
						return false;
					}
				};
			}
		};
		
		assertTrue(validator.validate(null, view, null, null, null).isFailed());
	}
}
