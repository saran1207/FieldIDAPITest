package com.n4systems.fieldid.selenium.login;

import com.n4systems.fieldid.selenium.datatypes.RegisterNewUserData;
import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public class RegisterNewUserPage extends SeleneseTestBase {

	private static final String stringForPageTitle = "Field ID : Safety Management - Register a New User";
	private static final String requestAnAccountLinkLocator = "css=#requestAccountButton > a";
	private static final String companyNameFieldLocator = "css=#companyName";
	private static final String firstNameFieldLocator = "css=#registerUserCreate_firstName";
	private static final String lastNameFieldLocator = "css=#registerUserCreate_lastName";
	private static final String emailFieldLocator = "css=#registerUserCreate_emailAddress";
	private static final String countryTimeZoneSelectLocator = "css=#registerUserCreate_countryId";
	private static final String timeZoneSelectLocator = "css=#tzlist";
	private static final String positionFieldLocator = "css=#registerUserCreate_position";
	private static final String phoneNumberFieldLocator = "css=#registerUserCreate_phoneNumber";
	private static final String commentsFieldLocator = "css=#registerUserCreate_comment";
	private static final String userNameFieldLocator = "css=#registerUserCreate_userId";
	private static final String passwordFieldLocator = "css=#registerUserCreate_password";
	private static final String verifyPasswordFieldLocator = "css=#registerUserCreate_passwordConfirmation";
	private static final String sendRequestButtonLocator = "css=#registerUserCreate_save";
	private static final String sentenceTwoConfirmationString = "The administrator of the account will need to verify your information before you can log in.";
	private static final String sentenceThreeConfirmationString = "You will receive an email when your login has been activated.";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public RegisterNewUserPage(Selenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().startsWith(stringForPageTitle));
	}
	
	/**
	 * Go to Request an Account. This is requesting a customer account with
	 * the current tenant. If the tenant has PartnerCenter enabled, this link
	 * is available. Otherwise, this method will throw an exception.
	 */
	public void gotoRequestAnAccount() {
		assertTrue("Could not find the link to Request an Account. Maybe this tenant doesn't have PartnerCenter enabled.", selenium.isElementPresent(requestAnAccountLinkLocator));
		selenium.click(requestAnAccountLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	public void assertRegisterNewUserPagerContent() {
		// company Details
		selenium.isElementPresent(companyNameFieldLocator);
		selenium.isElementPresent(firstNameFieldLocator);
		selenium.isElementPresent(lastNameFieldLocator);
		selenium.isElementPresent(emailFieldLocator);
		selenium.isElementPresent(countryTimeZoneSelectLocator);
		selenium.isElementPresent(timeZoneSelectLocator);
		selenium.isElementPresent(positionFieldLocator);
		selenium.isElementPresent(phoneNumberFieldLocator);
		selenium.isElementPresent(commentsFieldLocator);
		
		// Sign In Details
		selenium.isElementPresent(userNameFieldLocator);
		selenium.isElementPresent(passwordFieldLocator);
		selenium.isElementPresent(verifyPasswordFieldLocator);
	}
	
	/**
	 * Fills in the form for Register a New user. If fields of the input are
	 * null they will be skipped. If you wish to clear a field, set it to "".
	 * Otherwise, it will attempt to fill the field with the value. For the
	 * country and time zone select lists, we use pattern matching. That is,
	 * if you pass in "Toronto" for the time zone, it will select the first
	 * option which has "Toronto" in the visible text. i.e. "Ontario - Toronto"
	 * would be a match.
	 * 
	 * @param nu information for filling out the form.
	 */
	public void setRegisterNewUser(RegisterNewUserData nu) {
		if(nu.getCompanyName() != null) {
			selenium.type(companyNameFieldLocator, nu.getCompanyName());
		}
		if(nu.getFirstName() != null) {
			selenium.type(firstNameFieldLocator, nu.getFirstName());
		}
		if(nu.getLastName() != null) {
			selenium.type(lastNameFieldLocator, nu.getLastName());
		}
		if(nu.getEmailAddress() != null) {
			selenium.type(emailFieldLocator, nu.getEmailAddress());
		}
		if(nu.getCountry() != null) {
			String regexp = "label=/" + nu.getCountry() + "/";
			selenium.select(countryTimeZoneSelectLocator, regexp);
		}
		if(nu.getTimeZone() != null) {
			String regexp = "label=/" + nu.getTimeZone() + "/";
			selenium.select(timeZoneSelectLocator, regexp);
		}
		if(nu.getPosition() != null) {
			selenium.type(positionFieldLocator, nu.getPosition());
		}
		if(nu.getPhoneNumber() != null) {
			selenium.type(phoneNumberFieldLocator, nu.getPhoneNumber());
		}
		if(nu.getComments() != null) {
			selenium.type(commentsFieldLocator, nu.getComments());
		}
		if(nu.getUserName() != null) {
			selenium.type(userNameFieldLocator, nu.getUserName());
		}
		if(nu.getPassword() != null) {
			selenium.type(passwordFieldLocator, nu.getPassword());
			selenium.type(verifyPasswordFieldLocator, nu.getPassword());
		}
	}
	
	/**
	 * This method is called when you know the information in the form is
	 * wrong and will generate an error. After calling this method I expect
	 * there to be an error message on the page.
	 */
	public void gotoSendRequestFail() {
		selenium.isElementPresent(sendRequestButtonLocator);
		selenium.click(sendRequestButtonLocator);
	}

	/**
	 * Submits the request for a new customer account. This method assumes
	 * everything goes okay and you arrive at the confirmation page. If you
	 * call this method and there is an error on the form or it does not send
	 * the request for an account, this method will fail.
	 * 
	 * @param userName the User Name used in the form submitted.
	 * @param emailAddress the Email Address used in the form submitted.
	 */
	public void gotoSendRequest(String userName, String emailAddress) {
		selenium.isElementPresent(sendRequestButtonLocator);
		selenium.click(sendRequestButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// This goes to a new page but it has the same title as previous page.
		assertPageTitle();
		assertRegisterNewUserSuccess(userName, emailAddress);
	}

	private void assertRegisterNewUserSuccess(String userName, String emailAddress) {
		String userNameConfirmationString = "user name " + userName;
		String emailAddressConfirmationString = "email address " + emailAddress;
		selenium.isTextPresent(userNameConfirmationString);
		selenium.isTextPresent(emailAddressConfirmationString);
		selenium.isTextPresent(sentenceTwoConfirmationString);
		selenium.isTextPresent(sentenceThreeConfirmationString);
	}
}
