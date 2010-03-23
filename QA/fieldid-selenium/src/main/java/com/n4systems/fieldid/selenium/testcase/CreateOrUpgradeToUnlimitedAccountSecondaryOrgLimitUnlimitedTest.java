package com.n4systems.fieldid.selenium.testcase;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageOrganizations;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.login.page.SignUpComplete;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;

/**
 * WEB-1498
 * 
 *
 */
public class CreateOrUpgradeToUnlimitedAccountSecondaryOrgLimitUnlimitedTest extends FieldIDTestCase {

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

		
		createANewUnlimitedTenant(username, password);
		returnToLogin();
		logIntoNewTenant(username, password);
		String orgName = addASecondaryOrganization();
		verifySecondaryOrganizationWasAddedOkay(orgName);
	
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
}
