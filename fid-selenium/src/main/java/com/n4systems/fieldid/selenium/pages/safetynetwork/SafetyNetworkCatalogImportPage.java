package com.n4systems.fieldid.selenium.pages.safetynetwork;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkCatalogImportPage extends FieldIDPage {

	public SafetyNetworkCatalogImportPage(Selenium selenium) {
		super(selenium);
		if(!checkOnCatalogImportPage()){
			fail("Expected to be on asset list page!");
		}
	}

	private boolean checkOnCatalogImportPage() {
		return (selenium.isElementPresent("//h1[contains(text(), 'View Catalog')]"));
	}

	public void clickSelectItemsToImportButton(){
		selenium.click("//div[@class='stepAction']/input");
		waitForAjax();
	}
	
	public void clickFirstProductTypeCheckBox(){
		selenium.check("//ul[1]/li/span/input[1]");
		assertTrue("Checkbox wasn't successfully toggled", selenium.isChecked("//ul[1]/li/span/input[1]"));
	}
	
	public void clickContinue(){
		selenium.click("//input[@id='continue']");
		waitForAjax();
	}
	
	public void clickStartImport(){
		selenium.click("//input[@id='import']");
		waitForAjax();
	}
	
}
