package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AccountSetupWizardPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class QuickSetupWizardCausesErrorOnSessionTimeoutTest extends FieldIDTestCase {

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard() {
        AccountSetupWizardPage setupWizardPage = startAsCompany("test1").login().clickSetupLink().clickSetupWizard();
        setupWizardPage.forceSessionTimeout();
		assertSessionTimeoutLightboxAppeared(setupWizardPage);
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_step1() {
        AccountSetupWizardPage setupWizardPage = startAsCompany("test1").login().clickSetupLink().clickSetupWizard();
        setupWizardPage.clickImReadyLetsGo();
        setupWizardPage.forceSessionTimeout();
		assertSessionTimeoutLightboxAppeared(setupWizardPage);
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_step2() {
        AccountSetupWizardPage setupWizardPage = startAsCompany("test1").login().clickSetupLink().clickSetupWizard();
        setupWizardPage.clickImReadyLetsGo();
        setupWizardPage.gotoQuickSetupWizardStep2();
        setupWizardPage.forceSessionTimeout();
		assertSessionTimeoutLightboxAppeared(setupWizardPage);
	}

	@Test
	public void no_error_dialog_if_session_times_out_in_quick_setup_wizard_step3() {
        AccountSetupWizardPage setupWizardPage = startAsCompany("test1").login().clickSetupLink().clickSetupWizard();
        setupWizardPage.clickImReadyLetsGo();
        setupWizardPage.gotoQuickSetupWizardStep2();
		setupWizardPage.gotoQuickSetupWizardStep3();
		setupWizardPage.forceSessionTimeout();
		assertSessionTimeoutLightboxAppeared(setupWizardPage);
	}

	private void assertSessionTimeoutLightboxAppeared(FieldIDPage page) {
		assertTrue("The session timeout lightbox is not present", page.isSessionExpiredLightboxVisible());
	}

}
