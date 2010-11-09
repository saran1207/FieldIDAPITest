package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.EULAPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SignUpCompletePage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;

public class CreateNewAccountAndCreateSecondaryOrgTest extends FieldIDTestCase {
	
	LoginPage loginPage;
	CreateAccount create;
	
	@Before
	public void setUp() throws Exception {
		loginPage = start();
		create = new CreateAccount(selenium, misc);
	}
	
	@Test
	public void create_unlimited_account_type_tenant_and_it_should_be_able_to_create_secondary_org() throws Exception {
		String username = "darrell";
		String password = "makemore$";

		SignUpCompletePage completePage = createANewUnlimitedTenant(username, password);
		completePage.clickSignInNow();
		HomePage homePage = logIntoNewTenant(username, password);
		ManageOrganizationsPage orgsPage = homePage.clickSetupLink().clickManageOrganizations();
		String orgName = addASecondaryOrganization(orgsPage);
		verifySecondaryOrganizationWasAdded(orgsPage, orgName);
	}

	private void verifySecondaryOrganizationWasAdded(ManageOrganizationsPage orgsPage, String orgName) {
		List<String> orgs = orgsPage.getOrganizationNames();
		if(!orgs.contains(orgName)) {
			fail("Did not find newly created Secondary Organization '" + orgName + "' in list: " + orgs);
		}
	}

	private String addASecondaryOrganization(ManageOrganizationsPage orgsPage) {
		orgsPage.clickAdd();
		
		String name = MiscDriver.getRandomString(15);
		Organization o = new Organization(name);
		orgsPage.fillOrganizationForm(o);
		orgsPage.clickSave();
		return o.getName();
	}

	private HomePage logIntoNewTenant(String username, String password) {
		EULAPage eulaPage = loginPage.loginToEula(username, password);
		eulaPage.scrollToBottomOfEULA();
		return eulaPage.clickAcceptEULAToWizard().clickNoThanks();
	}

	private SignUpCompletePage createANewUnlimitedTenant(String username, String password) {
		String tenantName = MiscDriver.getRandomString(8);
		String tenantID = tenantName.toLowerCase();

		if(!loginPage.isPlansAndPricingAvailable()) {
			loginPage = startAsCompany("msa");
		}
		SelectPackagePage selectPackagePage = loginPage.clickPlansAndPricingLink();
		String packageName = "Unlimited";	// must be unlimited
		SignUpPage signUpPage = selectPackagePage.clickSignUpNowLink(packageName);

		TenantInfo t = new TenantInfo(username, password, tenantName, tenantID);
		t.setNumberOfUsers(2);
		t.setPhoneSupport(true);
		t.setPromoCode("promo");
		t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
		t.setPaymentType(TenantInfo.payByPurchaseOrder);
		t.setPurchaseOrderNumber("88888");

		signUpPage.enterCreateAccountForm(t);
		return signUpPage.submitCreateYourAccountForm();
	}
}
