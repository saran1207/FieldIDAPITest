package com.n4systems.fieldid.selenium.testcase.userregistration;

import java.util.Date;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.Misc;

public class UserRegistrationTest extends FieldIDTestCase {

	
	

	@Test
	public void should_show_all_values_entered_on_the_registration_form_when_viewing_request_in_the_system() throws Exception {
		String userId = "u_" + new Date().getTime();
		registerUser(userId);
		
		goToUserRequests();
		openRequestFromUser(userId);
		
		verifyRegistrationInformationIsShown(userId);
	}

	private void openRequestFromUser(String userId) {
		selenium.click("css=#viewRequest_" + userId);
		selenium.waitForPageToLoad(Misc.DEFAULT_TIMEOUT);
	}

	private void goToUserRequests() {
		Login loginPage = new Login(selenium, misc);
		loginPage.gotoSignInPage();
		
		loginPage.signInWithSystemAccount();
		
		selenium.openAndWaitForPageLoad("/fieldid/setup.action");
		selenium.clickAndWaitForPageLoad("link=Manage User Registrations");
	}

	private void verifyRegistrationInformationIsShown(String userId) {
		verifyTrue(selenium.isTextPresent(userId));
		verifyTrue(selenium.isTextPresent("dev@fieldid.com"));
		verifyTrue(selenium.isTextPresent("Test"));
		verifyTrue(selenium.isTextPresent("User"));
		verifyTrue(selenium.isTextPresent("some position"));
		verifyTrue(selenium.isTextPresent("Toronto"));
		verifyTrue(selenium.isTextPresent("Canada:Ontario - Toronto"));
		verifyTrue(selenium.isTextPresent("company_name"));
		verifyTrue(selenium.isTextPresent("647-202-2789"));
		verifyTrue(selenium.isTextPresent("This is a comment"));
	}

	private void registerUser(String userId) throws InterruptedException {
		selenium.open("/fieldid/registerUser.action");
		selenium.type("companyName", "company_name");
		selenium.type("registerUserCreate_firstName", "Our test user");
		selenium.type("registerUserCreate_firstName", "Test");
		selenium.type("registerUserCreate_lastName", "User");
		selenium.type("registerUserCreate_emailAddress", "dev@fieldid.com");
		selenium.type("registerUserCreate_city", "Toronto");
		selenium.select("countryId", "value=CA");
		selenium.waitForAjax(Misc.DEFAULT_TIMEOUT);
		selenium.select("tzlist", "label=Ontario - Toronto");
		selenium.type("registerUserCreate_position", "some position");
		selenium.type("registerUserCreate_phoneNumber", "647-202-2789");
		selenium.type("registerUserCreate_comment", "This is a comment");
		
		selenium.type("registerUserCreate_userId", userId);
		selenium.type("registerUserCreate_password", "makemore$");
		selenium.type("registerUserCreate_passwordConfirmation", "makemore$");
		selenium.click("registerUserCreate_save");
		selenium.waitForPageToLoad(Misc.DEFAULT_TIMEOUT);
	}
}
