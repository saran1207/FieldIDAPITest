package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class AssetPage extends FieldIDPage {

	public AssetPage(Selenium selenium) {
		super(selenium);
		if(!checkOnAssetPage()){
			fail("Expected to be on asset page!");
		}
	}
	
	public boolean checkOnAssetPage() {
		checkForErrorMessages(null);
		if(selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Safety Network')]")) {
			return selenium.isElementPresent("//div[@id='mainContent']/h1[contains(text(),'Asset')]");
		}else {
			return false;
		}
	}

	public boolean checkHeader(String serialNumber) {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='mainContent']/h1[contains(text(),'" + serialNumber + "')]");		
	}

}
