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
	private String productTypeTableXpath = "//DIV[@id='pageContent']/TABLE[@class='list']";
	private String productTypeCountXpath = productTypeTableXpath + "/TBODY/TR/TD/A";
	
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
	
	public List<String> getProductTypes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(productTypeCountXpath);
		int row = 2;
		String productTypeLinkLocator = "xpath=" + productTypeTableXpath + "." + row + ".0";
		int numProductTypes = n.intValue();
		for(int i = 0; i < numProductTypes; i++, row++) {
			String productType = selenium.getTable(productTypeLinkLocator);
			result.add(productType.trim());
			productTypeLinkLocator = productTypeLinkLocator.replaceFirst("\\." + row, "." + (row+1));
		}
		return result;
	}
	
	public void gotoProductType(String productType) {
		String productTypeLinkLocator = "xpath=" + productTypeCountXpath + "[contains(text(),'" + productType + "')]";
		if(selenium.isElementPresent(productTypeLinkLocator)) {
			selenium.click(productTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to asset type '" + productType + "'");
		}
	}
	
	public void verifyEditProductTypeAutoAttributeWizardPage(String productType) {
		String editProductTypeHeaderLocator = "//FORM[@id='autoAttributeCriteriaEdit']/H2[contains(text(),'" + productType + "')]";
		assertTrue("Could not find the header for asset type '" + productType + "'", selenium.isElementPresent(editProductTypeHeaderLocator));
	}
	
	

}
