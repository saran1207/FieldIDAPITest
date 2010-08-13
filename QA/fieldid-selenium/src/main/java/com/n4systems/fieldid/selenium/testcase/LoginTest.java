package com.n4systems.fieldid.selenium.testcase;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.page.Forgot;
import com.n4systems.fieldid.selenium.login.page.Login;

public class LoginTest extends FieldIDTestCase {

	Login login;
	Forgot forgot;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		forgot = new Forgot(selenium, misc);
	}
	
	@Test
	public void shouldBeAbleToGoToIForgotMyPasswordAndReturnToLoginWithoutResettingPassword() throws Exception {
		login.gotoIForgotMyPassword();
		forgot.gotoReturnToSignInWithoutResettingPassword();
		login.verifyLoginPageWithNoErrors();
	}
	
	@Test
	public void shouldBeAbleToChangeTheCompanyIDToAValidCompanyAndLogIn() throws Exception {
		// this has to be a valid company ID
		String companyID = getStringProperty("companyid");

		setCompany(companyID);
		login.verifyLoginPageWithNoErrors();
	}
	
	@Test
	public void shouldBeAbleToResetPassword() throws Exception {
		String username = getStringProperty("username");

		resetMyPassword(username);
		returnToLoginPage();
		// can only verify we didn't get an Oops page
		// XXX: verify the reset password email went out
		login.verifyLoginPageWithNoErrors();
	}
	
	private void returnToLoginPage() {
		forgot.gotoReturnToSignInAfterResetPassword();
	}

	private void resetMyPassword(String username) {
		login.verifyLoginPageWithNoErrors();
		login.gotoIForgotMyPassword();
		forgot.setUserName(username);
		forgot.gotoResetPassword();
		forgot.verifyResetPassword();
	}

	@Test
	public void shouldGetAGoodWarningMessageIfIAttemptToResetPasswordWithoutInputtingAUserName() throws Exception {
		login.gotoIForgotMyPassword();
		forgot.gotoResetPassword();
		forgot.verifyUserNameIsRequired();
	}
	
	@Test
	public void shouldBeAbleToLogIntoFieldID() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("companyid");

		setCompany(company);
		login.setUserName(username);
		login.setPassword(password);
		login.gotoSignIn();
		login.verifySignedIn();
	}
	
}
