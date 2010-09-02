package com.n4systems.fieldid.selenium.login.page;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.LoginPage;

public class SignUpComplete {
	FieldIdSelenium selenium;
	MiscDriver misc;
	
	// Locators
	private String headerText = "Account Created";
	private String signUpCompleteHeaderLocator = "xpath=//DIV[@id='mainContent']/H1[contains(text(),'" + headerText + ")]";
	private String signInNowLinkLocator = "xpath=//DIV[@id='signIndButton']/A";
	private String adminFirstNameTextLocator = "xpath=//LABEL[contains(text(),'First Name')]/../SPAN";
	private String adminLastNameTextLocator = "xpath=//LABEL[contains(text(),'Last Name')]/../SPAN";
	private String adminEmailTextLocator = "xpath=//LABEL[contains(text(),'Email')]/../SPAN";
	private String adminTimeZoneTextLocator = "xpath=//LABEL[contains(text(),'Time Zone')]/../SPAN";
	private String adminUserNameTextLocator = "xpath=//LABEL[contains(text(),'User Name')]/../SPAN";
	private String adminCompanyNameTextLocator = "xpath=//LABEL[contains(text(),'Company Name')]/../SPAN";
	private String adminCompanyAddressTextLocator = "xpath=//LABEL[contains(text(),'Address')]/../SPAN";
	private String adminCompanyCityTextLocator = "xpath=//LABEL[contains(text(),'City')]/../SPAN";
	private String adminCompanyStateTextLocator = "xpath=//LABEL[contains(text(),'State/Province')]/../SPAN";
	private String adminCompanyCountryTextLocator = "xpath=//LABEL[contains(text(),'Country')]/../SPAN";
	private String adminZipCodeTextLocator = "xpath=//LABEL[contains(text(),'Zip Code')]/../SPAN";
	private String adminPhoneNumberTextLocator = "xpath=//LABEL[contains(text(),'Phone Number')]/../SPAN";
	private String adminNumberOfUsersTextLocator = "xpath=//LABEL[contains(text(),'Number Of Users')]/../SPAN";
	private String adminPhoneSupportTextLocator = "xpath=//LABEL[contains(text(),'Phone Support')]/../SPAN";
	private String adminPaymentOptionTextLocator = "xpath=//LABEL[contains(text(),'Selected payment option')]/../SPAN";
	private String adminAmountPayableTextLocator = "xpath=//SPAN[@id='totalPrice']";

	public SignUpComplete(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifySignUpPackagePage() {
		assertTrue("Could not find '" + headerText + "'", selenium.isElementPresent(signUpCompleteHeaderLocator));
	}

	public LoginPage gotoSignInNow() {
		selenium.click(signInNowLinkLocator);
		return new LoginPage(selenium);
	}

	public String getAdminFirstName() {
		String s = null;
		if(selenium.isElementPresent(adminFirstNameTextLocator)) {
			s = selenium.getText(adminFirstNameTextLocator);
		} else {
			fail("Could not find the First Name");
		}
		return s;
	}

	public String getAdminLastName() {
		String s = null;
		if(selenium.isElementPresent(adminLastNameTextLocator)) {
			s = selenium.getText(adminLastNameTextLocator);
		} else {
			fail("Could not find the Last Name");
		}
		return s;
	}

	public String getAdminEmail() {
		String s = null;
		if(selenium.isElementPresent(adminEmailTextLocator)) {
			s = selenium.getText(adminEmailTextLocator);
		} else {
			fail("Could not find the Email");
		}
		return s;
	}

	public String getAdminTimeZone() {
		String s = null;
		if(selenium.isElementPresent(adminTimeZoneTextLocator)) {
			s = selenium.getText(adminTimeZoneTextLocator);
		} else {
			fail("Could not find the Time Zone");
		}
		return s;
	}

	public String getAdminUserName() {
		String s = null;
		if(selenium.isElementPresent(adminUserNameTextLocator)) {
			s = selenium.getText(adminUserNameTextLocator);
		} else {
			fail("Could not find the User Name");
		}
		return s;
	}

	public String getCompanyName() {
		String s = null;
		if(selenium.isElementPresent(adminCompanyNameTextLocator)) {
			s = selenium.getText(adminCompanyNameTextLocator);
		} else {
			fail("Could not find the Company Name");
		}
		return s;
	}

	public String getCompanyAddress() {
		String s = null;
		if(selenium.isElementPresent(adminCompanyAddressTextLocator)) {
			s = selenium.getText(adminCompanyAddressTextLocator);
		} else {
			fail("Could not find the Company Address");
		}
		return s;
	}

	public String getCompanyCity() {
		String s = null;
		if(selenium.isElementPresent(adminCompanyCityTextLocator)) {
			s = selenium.getText(adminCompanyCityTextLocator);
		} else {
			fail("Could not find the Company City");
		}
		return s;
	}

	public String getCompanyState() {
		String s = null;
		if(selenium.isElementPresent(adminCompanyStateTextLocator)) {
			s = selenium.getText(adminCompanyStateTextLocator);
		} else {
			fail("Could not find the Company State");
		}
		return s;
	}

	public String getCompanyCountry() {
		String s = null;
		if(selenium.isElementPresent(adminCompanyCountryTextLocator)) {
			s = selenium.getText(adminCompanyCountryTextLocator);
		} else {
			fail("Could not find the Company Country");
		}
		return s;
	}

	public String getCompanyZipCode() {
		String s = null;
		if(selenium.isElementPresent(adminZipCodeTextLocator)) {
			s = selenium.getText(adminZipCodeTextLocator);
		} else {
			fail("Could not find the Zip Code");
		}
		return s;
	}

	public String getCompanyPhoneNumber() {
		String s = null;
		if(selenium.isElementPresent(adminPhoneNumberTextLocator)) {
			s = selenium.getText(adminPhoneNumberTextLocator);
		} else {
			fail("Could not find the Phone Number");
		}
		return s;
	}

	public int getNumberOfUsers() {
		int i = 1;
		if(selenium.isElementPresent(adminNumberOfUsersTextLocator)) {
			String s = selenium.getText(adminNumberOfUsersTextLocator);
			i = Integer.parseInt(s);
		} else {
			fail("Could not find the number of users");
		}
		return i;
	}

	public boolean getPhoneSupport() {
		boolean b = false;
		if(selenium.isElementPresent(adminPhoneSupportTextLocator)) {
			String s = selenium.getText(adminPhoneSupportTextLocator);
			b = s.equalsIgnoreCase("yes") ? true : false;
		} else {
			fail("Could not find phone support");
		}
		return b;
	}

	public String getSelectedPaymentOption() {
		String s = null;
		if(selenium.isElementPresent(adminPaymentOptionTextLocator)) {
			s = selenium.getText(adminPaymentOptionTextLocator);
		} else {
			fail("Could not find the selected payment option");
		}
		return s;
	}

	public String getTotalAmountPayable() {
		String s = null;
		if(selenium.isElementPresent(adminAmountPayableTextLocator)) {
			s = selenium.getText(adminAmountPayableTextLocator);
		} else {
			fail("Could not find the Amount Payable");
		}
		return s;
	}
}
