package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageOrganizations;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.datatypes.PrimaryOrganization;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.login.page.SignUpComplete;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

/**
 * WEB-1469
 * 
 * @author dgrainge
 *
 */
public class PopulateOrgUnitAddressFromSignUpInformationTest extends FieldIDTestCase {

	Login login;
	SignUpPackages sup;
	CreateAccount create;
	SignUpComplete complete;
	Admin admin;
	ManageOrganizations mos;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		sup = new SignUpPackages(selenium, misc);
		create = new CreateAccount(selenium, misc);
		complete = new SignUpComplete(selenium, misc);
		admin = new Admin(selenium, misc);
		mos = new ManageOrganizations(selenium, misc);
	}
	
	@Test
	public void shouldBePopulatingPrimaryOrganizationAddressWithInformationGivenAtAccountSetUp() throws Exception {
		String username = "darrell";
		String password = "makemore$";

		try {
			TenantInfo t = createANewTenant(username, password);
			returnToLogin();
			logIntoNewTenant(username, password);
			PrimaryOrganization p = getPrimaryOrganization();
			verifyEquals(t, p);
		} catch(Exception e) {
			throw e;
		}
	}

	/**
	 * Checks the tenant information and compares it to the primary organization
	 * information.
	 * 
	 * @param t
	 * @param prime
	 */
	private void verifyEquals(TenantInfo expected, Organization actual) {
		assertEquals(expected.getCompanyAddress(), actual.getCompanyStreetAddress());
		assertEquals(expected.getCompanyCity(), actual.getCompanyCity());
		assertEquals(expected.getCompanyState(), actual.getCompanyState());
		assertEquals(expected.getCompanyCountry(), actual.getCompanyCountry());
		assertEquals(expected.getCompanyZipCode(), actual.getCompanyZipCode());
		assertEquals(expected.getCompanyPhoneNumber(), actual.getCompanyPhoneNumber());
	}

	private PrimaryOrganization getPrimaryOrganization() {
		misc.gotoAdministration();
		admin.verifyAdministrationPage();
		admin.gotoManageOrganizations();
		
		mos.verifyManageOrganizationsPage();
		mos.gotoEditPrimaryOrganization();
		PrimaryOrganization prime = mos.getPrimaryOrganization();
		return prime;
	}

	private void logIntoNewTenant(String username, String password) {
		login.setUserName(username);
		login.setPassword(password);
		login.submitSignIn();
		
		login.verifySignedInWithEULA();
		misc.scrollToBottomOfEULA();
		misc.gotoAcceptEULA();
		login.verifySignedInHomePage();
	}

	private void returnToLogin() {
		complete.gotoSignInNow();
	}

	private TenantInfo createANewTenant(String username, String password) {
		String tenantName = MiscDriver.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		if(!login.isPlansAndPricingAvailable()) {
			startAsCompany("msa");
		}
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
