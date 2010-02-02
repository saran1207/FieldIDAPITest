package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.Admin;
import com.n4systems.fieldid.selenium.administration.ManageOrganizations;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.datatypes.PrimaryOrganization;
import com.n4systems.fieldid.selenium.login.CreateAccount;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.login.SignUpComplete;
import com.n4systems.fieldid.selenium.login.SignUpPackages;

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
		super.setUp();
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
			CreateTenant t = createANewTenant(username, password);
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
	private void verifyEquals(CreateTenant expected, Organization actual) {
		verifyEquals(expected.getCompanyAddress(), actual.getCompanyStreetAddress());
		verifyEquals(expected.getCompanyCity(), actual.getCompanyCity());
		verifyEquals(expected.getCompanyState(), actual.getCompanyState());
		verifyEquals(expected.getCompanyCountry(), actual.getCompanyCountry());
		verifyEquals(expected.getCompanyZipCode(), actual.getCompanyZipCode());
		verifyEquals(expected.getCompanyPhoneNumber(), actual.getCompanyPhoneNumber());
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
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
		login.gotoSignIn();
		
		login.verifySignedInWithEULA();
		misc.scrollToBottomOfEULA();
		misc.gotoAcceptEULA();
		login.verifySignedInHomePage();
	}

	private void returnToLogin() {
		complete.gotoSignInNow();
	}

	private CreateTenant createANewTenant(String username, String password) {
		String tenantName = misc.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		if(!login.isPlansAndPricingAvailable()) {
			setCompany("msa");
		}
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
		create.gotoCreateMyAccount();
		
		return t;
	}
}
