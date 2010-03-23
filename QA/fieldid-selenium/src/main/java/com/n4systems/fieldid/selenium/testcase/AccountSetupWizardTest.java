package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageSystemSettings;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.datatypes.SystemSettings;
import com.n4systems.fieldid.selenium.home.page.Home;
import com.n4systems.fieldid.selenium.home.page.QuickSetupWizard;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.login.page.SignUpComplete;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;
import com.n4systems.fieldid.selenium.misc.CreateTenants;

/**
 * WEB-1501 and WEB-1508
 * 
 * @author dgrainge
 *
 */
public class AccountSetupWizardTest extends FieldIDTestCase {

	Login login;
	SignUpPackages sup;
	CreateAccount create;
	SignUpComplete complete;
	CreateTenants ct;
	QuickSetupWizard qsw;
	Home home;
	Admin admin;
	ManageSystemSettings mss;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		sup = new SignUpPackages(selenium, misc);
		create = new CreateAccount(selenium, misc);
		complete = new SignUpComplete(selenium, misc);
		ct = new CreateTenants(selenium, misc);
		qsw = new QuickSetupWizard(selenium, misc);
		home = new Home(selenium, misc);
		admin = new Admin(selenium, misc);
		mss = new ManageSystemSettings(selenium, misc);
	}

	@Test
	public void admin_logs_in_says_no_thanks_to_setup_wizard() {
		CreateTenant t = createARandomNewBasicTenant("fieldid");
		login.signIn(t.getUserName(), t.getPassword());
		assertNoThanksWorks();
	}

	@Test
	public void no_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		CreateTenant t = createARandomNewBasicTenant("fieldid");
		login.signIn(t.getUserName(), t.getPassword());
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks(false);
		assertDefaultSettingsConfiguredProperly(defaults);
	}
	
	@Test
	public void msa_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		String referrer = "msa";
		CreateTenant t = createARandomNewBasicTenant(referrer);
		login.signIn(t.getUserName(), t.getPassword());
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks(true);
		assertDefaultSettingsConfiguredProperly(defaults);
		assertDefaultVendorIsMSA(defaults);
	}
	
	private void assertDefaultVendorIsMSA(SystemSettings ss) {
		assertTrue(ss.getDefaultVendorContext().equals("MSA"));
	}

	private void assertNoThanksWorks() {
		qsw.verifyQuickSetupWizardPageHeader();
		qsw.gotoNoThanks();
		home.assertHomePageHeader();
	}

	private CreateTenant createARandomNewBasicTenant(String referrer) {
		setCompany(referrer);
		String promoCode = "";	// no promo code
		CreateTenant t = ct.createARandomNewTenant(SignUpPackages.packageTypeBasic, promoCode);
		return t;
	}

	private void assertDefaultSettingsConfiguredProperly(SystemSettings expected) {
		misc.gotoAdministration();
		admin.gotoManageSystemSettings();
		boolean hasVendors = (expected.getDefaultVendorContext() != null);
		SystemSettings ss = mss.getSystemSettings(hasVendors);
		verifyTrue(expected.getPreferredDateFormat().equals(ss.getPreferredDateFormat()));
		if(hasVendors) {
			verifyTrue(expected.getDefaultVendorContext().equals(ss.getDefaultVendorContext()));
		}
		if(expected.getWebSiteAddress() != null) {
			verifyTrue(expected.getWebSiteAddress().equals(ss.getWebSiteAddress()));
		} else
			verifyTrue(ss.getWebSiteAddress() == null);
	}

	private SystemSettings acceptDefaultSettingsForSetupWizardWorks(boolean referrer) {
		SystemSettings ss = new SystemSettings();
		qsw.gotoImReadyLetsGo();
		qsw.verifyQuickSetupWizardStep1PageHeader();
		qsw.gotoQuickSetupWizardStep2();
		qsw.verifyCompanyProfileSetup();
		qsw.verifyQuickSetupWizardStep2PageHeader();
		qsw.verifyQuickSetupWizardSystemSettingsPage(referrer);
		String pdf = qsw.getPreferredDateFormat();
		ss.setPreferredDateFormat(pdf);
		if(referrer) {
			String dvc = qsw.getDefaultVendorContext();
			ss.setDefaultVendorContext(dvc);
		}
		String wsa = qsw.getWebSiteAddress();
		ss.setWebSiteAddress(wsa);
		qsw.gotoQuickSetupWizardStep3();
		qsw.verifySystemSettingsUpdated();
		qsw.verifyQuickSetupWizardStep3PageHeader();
		qsw.verifyQuickSetupWizardImportTemplatesPage();
		qsw.gotoSkipImport();
		qsw.verifyQuickSetupWizardDonePageHeader();
		qsw.verifyQuickSetupWizardDonePage();
		return ss;
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
