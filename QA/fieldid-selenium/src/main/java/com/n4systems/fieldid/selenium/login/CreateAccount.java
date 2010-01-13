package com.n4systems.fieldid.selenium.login;

import java.util.List;

import org.junit.Assert;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

public class CreateAccount extends Assert {
	Selenium selenium;
	Misc misc;
	
	// Locators
	private String headerText = "Create Your Account";
	private String createAccountHeaderLocator = "xpath=//FORM[@id='mainContent']/H1[contains(text(),'" + headerText + "')]";
	private String firstNameTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_firstName']";
	private String lastNameTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_lastName']";
	private String emailTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_email']";
	private String countrySelectListLocator = "xpath=//SELECT[@id='mainContent_signUp_countryId']";
	private String timeZoneSelectListLocator = "xpath=//SELECT[@id='tzlist']";
	private String userNameTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_username']";
	private String passwordTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_password']";
	private String passwordAgainTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_passwordConfirm']";
	private String companyNameTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_companyName']";
	private String companyAddressTextFieldLocator = "xpath=//INPUT[@id='mainContent_address_addressLine1']";
	private String companyCityTextFieldLocator = "xpath=//INPUT[@id='mainContent_address_city']";
	private String companyStateTextFieldLocator = "xpath=//INPUT[@id='mainContent_address_state']";
	private String companyCountrySelectListLocator = "xpath=//SELECT[@id='mainContent_address_country']";
	private String companyZipCodeTextFieldLocator = "xpath=//INPUT[@id='mainContent_address_postal']";
	private String companyPhoneNumberTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_phoneNumber']";
	private String siteAddressTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_tenantName']";
	private String numberOfUsersTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_numberOfUsers']";
	private String phoneSupportCheckBoxLocator = "xpath=//INPUT[@id='mainContent_signUp_purchasingPhoneSupport']";
	private String promoCodeTextFieldLocator = "xpath=//INPUT[@id='promoCode']";
	private String payByCreditCardLinkLocator = "xpath=//A[@id='payCreditCard']";
	private String payByPurchaseOrderLinkLocator = "xpath=//A[@id='payPurchaseOrder']";
	private String createMyAccountButtonLocator = "xpath=//INPUT[@id='mainContent_label_create_my_account']";
	private String chooseAnotherPackageLinkLocator = "xpath=//A[contains(text(),'choose another package')]";
	private String oneYearPaymentOptionLocator = "xpath=//INPUT[@id='mainContent_signUp_paymentOptionONE_YEAR_UP_FRONT']";
	private String twoYearPaymentoptionLocator = "xpath=//INPUT[@id='mainContent_signUp_paymentOptionTWO_YEARS_UP_FRONT']";
	private String creditCardTypeSelectListLocator = "xpath=//SELECT[@id='mainContent_creditCard_cCType']";
	private String creditCardNameTextFieldLocator = "xpath=//INPUT[@id='mainContent_creditCard_name']";
	private String creditCardNumberTextFieldLocator = "xpath=//INPUT[@id='mainContent_creditCard_number']";
	private String creditCardExpiryMonthSelectListLocator = "xpath=//SELECT[@id='mainContent_creditCard_expiryMonth']";
	private String creditCardExpiryYearSelectListLocator = "xpath=//SELECT[@id='mainContent_creditCard_expiryYear']";
	private String purchaseOrderNumberTextFieldLocator = "xpath=//INPUT[@id='mainContent_signUp_purchaseOrderNumber']";

	public CreateAccount(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifyCreateAccountForm(boolean notfree) {
		assertTrue("Could not find the First Name text field", selenium.isElementPresent(firstNameTextFieldLocator));
		assertTrue("Could not find the Last Name text field", selenium.isElementPresent(lastNameTextFieldLocator));
		assertTrue("Could not find the Email text field", selenium.isElementPresent(emailTextFieldLocator));
		assertTrue("Could not find the Country select list", selenium.isElementPresent(countrySelectListLocator));
		assertTrue("Could not find the Time Zone select list", selenium.isElementPresent(timeZoneSelectListLocator));
		assertTrue("Could not find the User Name text field", selenium.isElementPresent(userNameTextFieldLocator));
		assertTrue("Could not find the Password text field", selenium.isElementPresent(passwordTextFieldLocator));
		assertTrue("Could not find the Password Again text field", selenium.isElementPresent(passwordAgainTextFieldLocator));
		assertTrue("Could not find the Company Name text field", selenium.isElementPresent(companyNameTextFieldLocator));
		assertTrue("Could not find the Company Address text field", selenium.isElementPresent(companyAddressTextFieldLocator));
		assertTrue("Could not find the Company City text field", selenium.isElementPresent(companyCityTextFieldLocator));
		assertTrue("Could not find the Company State/Province text field", selenium.isElementPresent(companyStateTextFieldLocator));
		assertTrue("Could not find the Company country select list", selenium.isElementPresent(companyCountrySelectListLocator));
		assertTrue("Could not find the Company Zip Code text field", selenium.isElementPresent(companyZipCodeTextFieldLocator));
		assertTrue("Could not find the Company Phone Number text field", selenium.isElementPresent(companyPhoneNumberTextFieldLocator));
		assertTrue("Could not find the Site Address text field", selenium.isElementPresent(siteAddressTextFieldLocator));
		if(notfree) {
			assertTrue("Could not find the Number Of Users text field", selenium.isElementPresent(numberOfUsersTextFieldLocator));
			assertTrue("Could not find the Phone Support check box", selenium.isElementPresent(phoneSupportCheckBoxLocator));
			assertTrue("Could not find the Promo Code text field", selenium.isElementPresent(promoCodeTextFieldLocator));
			assertTrue("Could not find the Pay by Credit link", selenium.isElementPresent(payByCreditCardLinkLocator));
			assertTrue("Could not find the Pay by Purchase Order link", selenium.isElementPresent(payByPurchaseOrderLinkLocator));
		}
		assertTrue("Could not find the Create My Account button", selenium.isElementPresent(createMyAccountButtonLocator));
		assertTrue("Could not find the choose another package link", selenium.isElementPresent(chooseAnotherPackageLinkLocator));
	}
	
	public void verifyCreateAccountPage(String packageName) {
		misc.info("Confirm we arrived at the Create Account page");
		assertTrue("Could not find '" + headerText + "'", selenium.isElementPresent(createAccountHeaderLocator));
		assertTrue("Could not find '" + packageName + "' in the header", selenium.isElementPresent("xpath=//H1[contains(text(),'" + packageName + "')]"));
		boolean notfree = !packageName.equals("Free");
		verifyCreateAccountForm(notfree);
	}
	
	public void setCreateYourAccountForm(CreateTenant t) {
		this.verifyCreateAccountForm(false);
		misc.info("Fill in the account information.");
		if(t.getFirstName() != null)			selenium.type(firstNameTextFieldLocator, t.getFirstName());
		if(t.getLastName() != null)				selenium.type(lastNameTextFieldLocator, t.getLastName());
		if(t.getEmail() != null)				selenium.type(emailTextFieldLocator, t.getEmail());
		if(t.getCountry() != null) {
			selenium.select(countrySelectListLocator, t.getCountry());
			misc.WaitForTimeZoneToUpdate();
		}
		if(t.getTimeZone() != null)				selenium.select(timeZoneSelectListLocator, t.getTimeZone());
		if(t.getUserName() != null)				selenium.type(userNameTextFieldLocator, t.getUserName());
		if(t.getPassword() != null)				selenium.type(passwordTextFieldLocator, t.getPassword());
		if(t.getPassword2() != null)			selenium.type(passwordAgainTextFieldLocator, t.getPassword2());
		
		if(t.getCompanyName() != null)			selenium.type(companyNameTextFieldLocator, t.getCompanyName());
		if(t.getCompanyAddress() != null)		selenium.type(companyAddressTextFieldLocator, t.getCompanyAddress());
		if(t.getCompanyCity() != null)			selenium.type(companyCityTextFieldLocator, t.getCompanyCity());
		if(t.getCompanyState() != null)			selenium.type(companyStateTextFieldLocator, t.getCompanyState());
		if(t.getCompanyCountry() != null)		selenium.select(companyCountrySelectListLocator, t.getCompanyCountry());
		if(t.getCompanyZipCode() != null)		selenium.type(companyZipCodeTextFieldLocator, t.getCompanyZipCode());
		if(t.getCompanyPhoneNumber() != null)	selenium.type(companyPhoneNumberTextFieldLocator, t.getCompanyPhoneNumber());
		if(t.getSiteAddress() != null)			selenium.type(siteAddressTextFieldLocator, t.getSiteAddress());
		if(t.getPromoCode() != null)			selenium.type(promoCodeTextFieldLocator, t.getPromoCode());
		
		// For Free accounts these should all be null and not get set
		
		if(t.getNumberOfUsers() != CreateTenant.numUsersFreeAccountFlag) {
			selenium.type(numberOfUsersTextFieldLocator, Integer.toString(t.getNumberOfUsers()));
		}
		
		// here we assume if the element is not present, we are configuring a free account.
		// use verifyCreateAccountForm() to confirm this element is there when expected.
		if(selenium.isElementPresent(phoneSupportCheckBoxLocator)) {
			if(t.getPhoneSupport()) {
				selenium.check(phoneSupportCheckBoxLocator);
			} else {
				selenium.uncheck(phoneSupportCheckBoxLocator);
			}
		}
		
		String paymentOption = t.getPaymentOptions();
		if(paymentOption != null) {
			if(paymentOption.equals(CreateTenant.paymentOptionsOneYear)) {
				if(selenium.isElementPresent(oneYearPaymentOptionLocator)) {
					selenium.check(oneYearPaymentOptionLocator);
				} else {
					fail("Could not find the radio button for One Year Payment Option");
				}
			} else if(paymentOption.equals(CreateTenant.paymentOptionsTwoYear)) {
				if(selenium.isElementPresent(twoYearPaymentoptionLocator)) {
					selenium.check(twoYearPaymentoptionLocator);
				}
			} else {
				fail("The payment option for this account is set to an unknown value: " + paymentOption);
			}
		}
		
		String paymentType = t.getPaymentType();
		if(paymentType.equals(CreateTenant.payByCreditCard)) {
			selectPayByCreditCard();
			String cardType = t.getCreditCardType();
			if(cardType != null) {
				if(misc.isOptionPresent(creditCardTypeSelectListLocator, cardType)) {
					selenium.select(creditCardTypeSelectListLocator, cardType);
				} else {
					fail("Could not find '" + cardType + "' in the Credit Card list");
				}
			}
			if(t.getNameOnCard() != null)	selenium.type(creditCardNameTextFieldLocator, t.getNameOnCard());
			if(t.getCardNumber() != null) 	selenium.type(creditCardNumberTextFieldLocator, t.getCardNumber());
			if(t.getExpiryMonth() != null) {
				
			}
			String expiryMonth = t.getExpiryMonth();
			if(expiryMonth != null) {
				if(misc.isOptionPresent(creditCardExpiryMonthSelectListLocator, expiryMonth)) {
					selenium.select(creditCardExpiryMonthSelectListLocator, expiryMonth);
				} else {
					fail("Could not find the month '" + expiryMonth + "' on the Expiry Date");
				}
			} else {
				fail("Could not find the month for the credit card expiry date");
			}
			String expiryYear = t.getExpiryYear();
			if(expiryYear != null) {
				if(misc.isOptionPresent(creditCardExpiryYearSelectListLocator, expiryYear)) {
					selenium.select(creditCardExpiryYearSelectListLocator, expiryYear);
				} else {
					fail("Could not find the year '" + expiryYear + "' on the Expiry Date");
				}
			} else {
				fail("Could not find the year for the credit card expiry date");
			}
		} else if(paymentType.equals(CreateTenant.payByPurchaseOrder)){
			selectPayByPurchaseOrder();
			if(t.getPurchaseOrderNumber() != null)	selenium.type(purchaseOrderNumberTextFieldLocator, t.getPurchaseOrderNumber());
		} // else assume a free account and do nothing
	}
	
	public void selectPayByCreditCard() {
		misc.info("Click the Pay by Credit Card link");
		if(selenium.isElementPresent(payByCreditCardLinkLocator)) {
			selenium.click(payByCreditCardLinkLocator);
		} else {
			fail("Could not find the link to pay by credit card");
		}
	}
	
	public void selectPayByPurchaseOrder() {
		misc.info("Click the Pay by Purchase Order link");
		if(selenium.isElementPresent(payByPurchaseOrderLinkLocator)) {
			selenium.click(payByPurchaseOrderLinkLocator);
		} else {
			fail("Could not find the link to pay by purchase order");
		}
	}

	public List<String> getUserCountryList() {
		String[] countries = selenium.getSelectOptions(countrySelectListLocator);
		List<String> result = misc.convertStringArrayToList(countries);
		return result;
	}

	public void setUserCountry(String s) {
		if(selenium.isElementPresent(countrySelectListLocator)) {
			if(misc.isOptionPresent(countrySelectListLocator, s)) {
				selenium.select(countrySelectListLocator, s);
			} else {
				fail("Could not find the country '" + s + "' in the select list");
			}
		} else {
			fail("Could not find the select list for Country under About You");
		}
	}

	public List<String> getUserTimeZoneList() {
		String[] timeZones = selenium.getSelectOptions(timeZoneSelectListLocator);
		List<String> result = misc.convertStringArrayToList(timeZones);
		return result;
	}

	public List<String> getCompanyCountryList() {
		String[] countries = selenium.getSelectOptions(companyCountrySelectListLocator);
		List<String> result = misc.convertStringArrayToList(countries);
		return result;
	}

	public void gotoCreateMyAccount() {
		if(selenium.isElementPresent(createMyAccountButtonLocator)) {
			selenium.click(createMyAccountButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Create My Account button");
		}
	}
	
	public void gotoChooseAnotherPackage() {
		if(selenium.isElementPresent(chooseAnotherPackageLinkLocator)) {
			selenium.click(chooseAnotherPackageLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to choose another package");
		}
	}
}
