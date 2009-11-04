package com.n4systems.fieldid.selenium.login;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.DefaultSelenium;

public class SignUpAddPage {

	private DefaultSelenium selenium;
	private static final Object stringForPageTitle = "failure";
	private static final String signUpNowFreeLinkLocator = "//TR[@class='signUp']/TD[2]/A[contains(text(),'Sign Up Now')]";
	private static final String signUpNowBasicLinkLocator = "//TR[@class='signUp']/TD[3]/A[contains(text(),'Sign Up Now')]";
	private static final String signUpNowPlusLinkLocator = "//TR[@class='signUp']/TD[4]/A[contains(text(),'Sign Up Now')]";
	private static final String signUpNowEnterpriseLinkLocator = "//TR[@class='signUp']/TD[5]/A[contains(text(),'Sign Up Now')]";
	private static final String signUpNowUnlimitedLinkLocator = "//TR[@class='signUp']/TD[6]/A[contains(text(),'Sign Up Now')]";
	private static final String firstNameFieldLocator = "css=#mainContent_signUp_firstName";
	private static final String lastNameFieldLocator = "css=#mainContent_signUp_lastName";
	private static final String emailFieldLocator = "css=#mainContent_signUp_email";
	private static final String userNameFieldLocator = "css=#mainContent_signUp_username";
	private static final String passwordFieldLocator = "css=#mainContent_signUp_password";
	private static final String verifyPasswordFieldLocator = "css=#mainContent_signUp_passwordConfirm";
	private static final String countryTimeZoneSelectLocator = "css=#mainContent_signUp_countryId";
	private static final String timeZoneSelectLocator = "css=#tzlist";
	private static final String companyNameFieldLocator = "css=#mainContent_signUp_companyName";
	private static final String addressFieldLocator = "css=#mainContent_address_addressLine1";
	private static final String cityFieldLocator = "css=#mainContent_address_city";
	private static final String stateFieldLocator = "css=#mainContent_address_state";
	private static final String countrySelectLocator = "css=#mainContent_address_country";
	private static final String zipCodeFieldLocator = "css=#mainContent_address_country";
	private static final String phoneNumberFieldLocator = "css=#mainContent_signUp_phoneNumber";
	private static final String siteAddressFieldLocator = "css=#mainContent_signUp_tenantName";
	private static final String promoCodeFieldLocator = "css=#promoCode";
	private static final String numberOfUsersFieldLocator = "css=#mainContent_signUp_numberOfUsers";
	private static final String phoneSupportCheckboxLocator = "css=#mainContent_signUp_purchasingPhoneSupport";
	private static final String paymentOptionsMonthlyRadioButtonLocator = "css=#mainContent_signUp_paymentOptionMONTH_TO_MONTH";
	private static final String paymentOptions20PercentRadioButtonLocator = "css=#mainContent_signUp_paymentOptionONE_YEAR_UP_FRONT";
	private static final String paymentOptions30PercentRadioButtonLocator = "css=#mainContent_signUp_paymentOptionTWO_YEARS_UP_FRONT";
	private static final String updatePriceButtonLocator = "css=input.updatePrice";
	private static final String payByCreditCardLinkLocator = "css=#payCreditCard";
	private static final String cardTypeSelectLocator = "css=#mainContent_creditCard_cCType";
	private static final String nameOnCardFieldLocator = "css=#mainContent_creditCard_name";
	private static final String cardNumberFieldLocator = "css=#mainContent_creditCard_number";
	private static final String expiryMonthSelectLocator = "css=#mainContent_creditCard_expiryMonth";
	private static final String expiryYearSelectLocator = "css=#mainContent_creditCard_expiryYear";
	private static final String payByPurchaseOrderLinkLocator = "css=#payPurchaseOrder";
	private static final String purchaseOrderNumberFieldLocator = "css=#mainContent_signUp_purchaseOrderNumber";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public SignUpAddPage(DefaultSelenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().equals(stringForPageTitle));
	}

	/**
	 * Clicks on the link to sign up for a Free tenant account. Link is
	 * on the SignUpPackagesPage. So it is assumed that you clicked on a
	 * Plan and Pricing link to get to the Sign Up Packages page before using
	 * this method.
	 */
	public void gotoSignUpNowFree() {
		assertTrue("Could not find the link to Sign Up Now (Free)", selenium.isElementPresent(signUpNowFreeLinkLocator));
		selenium.click(signUpNowFreeLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertCommonPageContent();
	}

	/**
	 * Clicks on the link to sign up for a Basic tenant account. Link is
	 * on the SignUpPackagesPage. So it is assumed that you clicked on a
	 * Plan and Pricing link to get to the Sign Up Packages page before using
	 * this method.
	 */
	public void gotoSignUpNowBasic() {
		assertTrue("Could not find the link to Sign Up Now (Basic)", selenium.isElementPresent(signUpNowBasicLinkLocator));
		selenium.click(signUpNowBasicLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertCommonPageContent();
	}

	/**
	 * Clicks on the link to sign up for a Plus tenant account. Link is
	 * on the SignUpPackagesPage. So it is assumed that you clicked on a
	 * Plan and Pricing link to get to the Sign Up Packages page before using
	 * this method.
	 */
	public void gotoSignUpNowPlus() {
		assertTrue("Could not find the link to Sign Up Now (Plus)", selenium.isElementPresent(signUpNowPlusLinkLocator));
		selenium.click(signUpNowPlusLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertCommonPageContent();
	}

	/**
	 * Clicks on the link to sign up for a Enterprise tenant account. Link is
	 * on the SignUpPackagesPage. So it is assumed that you clicked on a
	 * Plan and Pricing link to get to the Sign Up Packages page before using
	 * this method.
	 */
	public void gotoSignUpNowEnterprise() {
		assertTrue("Could not find the link to Sign Up Now (Enterprise)", selenium.isElementPresent(signUpNowEnterpriseLinkLocator));
		selenium.click(signUpNowEnterpriseLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertCommonPageContent();
	}

	/**
	 * Clicks on the link to sign up for a Unlimited tenant account. Link is
	 * on the SignUpPackagesPage. So it is assumed that you clicked on a
	 * Plan and Pricing link to get to the Sign Up Packages page before using
	 * this method.
	 */
	public void gotoSignUpNowUnlimited() {
		assertTrue("Could not find the link to Sign Up Now (Unlimited)", selenium.isElementPresent(signUpNowUnlimitedLinkLocator));
		selenium.click(signUpNowUnlimitedLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertCommonPageContent();
	}

	/**
	 * The Free package sign up is a subset of all the other packages. This
	 * method will validate the Free page content but not the additional fields
	 * not found on the Free sign up but found on all the other pages. See the
	 * assertNotFreePageContent() for validating the non-Free sign up pages.
	 */
	private void assertCommonPageContent() {
		// About You
		selenium.isElementPresent(firstNameFieldLocator);
		selenium.isElementPresent(lastNameFieldLocator);
		selenium.isElementPresent(emailFieldLocator);
		selenium.isElementPresent(userNameFieldLocator);
		selenium.isElementPresent(passwordFieldLocator);
		selenium.isElementPresent(verifyPasswordFieldLocator);
		selenium.isElementPresent(countryTimeZoneSelectLocator);
		selenium.isElementPresent(timeZoneSelectLocator);
		
		// About Your Company
		selenium.isElementPresent(companyNameFieldLocator);
		selenium.isElementPresent(addressFieldLocator);
		selenium.isElementPresent(cityFieldLocator);
		selenium.isElementPresent(stateFieldLocator);
		selenium.isElementPresent(countrySelectLocator);
		selenium.isElementPresent(zipCodeFieldLocator);
		selenium.isElementPresent(phoneNumberFieldLocator);
		
		// Site Address
		selenium.isElementPresent(siteAddressFieldLocator);

		// Promo Code
		selenium.isElementPresent(promoCodeFieldLocator);
	}
	
	/**
	 * For any non-Free account packages, these additional fields need to be
	 * present.
	 */
	public void assertNotFreePageContent() {
		// Users and Options
		selenium.isElementPresent(numberOfUsersFieldLocator);
		selenium.isElementPresent(phoneSupportCheckboxLocator);

		// Payment options
		selenium.isElementPresent(paymentOptionsMonthlyRadioButtonLocator);
		selenium.isElementPresent(paymentOptions20PercentRadioButtonLocator);
		selenium.isElementPresent(paymentOptions30PercentRadioButtonLocator);
		selenium.isElementPresent(updatePriceButtonLocator);
		
		// Pay by Credit Card
		selenium.isElementPresent(payByCreditCardLinkLocator);
		selenium.isElementPresent(cardTypeSelectLocator);
		selenium.isElementPresent(nameOnCardFieldLocator);
		selenium.isElementPresent(cardNumberFieldLocator);
		selenium.isElementPresent(expiryMonthSelectLocator);
		selenium.isElementPresent(expiryYearSelectLocator);

		// Pay by Purchase Order
		selenium.isElementPresent(payByPurchaseOrderLinkLocator);
		selenium.isElementPresent(purchaseOrderNumberFieldLocator);
	}
	
	// setCreateMyAccountCommon()
	// setCreateMyAccountNonFree()
	// gotoCreateMyAccount()
}
