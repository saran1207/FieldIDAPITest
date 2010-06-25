package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.console.page.ConsoleLogin;
import com.n4systems.fieldid.selenium.console.page.ConsoleOrganizations;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.login.page.SignUpComplete;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

/**
 * WEB-1378
 * 
 * @author dgrainge
 *
 */
public class TurnOnPlansAndPricingWithPartnerCenterTest extends FieldIDTestCase {

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
		String tenantName = MiscDriver.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		setCompany("msa");
		login.gotoPlansAndPricing();
		String packageName = "Unlimited";
		sup.gotoSignUpNow(packageName);
		create.verifyCreateAccountPage(packageName);

		CreateTenant t = new CreateTenant(username, password, tenantName, tenantID);
		
		t.setNumberOfUsers(2);
		t.setPhoneSupport(true);
		t.setPromoCode("promo");
		t.setPaymentOptions(CreateTenant.paymentOptionsTwoYear);
		t.setPaymentType(CreateTenant.payByPurchaseOrder);
		t.setpurchaseOrderNumber("88888");

		create.setCreateYourAccountForm(t);
		create.submitCreateYourAccountForm();
		
		return t;
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
