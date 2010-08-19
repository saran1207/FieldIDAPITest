package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
public class OwnerExistsValidatorTest {

	@Test
	public void test_validation_passes_when_value_is_null() {
		OwnerExistsValidator validator = new OwnerExistsValidator();
		
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_passes_when_org_exists() {
		final String[] names = { "my org", "my customer", "my division" };
		
		final OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);
		
		OwnerExistsValidator validator = new OwnerExistsValidator() {
			@Override
			protected OrgByNameLoader createOrgExistsLoader(SecurityFilter filter) {
				return orgLoader;
			}
		};
		
		expect(orgLoader.setOrganizationName(names[0])).andReturn(orgLoader);
		expect(orgLoader.setCustomerName(names[1])).andReturn(orgLoader);
		expect(orgLoader.setDivision(names[2])).andReturn(orgLoader);
		expect(orgLoader.load()).andReturn(new PrimaryOrg());
		replay(orgLoader);
		
		assertTrue(validator.validate(names, null, null, null, null).isPassed());
		verify(orgLoader);
	}
	
	@Test
	public void test_validation_fails_when_org_does_not_exist() {
		final String[] names = { "my org", "my customer", "my division" };
		
		final OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);
		
		OwnerExistsValidator validator = new OwnerExistsValidator() {
			@Override
			protected OrgByNameLoader createOrgExistsLoader(SecurityFilter filter) {
				return orgLoader;
			}
		};
		
		expect(orgLoader.setOrganizationName(names[0])).andReturn(orgLoader);
		expect(orgLoader.setCustomerName(names[1])).andReturn(orgLoader);
		expect(orgLoader.setDivision(names[2])).andReturn(orgLoader);
		expect(orgLoader.load()).andReturn(null);
		replay(orgLoader);
		
		assertTrue(validator.validate(names, null, null, null, null).isFailed());
		verify(orgLoader);
	}
	
}
