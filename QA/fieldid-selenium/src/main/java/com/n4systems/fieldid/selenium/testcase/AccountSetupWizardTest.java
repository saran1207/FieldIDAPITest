package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.Admin;
import com.n4systems.fieldid.selenium.administration.ManageSystemSettings;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.datatypes.SystemSettings;
import com.n4systems.fieldid.selenium.home.Home;
import com.n4systems.fieldid.selenium.home.QuickSetupWizard;
import com.n4systems.fieldid.selenium.login.CreateAccount;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.login.SignUpComplete;
import com.n4systems.fieldid.selenium.login.SignUpPackages;
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
		login.loginAcceptingEULAIfNecessary(t.getUserName(), t.getPassword());
		assertNoThanksWorks();
	}

	@Test
	public void no_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		CreateTenant t = createARandomNewBasicTenant("fieldid");
		login.loginAcceptingEULAIfNecessary(t.getUserName(), t.getPassword());
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks();
		assertDefaultSettingsConfiguredProperly(defaults);
	}
	
	@Test
	public void msa_referral_basic_admin_logs_in_accepts_all_defaults_for_setup_wizard() {
		String referrer = "msa";
		CreateTenant t = createARandomNewBasicTenant(referrer);
		login.loginAcceptingEULAIfNecessary(t.getUserName(), t.getPassword());
		SystemSettings defaults = acceptDefaultSettingsForSetupWizardWorks();
		assertDefaultSettingsConfiguredProperly(defaults);
		assertDefaultVendorIsMSA(defaults);
	}
	
	private void assertDefaultVendorIsMSA(SystemSettings ss) {
		assertTrue(ss.getDefaultVendorContext().equals("MSA"));
	}

	private void assertNoThanksWorks() {
		qsw.verifyQuickSetupWizardPageHeader();
		qsw.gotoNoThanks();
		home.verifyHomePageHeader();
	}

	private CreateTenant createARandomNewBasicTenant(String referrer) {
		setCompany(referrer);
		String promoCode = "";	// no promo code
		CreateTenant t = ct.createARandomNewTenant(SignUpPackages.packageTypeBasic, promoCode);
		misc.info("Tenant created: " + t);
		return t;
	}

	private void assertDefaultSettingsConfiguredProperly(SystemSettings expected) {
		misc.gotoAdministration();
		admin.gotoManageSystemSettings();
		SystemSettings ss = mss.getSystemSettings();
		verifyTrue(expected.getPreferredDateFormat().equals(ss.getPreferredDateFormat()));
		verifyTrue(expected.getDefaultVendorContext().equals(ss.getDefaultVendorContext()));
		if(expected.getWebSiteAddress() != null) {
			verifyTrue(expected.getWebSiteAddress().equals(ss.getWebSiteAddress()));
		} else
			verifyTrue(ss.getWebSiteAddress() == null);
	}

	private SystemSettings acceptDefaultSettingsForSetupWizardWorks() {
		SystemSettings ss = new SystemSettings();
		qsw.gotoImReadyLetsGo();
		qsw.verifyQuickSetupWizardStep1PageHeader();
		qsw.gotoQuickSetupWizardStep2();
		qsw.verifyCompanyProfileSetup();
		qsw.verifyQuickSetupWizardStep2PageHeader();
		qsw.verifyQuickSetupWizardSystemSettingsPage();
		String pdf = qsw.getPreferredDateFormat();
		ss.setPreferredDateFormat(pdf);
		String dvc = qsw.getDefaultVendorContext();
		ss.setDefaultVendorContext(dvc);
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
