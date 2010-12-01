package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.console.page.ConsoleLogin;
import com.n4systems.fieldid.selenium.console.page.ConsoleOrganizations;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
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
public class TurnOnPlansAndPricingWithReadOnlyUserTest extends FieldIDTestCase {

	Login login;
	ConsoleLogin console;
	ConsoleOrganizations consoleOrgs;
	SignUpPackages sup;
	CreateAccount create;
	SignUpComplete complete;
	TenantInfo t;
	String username = "darrell";
	String password = "makemore$";
	
	@Before
	public void setUp() throws Exception {
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
	public void readOnlyUserOnEnablePlansAndPricingOffShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = true;
		boolean showPlansAndPricing = false;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		startAsCompany(t.getCompanyName());
		verifyRequestAnAccountIsPresent();
	}
	
	@Test
	public void readOnlyUserOnEnablePlansAndPricingOnShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = true;
		boolean showPlansAndPricing = true;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		startAsCompany(t.getCompanyName());
		verifyPlansAndPricingIsPresent();
	}
	
	@Test
	public void readOnlyUserOffEnablePlansAndPricingOnShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = false;
		boolean showPlansAndPricing = true;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		startAsCompany(t.getCompanyName());
		verifyPlansAndPricingIsPresent();
	}
	
	@Test
	public void readOnlyUserOffEnablePlansAndPricingOffShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = false;
		boolean showPlansAndPricing = false;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		startAsCompany(t.getCompanyName());
		verifyPlansAndPricingIsPresent();
	}
	
	private void setTenantFeatures(boolean readOnlyUser, boolean showPlansAndPricing) {
		console.gotoAdminConsoleAndLogin();
		consoleOrgs.gotoEditTenant(t.getSiteAddress());
		consoleOrgs.setReadOnlyUser(readOnlyUser);
		consoleOrgs.setShowPlansAndPricing(showPlansAndPricing);
		consoleOrgs.gotoSubmitTenantInformation();
	}

	private void verifyPlansAndPricingIsPresent() {
		assertTrue(login.isPlansAndPricingAvailable());
	}

	private void verifyRequestAnAccountIsPresent() {
		assertTrue(login.isRequestAnAccountAvailable());
	}

	private TenantInfo createANewTenant(String username, String password) {
		String tenantName = MiscDriver.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		startAsCompany("msa");
		login.gotoPlansAndPricing();
		String packageName = "Unlimited";
		sup.gotoSignUpNow(packageName);
		create.verifyCreateAccountPage(packageName);

		TenantInfo t = new TenantInfo(username, password, tenantName, tenantID);
		
		t.setNumberOfUsers(2);
		t.setPhoneSupport(true);
		t.setPromoCode("promo");
		t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
		t.setPaymentType(TenantInfo.payByPurchaseOrder);
		t.setPurchaseOrderNumber("88888");

		create.setCreateYourAccountForm(t);
		create.submitCreateYourAccountForm();
		
		return t;
	}

}
