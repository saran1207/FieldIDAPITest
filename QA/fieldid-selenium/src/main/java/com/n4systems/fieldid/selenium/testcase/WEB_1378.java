package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.console.ConsoleLogin;
import com.n4systems.fieldid.selenium.console.ConsoleOrganizations;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.login.CreateAccount;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.login.SignUpComplete;
import com.n4systems.fieldid.selenium.login.SignUpPackages;

public class WEB_1378 extends FieldIDTestCase {

	Login login;
	ConsoleLogin console;
	ConsoleOrganizations consoleOrgs;
	SignUpPackages sup;
	CreateAccount create;
	SignUpComplete complete;
	CreateTenant t;
	String username = "darrell";
	String password = "makemore$";
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		console = new ConsoleLogin(selenium, misc);
		consoleOrgs = new ConsoleOrganizations(selenium, misc);
		sup = new SignUpPackages(selenium, misc);
		create = new CreateAccount(selenium, misc);
		complete = new SignUpComplete(selenium, misc);
		if(t == null) {
			t = createANewTenant(username, password);
		}
	}

	@Test
	public void partnerCenterOnEnablePlansAndPricingOffShowsRequestAnAccount() throws Exception {
		try {
			boolean partnerCenter = true;
			boolean showPlansAndPricing = false;
			setTenantFeatures(partnerCenter, showPlansAndPricing);
			setCompany(t.getCompanyName());
			verifyRequestAnAccountIsPresent();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void partnerCenterOnEnablePlansAndPricingOnShowsRequestAnAccount() throws Exception {
		try {
			boolean partnerCenter = true;
			boolean showPlansAndPricing = true;
			setTenantFeatures(partnerCenter, showPlansAndPricing);
			setCompany(t.getCompanyName());
			verifyPlansAndPricingIsPresent();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void partnerCenterOffEnablePlansAndPricingOnShowsRequestAnAccount() throws Exception {
		try {
			boolean partnerCenter = false;
			boolean showPlansAndPricing = true;
			setTenantFeatures(partnerCenter, showPlansAndPricing);
			setCompany(t.getCompanyName());
			verifyPlansAndPricingIsPresent();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void partnerCenterOffEnablePlansAndPricingOffShowsRequestAnAccount() throws Exception {
		try {
			boolean partnerCenter = false;
			boolean showPlansAndPricing = false;
			setTenantFeatures(partnerCenter, showPlansAndPricing);
			setCompany(t.getCompanyName());
			verifyPlansAndPricingIsPresent();
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void setTenantFeatures(boolean partnerCenter, boolean showPlansAndPricing) {
		console.gotoAdminConsoleAndLogin();
		consoleOrgs.gotoEditTenant(t.getSiteAddress());
		consoleOrgs.setPartnerCenter(partnerCenter);
		consoleOrgs.setShowPlansAndPricing(showPlansAndPricing);
		consoleOrgs.gotoSubmitTenantInformation();
	}

	private void verifyPlansAndPricingIsPresent() {
		assertTrue(login.isPlansAndPricingAvailable());
	}

	private void verifyRequestAnAccountIsPresent() {
		assertTrue(login.isRequestAnAccountAvailable());
	}

	private CreateTenant createANewTenant(String username, String password) {
		String tenantName = misc.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		setCompany("msa");
		login.gotoPlansAndPricing();
		String packageName = "Unlimited";
		sup.gotoSignUpNow(packageName);
		create.verifyCreateAccountPage(packageName);

		CreateTenant t = new CreateTenant();
		t.setFirstName("Darrell");
		t.setLastName("Grainger");
		t.setEmail("darrell.grainger@fieldid.com");
		t.setCountry("Canada");
		t.setTimeZone("Ontario - Toronto");
		t.setUserName(username);
		t.setPassword(password);
		t.setPassword2(password);
		t.setCompanyName(tenantName);
		t.setCompanyAddress("179 John Street");
		t.setCompanyCity("Toronto");
		t.setCompanyState("Ontario");
		t.setCompanyCountry("Canada");
		t.setCompanyZipCode("M5T 1X4");
		t.setCompanyPhoneNumber("(416) 599-6464");
		t.setSiteAddress(tenantID);
		t.setNumberOfUsers(2);
		t.setPhoneSupport(true);
		t.setPromoCode("promo");
		t.setPaymentOptions(CreateTenant.paymentOptionsTwoYear);
		t.setPaymentType(CreateTenant.payByPurchaseOrder);
		t.setpurchaseOrderNumber("88888");
//		t.setPaymentType(CreateTenant.payByCreditCard);
//		t.setCreditCardType(CreateTenant.creditCardTypeVISA);
//		t.setNameOnCard("John Q. Public");
//		t.setCardNumber("1111111111111111");
//		t.setexpiryMonth("12");
//		t.setexpiryYear("2011");
		create.setCreateYourAccountForm(t);
		create.gotoCreateMyAccount();
		
		return t;
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
