package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.home.page.QuickSetupWizard;
import com.n4systems.fieldid.selenium.login.page.Login;

/**
 * WEB-1569
 * 
 * @author dgrainge
 *
 */
public class QuickSetupWizardCausesErrorOnSessionTimeoutTest extends FieldIDTestCase {

	Login login;
	QuickSetupWizard qsw;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		qsw = new QuickSetupWizard(selenium, misc);
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard() {
		startAsCompany("n4");
		login.signInAllTheWayToHome("saunders", "makemore$");
		qsw.gotoQuickSetupWizard();
		misc.forceSessionTimeout(getFieldIDDomain());
		assertSessionTimeoutLightboxAppeared();
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_step1() {
		startAsCompany("n4");
		login.signInAllTheWayToHome("saunders", "makemore$");
		qsw.gotoQuickSetupWizard();
		qsw.gotoImReadyLetsGo();
		misc.forceSessionTimeout(getFieldIDDomain());
		assertSessionTimeoutLightboxAppeared();
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_step2() {
		startAsCompany("n4");
		login.signInAllTheWayToHome("saunders", "makemore$");
		qsw.gotoQuickSetupWizard();
		qsw.gotoImReadyLetsGo();
		qsw.gotoQuickSetupWizardStep2();
		misc.forceSessionTimeout(getFieldIDDomain());
		assertSessionTimeoutLightboxAppeared();
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_step3() {
		startAsCompany("n4");
		login.signInAllTheWayToHome("saunders", "makemore$");
		qsw.gotoQuickSetupWizard();
		qsw.gotoImReadyLetsGo();
		qsw.gotoQuickSetupWizardStep2();
		qsw.gotoQuickSetupWizardStep3();
		misc.forceSessionTimeout(getFieldIDDomain());
		assertSessionTimeoutLightboxAppeared();
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_you_are_done() {
		startAsCompany("n4");
		login.signInAllTheWayToHome("saunders", "makemore$");
		qsw.gotoQuickSetupWizard();
		qsw.gotoImReadyLetsGo();
		qsw.gotoQuickSetupWizardStep2();
		qsw.gotoQuickSetupWizardStep3();
		qsw.gotoSkipImport();
		misc.forceSessionTimeout(getFieldIDDomain());
		assertSessionTimeoutLightboxAppeared();
	}

	private void assertSessionTimeoutLightboxAppeared() {
		assertTrue("The session timeout lightbox is not present", misc.isSessionExpired());
	}

}
