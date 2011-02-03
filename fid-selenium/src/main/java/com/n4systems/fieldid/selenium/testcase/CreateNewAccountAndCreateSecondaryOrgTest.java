package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.pages.EULAPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SetPasswordPage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.util.SignUpEmailLoginNavigator;
import com.n4systems.util.ConfigEntry;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

public class CreateNewAccountAndCreateSecondaryOrgTest extends FieldIDTestCase {

    private static final String NEW_TENANT_NAME = TEST_CREATED_TENANT_NAMES[0];
    private static final String NEW_ORG_NAME = "TestOrg";
    private static final String NEW_USER = "testuser";
    private static final String NEW_PASSWORD = "testuser";
	
    @Override
    public void setupScenario(Scenario scenario) {
        scenario.updateConfigurationForTenant("test1", ConfigEntry.EXTERNAL_PLANS_AND_PRICING_ENABLED, "true");
    }

    @Test
	public void create_unlimited_account_type_tenant_and_it_should_be_able_to_create_secondary_org() throws Exception {
		SetPasswordPage completePage = createANewUnlimitedTenant(NEW_USER, NEW_PASSWORD);
        completePage.enterAndConfirmPassword(NEW_PASSWORD);
        LoginPage loginPage = completePage.submitConfirmPassword();
		HomePage homePage = logIntoNewTenant(loginPage, NEW_USER, NEW_PASSWORD);
		ManageOrganizationsPage orgsPage = homePage.clickSetupLink().clickManageOrganizations();
		addASecondaryOrganization(orgsPage);
		verifySecondaryOrganizationWasAdded(orgsPage);
	}

	private void verifySecondaryOrganizationWasAdded(ManageOrganizationsPage orgsPage) {
		List<String> orgs = orgsPage.getOrganizationNames();
		if(!orgs.contains(NEW_ORG_NAME)) {
			fail("Did not find newly created Secondary Organization '" + NEW_ORG_NAME + "' in list: " + orgs);
		}
	}

	private void addASecondaryOrganization(ManageOrganizationsPage orgsPage) {
		orgsPage.clickAdd();
		
		Organization o = new Organization(NEW_ORG_NAME);
		orgsPage.enterOrganizationName(NEW_ORG_NAME);
		orgsPage.clickSave();
	}

	private HomePage logIntoNewTenant(LoginPage loginPage, String username, String password) {
		EULAPage eulaPage = loginPage.loginToEula(username, password);
		eulaPage.scrollToBottomOfEULA();
		return eulaPage.clickAcceptEULAToWizard().clickNoThanks();
	}

	private SetPasswordPage createANewUnlimitedTenant(String username, String password) {
		SelectPackagePage selectPackagePage = startAsCompany("test1").clickPlansAndPricingLink();
		String packageName = "Unlimited";	// must be unlimited
		SignUpPage signUpPage = selectPackagePage.clickSignUpNowLink(packageName);

		TenantInfo t = new TenantInfo(username, password, NEW_TENANT_NAME, NEW_TENANT_NAME);
		t.setNumberOfUsers(2);
		t.setPhoneSupport(true);
		t.setPromoCode("promo");
		t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
		t.setPaymentType(TenantInfo.payByPurchaseOrder);
		t.setPurchaseOrderNumber("88888");

		signUpPage.enterCreateAccountForm(t);
        signUpPage.submitCreateAccountForm();

        mailServer.waitForMessages(1);
        MailMessage activationMessage = mailServer.getAndClearMessages().get(0);
        return new SignUpEmailLoginNavigator().navigateToSignInPageSpecifiedIn(activationMessage, selenium);
	}

}
