package com.n4systems.fieldid.selenium.home;

import static org.junit.Assert.*;
import java.util.List;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

public class QuickSetupWizard {

	private Selenium selenium;
	private Misc misc;
	
	// Locators
	private String quickSetupWizardPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Quick Setup Wizard')]";
	private String noThanksLinkLocator = "xpath=//A[contains(text(),'No Thanks')]";
	private String readyLetsGoButtonLocator = "xpath=//BUTTON[@id='startButton']";
	private String quickSetupWizardStep1PageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Quick Setup Wizard - Step 1 of 3')]";
	private String step1CompleteButtonLocator = "xpath=//INPUT[@id='step1Complete_label_next']";
	private String companyProfileSetupMessage = "Your company profile has been setup";
	private String quickSetupWizardStep2PageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Quick Setup Wizard - Step 2 of 3')]";
	private String preferredDateFormatSelectListLocator = "xpath=//SELECT[@id='step2Complete_dateFormat']";
	private String defaultVendorContextSelectListLocator = "xpath=//SELECT[@id='step2Complete_defaultVendorContext']";
	private String webSiteAddressTextFieldLocator = "xpath=//INPUT[@id='step2Complete_webSite']";
	private String step2CompleteButtonLocator = "xpath=//INPUT[@id='step2Complete_label_next']";
	private String systemSettingsUpdatedMessage = "System settings updated";
	private String quickSetupWizardStep3PageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Quick Setup Wizard - Step 3 of 3')]";
	private String fieldIDImportDescriptionString = "Field ID provides suggested templates for a wide range of equipment.";
	private String fieldIDImportRadioButtonLocator = "xpath=//TD[contains(text(),'" + fieldIDImportDescriptionString + "')]/../TD[1]/INPUT[contains(@id,'step3Import_uniqueID')]";
	private String step3CompleteLinkLocator = "xpath=//A[contains(text(),'Skip')]";
	private String quickSetupWizardDonePageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Quick Setup Wizard - Done')]";
	private String setupYourMobileComputerLinkLocator = "xpath=//A[contains(text(),'Setup your mobile computers')]";
	private String identifyYourFirstAssetLinkLocator = "xpath=//A[contains(text(),'Identify your first asset')]";
	private String furtherCustomizeYourAccountSetupLinkLocator = "xpath=//A[contains(text(),'Further customize your account setup')]";
	private String visitTheHelpDocumentationLinkLocator = "xpath=//A[contains(text(),'Visit the help documentation')]";
	private String watchTheIntroductionVideoLinkLocator = "xpath=//A[contains(text(),'Watch the introduction video')]";
	private String previewSystemLogoImageLocator = "xpath=//IMG[@id='previewImage')]";
	
	public QuickSetupWizard(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifyQuickSetupWizardPageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard page", selenium.isElementPresent(quickSetupWizardPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public void gotoNoThanks() {
		misc.info("Click the No Thanks link");
		if(selenium.isElementPresent(noThanksLinkLocator)) {
			selenium.click(noThanksLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the 'No Thanks' link");
		}
	}

	public void gotoImReadyLetsGo() {
		misc.info("Click the I'm Ready - Lets Go! button");
		if(selenium.isElementPresent(readyLetsGoButtonLocator)) {
			selenium.click(readyLetsGoButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the I'm Ready - Lets Go! button");
		}
	}

	public void verifyQuickSetupWizardStep1PageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Step 1 of 3 page", selenium.isElementPresent(quickSetupWizardStep1PageHeaderLocator));
		misc.checkForErrorMessages(null);
	}

	public void gotoQuickSetupWizardStep2() {
		misc.info("Click the 'Next ->' button");
		if(selenium.isElementPresent(step1CompleteButtonLocator)) {
			selenium.click(step1CompleteButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the 'Next ->' button");
		}
	}

	public void verifyCompanyProfileSetup() {
		List<String> successMsgs = misc.getActionMessages();
		List<String> errorMsgs = misc.getErrorMessages();
		assertTrue("Could not find the action message '" + companyProfileSetupMessage + "'", successMsgs.contains(companyProfileSetupMessage));
		assertTrue("There were errors on the page: " + misc.convertListToString(errorMsgs), errorMsgs.size() == 0);
	}

	public void verifyQuickSetupWizardStep2PageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Step 2 of 3 page", selenium.isElementPresent(quickSetupWizardStep2PageHeaderLocator));
		misc.checkForErrorMessages(null);
	}

	public void verifyQuickSetupWizardSystemSettingsPage() {
		assertTrue("Could not find the Preferred Date Format select list", selenium.isElementPresent(preferredDateFormatSelectListLocator));
		assertTrue("Could not find the Default Vendor Context select list", selenium.isElementPresent(defaultVendorContextSelectListLocator));
	}

	public void gotoQuickSetupWizardStep3() {
		misc.info("Click the 'Next ->' button");
		if(selenium.isElementPresent(step2CompleteButtonLocator)) {
			selenium.click(step2CompleteButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the 'Next ->' button");
		}
	}

	public void verifySystemSettingsUpdated() {
		List<String> successMsgs = misc.getActionMessages();
		List<String> errorMsgs = misc.getErrorMessages();
		assertTrue("Could not find the action message '" + systemSettingsUpdatedMessage + "'", successMsgs.contains(systemSettingsUpdatedMessage));
		assertTrue("There were errors on the page: " + misc.convertListToString(errorMsgs), errorMsgs.size() == 0);
	}

	public void verifyQuickSetupWizardStep3PageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Step 3 of 3 page", selenium.isElementPresent(quickSetupWizardStep3PageHeaderLocator));
		misc.checkForErrorMessages(null);
	}

	public void verifyQuickSetupWizardImportTemplatesPage() {
		assertTrue("Could not find the Field ID acount radio button", selenium.isElementPresent(fieldIDImportRadioButtonLocator));
	}

	public void gotoSkipImport() {
		misc.info("Click the 'Skip' link");
		if(selenium.isElementPresent(step3CompleteLinkLocator)) {
			selenium.click(step3CompleteLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the 'Next ->' button");
		}
	}

	public void verifyQuickSetupWizardDonePageHeader() {
		assertTrue("Could not find the header for the Quick Setup Wizard Done page", selenium.isElementPresent(quickSetupWizardDonePageHeaderLocator));
		misc.checkForErrorMessages(null);
	}

	public void verifyQuickSetupWizardDonePage() {
		assertTrue("Could not find the Setup your mobile computer link", selenium.isElementPresent(setupYourMobileComputerLinkLocator));
		assertTrue("Could not find the Identify your first asset link", selenium.isElementPresent(identifyYourFirstAssetLinkLocator));
		assertTrue("Could not find the Further customize your account setup link", selenium.isElementPresent(furtherCustomizeYourAccountSetupLinkLocator));
		assertTrue("Could not find the Visit the help documentation link", selenium.isElementPresent(visitTheHelpDocumentationLinkLocator));
		assertTrue("Could not find the Watch the introduction video link", selenium.isElementPresent(watchTheIntroductionVideoLinkLocator));
	}

	public String getPreferredDateFormat() {
		String result = null;
		if(selenium.isElementPresent(preferredDateFormatSelectListLocator)) {
			result = selenium.getSelectedValue(preferredDateFormatSelectListLocator);
		} else {
			fail("Could not locator the Preferred Date Format select list");
		}
		return result;
	}

	public String getDefaultVendorContext() {
		String result = null;
		if(selenium.isElementPresent(defaultVendorContextSelectListLocator)) {
			result = selenium.getSelectedLabel(defaultVendorContextSelectListLocator);
		} else {
			fail("Could not locator the Default Vendor Context select list");
		}
		return result;
	}

	/**
	 * System Logo does not exist for all tenants. If it returns null
	 * and you expected it to exist, you need to throw an error.
	 * 
	 * @return
	 */
	public boolean isSystemLogoExists() {
		boolean result = selenium.isElementPresent(previewSystemLogoImageLocator);
		return result;
	}

	/**
	 * Web Site Address does not exist for all tenants. If it returns null
	 * and you expected it to exist, you need to throw an error.
	 * 
	 * @return
	 */
	public String getWebSiteAddress() {
		String result = null;
		if(selenium.isElementPresent(webSiteAddressTextFieldLocator)) {
			result = selenium.getValue(webSiteAddressTextFieldLocator);
		}
		return result;
	}
}
