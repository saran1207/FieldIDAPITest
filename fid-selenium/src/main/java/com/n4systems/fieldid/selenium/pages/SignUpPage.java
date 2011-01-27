package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.thoughtworks.selenium.Selenium;

public class SignUpPage extends WebPage {
	
	private String headerText = "You Have Selected the";
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


	public SignUpPage(Selenium selenium) {
		super(selenium);
	}
	
	public void enterCreateAccountForm(TenantInfo t) {
		if(t.getFirstName() != null)			selenium.type(firstNameTextFieldLocator, t.getFirstName());
		if(t.getLastName() != null)				selenium.type(lastNameTextFieldLocator, t.getLastName());
		if(t.getEmail() != null)				selenium.type(emailTextFieldLocator, t.getEmail());
		if(t.getCountry() != null) {
			selenium.select(countrySelectListLocator, t.getCountry());
			waitForAjax();
		}
		if(t.getTimeZone() != null)				selenium.select(timeZoneSelectListLocator, t.getTimeZone());
		if(t.getUserName() != null)				selenium.type(userNameTextFieldLocator, t.getUserName());
		
		if(t.getCompanyName() != null)			selenium.type(companyNameTextFieldLocator, t.getCompanyName());
		if(t.getCompanyAddress() != null)		selenium.type(companyAddressTextFieldLocator, t.getCompanyAddress());
		if(t.getCompanyCity() != null)			selenium.type(companyCityTextFieldLocator, t.getCompanyCity());
		if(t.getCompanyState() != null)			selenium.type(companyStateTextFieldLocator, t.getCompanyState());
		if(t.getCompanyCountry() != null)		selenium.select(companyCountrySelectListLocator, t.getCompanyCountry());
		if(t.getCompanyZipCode() != null)		selenium.type(companyZipCodeTextFieldLocator, t.getCompanyZipCode());
		if(t.getCompanyPhoneNumber() != null)	selenium.type(companyPhoneNumberTextFieldLocator, t.getCompanyPhoneNumber());
		if(t.getSiteAddress() != null) {
			selenium.type(siteAddressTextFieldLocator, t.getSiteAddress());
		}
		if(t.getPromoCode() != null)			selenium.type(promoCodeTextFieldLocator, t.getPromoCode());
		
		// For Free accounts these should all be null and not get set
		
		if(t.getNumberOfUsers() != TenantInfo.numUsersFreeAccountFlag) {
			selenium.type(numberOfUsersTextFieldLocator, Integer.toString(t.getNumberOfUsers()));
		}
		
		String paymentOption = t.getPaymentOptions();
		if(paymentOption != null) {
			if(paymentOption.equals(TenantInfo.paymentOptionsOneYear)) {
				if(selenium.isElementPresent(oneYearPaymentOptionLocator)) {
					selenium.check(oneYearPaymentOptionLocator);
				} else {
					fail("Could not find the radio button for One Year Payment Option");
				}
			} else if(paymentOption.equals(TenantInfo.paymentOptionsTwoYear)) {
				if(selenium.isElementPresent(twoYearPaymentoptionLocator)) {
					selenium.check(twoYearPaymentoptionLocator);
				}
			} else {
				fail("The payment option for this account is set to an unknown value: " + paymentOption);
			}
		}
		
		String paymentType = t.getPaymentType();
		if(paymentType.equals(TenantInfo.PAY_BY_CREDIT_CARD)) {
			selectPayByCreditCard();
			String cardType = t.getCreditCardType();
			if(cardType != null) {
				if(isOptionPresent(creditCardTypeSelectListLocator, cardType)) {
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
				if(isOptionPresent(creditCardExpiryMonthSelectListLocator, expiryMonth)) {
					selenium.select(creditCardExpiryMonthSelectListLocator, expiryMonth);
				} else {
					fail("Could not find the month '" + expiryMonth + "' on the Expiry Date");
				}
			} else {
				fail("Could not find the month for the credit card expiry date");
			}
			String expiryYear = t.getExpiryYear();
			if(expiryYear != null) {
				if(isOptionPresent(creditCardExpiryYearSelectListLocator, expiryYear)) {
					selenium.select(creditCardExpiryYearSelectListLocator, expiryYear);
				} else {
					fail("Could not find the year '" + expiryYear + "' on the Expiry Date");
				}
			} else {
				fail("Could not find the year for the credit card expiry date");
			}
		} else if(paymentType.equals(TenantInfo.payByPurchaseOrder)){
			selectPayByPurchaseOrder();
			if(t.getPurchaseOrderNumber() != null)	selenium.type(purchaseOrderNumberTextFieldLocator, t.getPurchaseOrderNumber());
		} // else assume a free account and do nothing
	}
	
	public void selectPayByCreditCard() {
		//TODO: uhhhh..????
		fail("Do not use credit card until Shaun clears up problems with billing company");
		if(selenium.isElementPresent(payByCreditCardLinkLocator)) {
			selenium.click(payByCreditCardLinkLocator);
		} else {
			fail("Could not find the link to pay by credit card");
		}
	}
	
	public void selectPayByPurchaseOrder() {
		selenium.click(payByPurchaseOrderLinkLocator);
	}

	public SignUpCompletePage submitCreateYourAccountForm() {
		selenium.click(createMyAccountButtonLocator);
		return new SignUpCompletePage(selenium);
	}

}
