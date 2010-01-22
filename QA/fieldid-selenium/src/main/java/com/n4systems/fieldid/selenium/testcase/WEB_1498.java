package com.n4systems.fieldid.selenium.testcase;

import java.util.List;

import org.junit.*;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.Admin;
import com.n4systems.fieldid.selenium.administration.ManageOrganizations;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.login.CreateAccount;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.login.SignUpComplete;
import com.n4systems.fieldid.selenium.login.SignUpPackages;

public class WEB_1498 extends FieldIDTestCase {

	Login login;
	SignUpPackages sup;
	CreateAccount create;
	SignUpComplete complete;
	Admin admin;
	ManageOrganizations mos;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		sup = new SignUpPackages(selenium, misc);
		create = new CreateAccount(selenium, misc);
		complete = new SignUpComplete(selenium, misc);
		admin = new Admin(selenium, misc);
		mos = new ManageOrganizations(selenium, misc);
	}
	
	@Test
	public void newlyCreatedUnlimitedTenantShouldBeAbleToCreateSecondaryOrganizations() throws Exception {
		String username = "darrell";
		String password = "makemore$";

		try {
			@SuppressWarnings("unused")
			CreateTenant t = createANewUnlimitedTenant(username, password);
			returnToLogin();
			logIntoNewTenant(username, password);
			String orgName = addASecondaryOrganization();
			verifySecondaryOrganizationWasAddedOkay(orgName);
		} catch(Exception e) {
			throw e;
		}
	}

	private void verifySecondaryOrganizationWasAddedOkay(String orgName) {
		List<String> orgs = mos.getSecondaryOrganizationNames();
		if(!orgs.contains(orgName)) {
			fail("Did not find newly created Secondary Organization '" + orgName + "'");
		}
	}

	private String addASecondaryOrganization() {
		misc.gotoAdministration();
		admin.verifyAdministrationPage();
		admin.gotoManageOrganizations();
		
		mos.verifyManageOrganizationsPage();
		mos.gotoAddSecondaryOrganization();
		mos.waitForTenantSettingsToBeCreated();
		String name = misc.getRandomString(15);
		Organization o = new Organization(name);
		mos.setSecondaryOrganization(o);
		mos.gotoSaveAddSecondaryOrganization();
		return o.getName();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void logIntoNewTenant(String username, String password) {
		login.setUserName(username);
		login.setPassword(password);
		login.gotoSignIn();
		
		login.verifySignedInWithEULA();
		misc.scrollToBottomOfEULA();
		misc.gotoAcceptEULA();
		login.verifySignedInHomePage();
	}

	private void returnToLogin() {
		complete.gotoSignInNow();
	}

	private CreateTenant createANewUnlimitedTenant(String username, String password) {
		String tenantName = misc.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		if(!login.isPlansAndPricingAvailable()) {
			setCompany("msa");
		}
		login.gotoPlansAndPricing();
		String packageName = "Unlimited";	// must be unlimited
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
		t.setPaymentType(CreateTenant.payByCreditCard);
		t.setCreditCardType(CreateTenant.creditCardTypeVISA);
		t.setNameOnCard("John Q. Public");
		t.setCardNumber("1111111111111111");
		t.setexpiryMonth("12");
		t.setexpiryYear("2011");
		create.setCreateYourAccountForm(t);
		create.gotoCreateMyAccount();
		
		return t;
	}
}
