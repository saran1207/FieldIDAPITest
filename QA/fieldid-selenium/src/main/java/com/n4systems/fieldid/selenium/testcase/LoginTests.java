package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.Forgot;
import com.n4systems.fieldid.selenium.login.Login;

public class LoginTests extends FieldIDTestCase {

	Login login;
	Forgot forgot;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		forgot = new Forgot(selenium, misc);
	}
	
	@Test
	public void shouldBeAbleToGoToIForgotMyPasswordAndReturnToLoginWithoutResettingPassword() throws Exception {
		try {
			login.gotoIForgotMyPassword();
			forgot.gotoReturnToSignInWithoutResettingPassword();
			login.verifyLoginPage();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Ignore("No longer have a choose company option")
	@Test
	public void shouldBeAbleToChangeTheCompanyIDToAValidCompanyAndLogIn() throws Exception {
		// this has to be a valid company ID
		String companyID = getStringProperty("companyid");

		try {
			setCompany(companyID);
			login.verifyLoginPage();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void shouldBeAbleToResetPassword() throws Exception {
		String username = getStringProperty("username");

		try {
			resetMyPassword(username);
			returnToLoginPage();
			// can only verify we didn't get an Oops page
			// TODO: verify the reset password email went out
			login.verifyLoginPage();
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void returnToLoginPage() {
		forgot.gotoReturnToSignInAfterResetPassword();
	}

	private void resetMyPassword(String username) {
		login.verifyLoginPage();
		login.gotoIForgotMyPassword();
		forgot.setUserName(username);
		forgot.gotoResetPassword();
		forgot.verifyResetPassword();
	}

	@Test
	public void shouldGetAGoodWarningMessageIfIAttemptToResetPasswordWithoutInputtingAUserName() throws Exception {
		try {
			login.gotoIForgotMyPassword();
			forgot.gotoResetPassword();
			forgot.verifyUserNameIsRequired();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void shouldBeAbleToLogIntoFieldID() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");

		try {
			login.setUserName(username);
			login.setPassword(password);
			login.gotoSignIn();
			login.verifySignedIn();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
