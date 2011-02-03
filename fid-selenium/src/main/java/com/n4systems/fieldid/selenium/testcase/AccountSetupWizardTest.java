package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.SystemSettings;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.pages.AccountSetupWizardPage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SetPasswordPage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.pages.setup.BrandingPage;
import com.n4systems.fieldid.selenium.pages.setup.SystemSettingsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.util.SignUpEmailLoginNavigator;
import com.n4systems.util.ConfigEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountSetupWizardTest extends FieldIDTestCase {

    private static final String REFERRING_TENANT = TEST_TENANT_NAMES[0];
    private static final String TENANT_TO_CREATE = TEST_CREATED_TENANT_NAMES[0];
    private static final String TEST_USER_NAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.updateConfigurationForTenant(REFERRING_TENANT, ConfigEntry.EXTERNAL_PLANS_AND_PRICING_ENABLED, "true");
    }

    @Test
	public void admin_logs_in_says_no_thanks_to_setup_wizard() {
		LoginPage loginPage = createNewBasicTenant(REFERRING_TENANT);
		AccountSetupWizardPage wizardPage = loginPage.signInAllTheWayToWizard(TEST_USER_NAME, TEST_PASSWORD);
		assertNoThanksWorks(wizardPage);
	}

	@Test
	public void no_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		LoginPage loginPage = createNewBasicTenant(REFERRING_TENANT);
		AccountSetupWizardPage wizardPage = loginPage.signInAllTheWayToWizard(TEST_USER_NAME, TEST_PASSWORD);
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks(wizardPage, false);
		verifyDefaultSettingsConfiguredProperly(wizardPage, defaults);
	}

	private void assertNoThanksWorks(AccountSetupWizardPage wizardPage) {
		wizardPage.verifyQuickSetupWizardPageHeader();
		wizardPage.clickNoThanks();
	}

	private LoginPage createNewBasicTenant(String referrer) {
        SelectPackagePage selectPackagePage = startAsCompany(referrer).clickPlansAndPricingLink();
        String packageName = "Plus";	// must be unlimited
        SignUpPage signUpPage = selectPackagePage.clickSignUpNowLink(packageName);

        TenantInfo t = new TenantInfo(TEST_USER_NAME, TEST_PASSWORD, TENANT_TO_CREATE, TENANT_TO_CREATE);
        t.setNumberOfUsers(2);
        t.setPhoneSupport(true);
        t.setPromoCode("");
        t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
        t.setPaymentType(TenantInfo.payByPurchaseOrder);
        t.setPurchaseOrderNumber("88888");

        signUpPage.enterCreateAccountForm(t);
        signUpPage.submitCreateAccountForm();

        mailServer.waitForMessages(1);
        MailMessage activationMessage = mailServer.getAndClearMessages().get(0);
        SetPasswordPage setPasswordPage = new SignUpEmailLoginNavigator().navigateToSignInPageSpecifiedIn(activationMessage, selenium);
        setPasswordPage.enterAndConfirmPassword(TEST_PASSWORD);
        return setPasswordPage.submitConfirmPassword();
    }

	private void verifyDefaultSettingsConfiguredProperly(AccountSetupWizardPage wizardPage, SystemSettings expected) {
		SystemSettingsPage settingsPage = wizardPage.clickSetupLink().clickSystemSettings();
		assertEquals(expected.getPreferredDateFormat(), settingsPage.getPreferredDateFormat());
        BrandingPage brandingPage = settingsPage.clickSetupLink().clickBranding();
		assertEquals(expected.getWebSiteAddress(), brandingPage.getWebSiteAddress());
	}

	private SystemSettings acceptDefaultSettingsForSetupWizardWorks(AccountSetupWizardPage page, boolean referrer) {
		SystemSettings systemSettings = new SystemSettings();
		
		page.clickImReadyLetsGo();
		page.verifyQuickSetupWizardStep1PageHeader();
		page.gotoQuickSetupWizardStep2();
		page.verifyCompanyProfileSetup();
		page.verifyQuickSetupWizardStep2PageHeader();
		page.verifyQuickSetupWizardSystemSettingsPage(referrer);
		String prefDateFormat = page.getPreferredDateFormat();
		systemSettings.setPreferredDateFormat(prefDateFormat);
		String wsa = page.getWebSiteAddress();
		systemSettings.setWebSiteAddress(wsa);
		page.gotoQuickSetupWizardStep3();
		page.verifySystemSettingsUpdated();
		page.verifyQuickSetupWizardStep3PageHeader();
		page.verifyQuickSetupWizardImportTemplatesPage();
		page.gotoSkipImport();
		page.verifyQuickSetupWizardDonePage();
		return systemSettings;
	}

}
