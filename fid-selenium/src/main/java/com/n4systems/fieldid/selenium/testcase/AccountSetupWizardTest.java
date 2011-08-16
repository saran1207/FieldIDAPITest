package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import com.n4systems.fieldid.selenium.pages.admin.AdminCreateTenantPage;
import org.junit.Test;

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
import com.n4systems.fieldid.selenium.util.SignUpEmailLoginNavigator;

public class AccountSetupWizardTest extends FieldIDTestCase {

    private static final String TENANT_TO_CREATE = TEST_CREATED_TENANT_NAMES[0];
    private static final String TEST_USER_NAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";

    @Test
	public void admin_logs_in_says_no_thanks_to_setup_wizard() {
		LoginPage loginPage = createNewBasicTenant();
		AccountSetupWizardPage wizardPage = loginPage.signInAllTheWayToWizard(TEST_USER_NAME, TEST_PASSWORD);
		assertNoThanksWorks(wizardPage);
	}

	@Test
	public void no_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		LoginPage loginPage = createNewBasicTenant();
		AccountSetupWizardPage wizardPage = loginPage.signInAllTheWayToWizard(TEST_USER_NAME, TEST_PASSWORD);
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks(wizardPage, false);
		verifyDefaultSettingsConfiguredProperly(wizardPage, defaults);
	}

	private void assertNoThanksWorks(AccountSetupWizardPage wizardPage) {
		wizardPage.verifyQuickSetupWizardPageHeader();
		wizardPage.clickNoThanks();
	}

	private LoginPage createNewBasicTenant() {
        AdminCreateTenantPage createTenantPage = startAdmin().login().clickCreateANewTenant();

        createTenantPage.enterNumEmployeeUsers(2);
        createTenantPage.selectPackage("Plus");

        createTenantPage.enterName(TENANT_TO_CREATE);
        createTenantPage.enterUserId(TEST_USER_NAME);
        createTenantPage.enterFirstName("John");
        createTenantPage.enterLastName("Doe");
        createTenantPage.enterEmailAddress("at@dot.com");
        createTenantPage.enterPrimaryOrgName("org");
        createTenantPage.enterPrimaryNetsuiteUser("nsuser");
        createTenantPage.enterPrimaryNetsuitePassword("nspass");
        createTenantPage.submitForm();

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

        page.verifyAddFirstCustomerPage();
        page.gotoQuickSetupWizardStep3();

		page.verifyQuickSetupWizardSystemSettingsPage(referrer);
		String prefDateFormat = page.getPreferredDateFormat();
		systemSettings.setPreferredDateFormat(prefDateFormat);
		String wsa = page.getWebSiteAddress();
		systemSettings.setWebSiteAddress(wsa);
        page.gotoQuickSetupWizardStep4();

		page.verifySystemSettingsUpdated();
		page.verifyQuickSetupWizardStep4PageHeader();
		page.verifyQuickSetupWizardImportTemplatesPage();
		page.gotoSkipImport();
		page.verifyQuickSetupWizardDonePage();
		return systemSettings;
	}

}
