package com.n4systems.fieldid.actions.subscriptions;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.subscriptions.view.model.SignUpRequestDecorator;
import com.n4systems.model.tenant.TenantNameAvailabilityChecker;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.util.ConfigContextRequiredTestCase;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.ActionValidatorManagerFactory;

public class VaildiationsForSignUpCrud extends ConfigContextRequiredTestCase {
	
	private ActionValidatorManager avm;
	private SignUpCrudExtension signUpCrud;

	@Before
	public void createObjectFactory() {
		ObjectFactory.setObjectFactory(new ObjectFactory());
	}
	
	@After
	public void teardownObjectFactory() {
		ObjectFactory.setObjectFactory(null);
	}
	
	@Before
	public void createValidator() {
		avm = ActionValidatorManagerFactory.getInstance();
	}
	@Before
	public void createSignUpActionReadyToValidate() {
		signUpCrud = new SignUpCrudExtension(createNiceMock(PersistenceManager.class));
		SignUpRequestDecorator signUpRequest = new SignUpRequestDecorator(new SuccessfulTenantUniqueAvailableNameLoader(), createNiceMock(SubscriptionAgent.class));
		signUpCrud.setSignUp(signUpRequest);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_validate_tenant_name_invalid_with_characters() throws Exception {
		signUpCrud.getSignUp().setTenantName("a_a");
		avm.validate(signUpCrud,"");
		
		assertThat(signUpCrud.getFieldErrors(), hasKey("signUp.tenantName"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_validate_tenant_too_short() throws Exception {
		
		signUpCrud.getSignUp().setTenantName("a");
		avm.validate(signUpCrud,"");
	    
		assertThat(signUpCrud.getFieldErrors(), hasKey("signUp.tenantName"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_validate_tenant_to_not_blank_required() throws Exception {
		signUpCrud.getSignUp().setTenantName("");
		avm.validate(signUpCrud,"");
	    
		assertThat(signUpCrud.getFieldErrors(), hasKey("signUp.tenantName"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_validate_tenant_is_required() throws Exception {
		signUpCrud.getSignUp().setTenantName(null);
		avm.validate(signUpCrud,"");
	    
		assertThat(signUpCrud.getFieldErrors(), hasKey("signUp.tenantName"));
		
		// ensure multiple validations.
		assertThat(signUpCrud.getFieldErrors().size(), greaterThan(1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_validate_tenant_as_valid() throws Exception {
		signUpCrud.getSignUp().setTenantName("agoodtenantname");
		avm.validate(signUpCrud,"");
	    
		assertThat(signUpCrud.getFieldErrors(), not(hasKey("signUp.tenantName")));
	}
	
	private final class SignUpCrudExtension extends SignUpCrud {
		private SignUpCrudExtension(PersistenceManager persistenceManager) {
			super(persistenceManager);
		}

		protected void setSignUp(SignUpRequestDecorator signUp) {
			signUpRequest = signUp;
		}
	}

	private class SuccessfulTenantUniqueAvailableNameLoader extends TenantNameAvailabilityChecker {
		@Override
		public boolean isAvailable(String name) {
			return true;
		}
	}
}
