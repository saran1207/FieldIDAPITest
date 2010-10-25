package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class AutoAttributeWizard {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String autoAttributeWizardPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Auto Attribute Wizard')]";
	private String assetTypeTableXpath = "//DIV[@id='pageContent']/TABLE[@class='list']";
	private String assetTypeCountXpath = assetTypeTableXpath + "/TBODY/TR/TD/A";
	
	public AutoAttributeWizard(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyAutoAttributeWizardPage() {
		misc.checkForErrorMessages("verifyAutoAttributeWizardPage");
		if(!selenium.isElementPresent(autoAttributeWizardPageHeaderLocator)) {
			fail("Could not find the header for 'Auto Attribute Wizard'.");
		}
	}
	
	public List<String> getAssetTypes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(assetTypeCountXpath);
		int row = 2;
		String assetTypeLinkLocator = "xpath=" + assetTypeTableXpath + "." + row + ".0";
		int numAssetTypes = n.intValue();
		for(int i = 0; i < numAssetTypes; i++, row++) {
			String productType = selenium.getTable(assetTypeLinkLocator);
			result.add(productType.trim());
			assetTypeLinkLocator = assetTypeLinkLocator.replaceFirst("\\." + row, "." + (row+1));
		}
		return result;
	}
	
	public void gotoAssetType(String assetType) {
		String assetTypeLinkLocator = "xpath=" + assetTypeCountXpath + "[contains(text(),'" + assetType + "')]";
		if(selenium.isElementPresent(assetTypeLinkLocator)) {
			selenium.click(assetTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to asset type '" + assetType + "'");
		}
	}
	
	public void verifyEditAssetTypeAutoAttributeWizardPage(String assetType) {
		String editAssetTypeHeaderLocator = "//FORM[@id='autoAttributeCriteriaEdit']/H2[contains(text(),'" + assetType + "')]";
		assertTrue("Could not find the header for asset type '" + assetType + "'", selenium.isElementPresent(editAssetTypeHeaderLocator));
	}
	
	

}
