package com.n4systems.fieldid.selenium.testcase.signup;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;


public class TenantNameValidationTest extends FieldIDTestCase {

	private static final String TENANT_NAME_INPUT_WITH_ERROR = "css=#mainContent_signUp_tenantName.inputError";
	private CreateAccount createAccountPage;
	private CreateTenant signUpForm;
	
	
	@Before
	public void getToSignUpPage() {
		selenium.open("/fieldid/public/signUpPackages.action");
		selenium.waitForPageToLoad();
		new SignUpPackages(selenium, misc).gotoSignUpNow(SignUpPackages.packageTypeBasic);
	}
	
	@Before
	public void createSignUpPageAndForm() {
		createAccountPage = new CreateAccount(selenium, misc);
		signUpForm = new CreateTenant("alex", "makemore$", "test Company", "a");
	}
	
	@Test
	public void should_see_the_tenant_name_too_short_gets_validation_error() throws Exception {
		signUpForm.setSiteAddress("aa");
		createAccountPage.setCreateYourAccountForm(signUpForm);
		
		createAccountPage.submitCreateYourAccountForm();
		
		verifyTrue(selenium.isElementPresent(TENANT_NAME_INPUT_WITH_ERROR));
		verifyTrue(selenium.isTextPresent("The domain name must be at between 3 and"));
	}
	
	
	@Test
	public void should_see_the_tenant_name_blank_gets_a_required_validation_error() throws Exception {
		signUpForm.setSiteAddress("");
		createAccountPage.setCreateYourAccountForm(signUpForm);
		
		createAccountPage.submitCreateYourAccountForm();
		
		verifyTrue(selenium.isElementPresent(TENANT_NAME_INPUT_WITH_ERROR));
		verifyTrue(selenium.isTextPresent("You must choose a domain name"));
	}
	
	
	@Test
	public void should_see_the_tenant_name_with_bad_characters_gets_a_validation_error() throws Exception {
		signUpForm.setSiteAddress("msachesnut.msanet");
		createAccountPage.setCreateYourAccountForm(signUpForm);
		selenium.setSpeed("1000");
		createAccountPage.submitCreateYourAccountForm();
		
		verifyTrue(selenium.isElementPresent(TENANT_NAME_INPUT_WITH_ERROR));
		verifyTrue(selenium.isTextPresent("The domain name is not in the correct format. You may only user letters, numbers and dashes"));
	}
	
	
	@Test
	public void should_see_the_tenant_name_that_already_exists_in_the_system_gets_a_validation_error() throws Exception {
		signUpForm.setSiteAddress("fieldid");
		createAccountPage.setCreateYourAccountForm(signUpForm);
		
		createAccountPage.submitCreateYourAccountForm();
		
		verifyTrue(selenium.isElementPresent(TENANT_NAME_INPUT_WITH_ERROR));
		verifyTrue(selenium.isTextPresent("This domain name is already in use"));
	}
	
}
