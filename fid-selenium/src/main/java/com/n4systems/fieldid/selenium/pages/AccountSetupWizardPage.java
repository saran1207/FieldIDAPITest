package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.*;

import java.util.List;

import com.thoughtworks.selenium.Selenium;

public class AccountSetupWizardPage extends FieldIDPage {
	
	private static final String READY_LETS_GO_BUTTON_XPATH = "//input[@id='startButton']";
	private static final String STEP_1_COMPLETE_XPATH = "//input[@id='step1Complete_label_next']";
	private static final String STEP_1_PAGE_HEADER_XPATH = "//div[@id='contentTitle']/H1[contains(text(),'Setup Wizard - Step 1')]";
	private static final String STEP_2_PAGE_HEADER_XPATH = "//div[@id='contentTitle']/H1[contains(text(),'Setup Wizard - Step 2')]";
	private static final String PREFERRED_DATE_FORMAT_SELECT_LIST_XPATH = "//select[@id='step2Complete_dateFormat']";
	private static final String DEFAULT_VENDOR_CONTEXT_SELECT_LIST_XPATH = "//select[@id='step2Complete_defaultVendorContext']";
	private static final String WEBSITE_ADDRESS_TEXT_FIELD_XPATH = "//input[@id='step2Complete_webSite']";
	private static final String STEP_2_COMPLETE_BUTTON_XPATH = "//input[@id='step2Complete_label_next']";
	private static final String STEP_3_PAGE_HEADER_XPATH = "//div[@id='contentTitle']/H1[contains(text(),'Setup Wizard - Step 3')]";
	private static final String IMPORT_FORM = "//form[@id='importForm']";
	private static final String STEP_3_COMPLETE_LINK_XPATH = "//input[@id='continue']";
	private static final String WIZARD_DONE_PAGE_HEADER_XPATH = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Setup Wizard - Done')]";
	
	public AccountSetupWizardPage(Selenium selenium) {
		super(selenium);
	}

    public AccountSetupWizardPage(Selenium selenium, boolean waitForPageToLoad) {
        super(selenium, waitForPageToLoad);
    }

	public void verifyQuickSetupWizardPageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard page", selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Setup Wizard')]"));
		checkForErrorMessages(null);
	}

	public HomePage clickNoThanks() {
		selenium.click("//a[contains(text(),'No Thanks')]");
		return new HomePage(selenium);
	}

	public void clickImReadyLetsGo() {
		selenium.click(READY_LETS_GO_BUTTON_XPATH);
		waitForPageToLoad();
	}

	public void verifyQuickSetupWizardStep1PageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Step 1", selenium.isElementPresent(STEP_1_PAGE_HEADER_XPATH));
		checkForErrorMessages(null);
	}

	public void gotoQuickSetupWizardStep2() {
		selenium.click(STEP_1_COMPLETE_XPATH);
		waitForPageToLoad();
	}

	public void verifyCompanyProfileSetup() {
		List<String> successMsgs = getActionMessages();
		List<String> errorMsgs = getFormErrorMessages();
		assertTrue("Could not find the action message 'Your company profile has been setup'", successMsgs.contains("Your company profile has been setup"));
		assertTrue("There were errors on the page: " + errorMsgs, errorMsgs.size() == 0);
	}

	public void verifyQuickSetupWizardStep2PageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Step 2", selenium.isElementPresent(STEP_2_PAGE_HEADER_XPATH));
		checkForErrorMessages(null);
	}

	public void verifyQuickSetupWizardSystemSettingsPage(boolean referrer) {
		assertTrue("Could not find the Preferred Date Format select list", selenium.isElementPresent(PREFERRED_DATE_FORMAT_SELECT_LIST_XPATH));
		if(referrer) {
			assertTrue("Could not find the Default Vendor Context select list", selenium.isElementPresent(DEFAULT_VENDOR_CONTEXT_SELECT_LIST_XPATH));
		}
	}

	public String getPreferredDateFormat() {
		return getSelectedValueIfPresent(PREFERRED_DATE_FORMAT_SELECT_LIST_XPATH);
	}

	public String getDefaultVendorContext() {
		return getSelectedLabelIfPresent(DEFAULT_VENDOR_CONTEXT_SELECT_LIST_XPATH);
	}

	public String getWebSiteAddress() {
		return getValueIfPresent(WEBSITE_ADDRESS_TEXT_FIELD_XPATH);
	}

	public void gotoQuickSetupWizardStep3() {
		selenium.click(STEP_2_COMPLETE_BUTTON_XPATH);
		waitForPageToLoad();
	}

	public void verifySystemSettingsUpdated() {
		List<String> successMsgs = getActionMessages();
		List<String> errorMsgs = getFormErrorMessages();
		assertTrue("Could not find the action message 'System settings updated'", successMsgs.contains("System settings updated"));
		assertTrue("There were errors on the page: " + errorMsgs, errorMsgs.size() == 0);
	}

	public void verifyQuickSetupWizardStep3PageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Step 3", selenium.isElementPresent(STEP_3_PAGE_HEADER_XPATH));
		checkForErrorMessages(null);
	}

	public void verifyQuickSetupWizardImportTemplatesPage() {
		assertTrue("Could not find the import form", selenium.isElementPresent(IMPORT_FORM));
	}

	public void gotoSkipImport() {
		selenium.click(STEP_3_COMPLETE_LINK_XPATH);
		waitForPageToLoad();
	}

	public void verifyQuickSetupWizardDonePage() {
		assertTrue("Could not find the header for the Quick Setup Wizard Done page", selenium.isElementPresent(WIZARD_DONE_PAGE_HEADER_XPATH));
		checkForErrorMessages(null);
	}
	
}
