package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.api.Exportable;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdExistsLoader;
public class ExternalOrgGlobalIdValidatorTest {

	@Test
	public void test_validate_passes_when_global_id_is_null() {
		ExternalOrgGlobalIdValidator validator = new ExternalOrgGlobalIdValidator();
		
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_passes_if_view_is_not_customer_or_division() {
		ExternalOrgGlobalIdValidator validator = new ExternalOrgGlobalIdValidator();
		
		FullExternalOrgView view = new FullExternalOrgView();
		view.setType("Z");
		
		assertTrue(validator.validate(null, view, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_passes_for_customer_when_org_exists() {
		String globalId = "12345";
		
		ExternalOrgGlobalIdValidator validator = new ExternalOrgGlobalIdValidator() {
			protected GlobalIdExistsLoader getGlobalIdExistsLoader(Class<? extends Exportable> clazz, SecurityFilter filter) {
				return new GlobalIdExistsLoader(filter, clazz) {
					@Override
					public Boolean load() {
						assertEquals(globalId, this.globalId);
						assertEquals(CustomerOrg.class, this.entityClass);
						return true;
					}
				};
			}
		};
		
		assertTrue(validator.validate(globalId, FullExternalOrgView.newCustomer(), null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_passes_for_division_when_org_exists() {
		String globalId = "12345";
		
		ExternalOrgGlobalIdValidator validator = new ExternalOrgGlobalIdValidator() {
			protected GlobalIdExistsLoader getGlobalIdExistsLoader(Class<? extends Exportable> clazz, SecurityFilter filter) {
				return new GlobalIdExistsLoader(filter, clazz) {
					@Override
					public Boolean load() {
						assertEquals(globalId, this.globalId);
						assertEquals(DivisionOrg.class, this.entityClass);
						return true;
					}
				};
			}
		};
		
		assertTrue(validator.validate(globalId, FullExternalOrgView.newDivision(), null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_fails_for_customer_when_org_does_not_exist() {
		String globalId = "12345";
		
		ExternalOrgGlobalIdValidator validator = new ExternalOrgGlobalIdValidator() {
			protected GlobalIdExistsLoader getGlobalIdExistsLoader(Class<? extends Exportable> clazz, SecurityFilter filter) {
				return new GlobalIdExistsLoader(filter, clazz) {
					@Override
					public Boolean load() {
						assertEquals(globalId, this.globalId);
						assertEquals(CustomerOrg.class, this.entityClass);
						return false;
					}
				};
			}
		};
		
		assertTrue(validator.validate(globalId, FullExternalOrgView.newCustomer(), null, null, null).isFailed());
	}
	
	@Test
	public void test_validate_fails_for_division_when_org_does_not_exist() {
		String globalId = "12345";
		
		ExternalOrgGlobalIdValidator validator = new ExternalOrgGlobalIdValidator() {
			protected GlobalIdExistsLoader getGlobalIdExistsLoader(Class<? extends Exportable> clazz, SecurityFilter filter) {
				return new GlobalIdExistsLoader(filter, clazz) {
					@Override
					public Boolean load() {
						assertEquals(globalId, this.globalId);
						assertEquals(DivisionOrg.class, this.entityClass);
						return false;
					}
				};
			}
		};
		
		assertTrue(validator.validate(globalId, FullExternalOrgView.newDivision(), null, null, null).isFailed());
	}
}
