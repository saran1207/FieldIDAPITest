package com.n4systems.fieldid.selenium.testcase.nonloggedin;

import static com.n4systems.fieldid.selenium.asserts.FieldIdAssert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.CreateAccount;
import com.n4systems.fieldid.selenium.login.SignUpPackages;

public class SignUpProcessBrandingTest extends FieldIDTestCase {
	private SignUpPackages signUpPackagePage;
	private CreateAccount createAccountPage;
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		signUpPackagePage = new SignUpPackages(selenium, misc);
		createAccountPage = new CreateAccount(selenium, misc);
	}

	
	@Test
	public void should_have_a_system_logo_on_plans_and_pricing_page() throws Exception {
		selenium.open("/fieldid/public/signUpPackages.action");
		
		signUpPackagePage.verifySignUpPackagePage();
		
		assertSystemLogoIsUsed(selenium);
	}
	
	@Test
	public void should_have_a_system_logo_on_sign_up_form() throws Exception {
		selenium.open("/fieldid/public/signUpPackages.action");
		signUpPackagePage.gotoSignUpNow("Free");
		createAccountPage.verifyCreateAccountForm(false);
		
		assertSystemLogoIsUsed(selenium);
	}
	
	
}
