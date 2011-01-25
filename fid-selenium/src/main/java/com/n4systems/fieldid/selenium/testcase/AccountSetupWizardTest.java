package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.pages.setup.BrandingPage;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.SystemSettings;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;
import com.n4systems.fieldid.selenium.misc.CreateTenants;
import com.n4systems.fieldid.selenium.pages.AccountSetupWizardPage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.setup.SystemSettingsPage;

public class AccountSetupWizardTest extends FieldIDTestCase {

	CreateTenants ct;
	
	@Before
	public void setUp() throws Exception {
		ct = new CreateTenants(selenium, misc);
	}

	@Test
	public void admin_logs_in_says_no_thanks_to_setup_wizard() {
		TenantInfo t = createARandomNewBasicTenant("fieldid");
		LoginPage loginPage = t.getLoginPage();
		AccountSetupWizardPage wizardPage = loginPage.signInAllTheWayToWizard(t.getUserName(), t.getPassword());
		assertNoThanksWorks(wizardPage);
	}

	@Test
	public void no_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		TenantInfo t = createARandomNewBasicTenant("fieldid");
		AccountSetupWizardPage wizardPage = t.getLoginPage().signInAllTheWayToWizard(t.getUserName(), t.getPassword());
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks(wizardPage, false);
		verifyDefaultSettingsConfiguredProperly(wizardPage, defaults);
	}
	

	private void assertNoThanksWorks(AccountSetupWizardPage wizardPage) {
		wizardPage.verifyQuickSetupWizardPageHeader();
		wizardPage.clickNoThanks();
	}

	private TenantInfo createARandomNewBasicTenant(String referrer) {
		startAsCompany(referrer);
		String promoCode = "";	// no promo code
        return ct.createARandomNewTenant(SignUpPackages.packageTypePlus, promoCode, mailServer);
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
