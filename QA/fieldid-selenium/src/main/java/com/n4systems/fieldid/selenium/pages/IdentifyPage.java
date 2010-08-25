package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class IdentifyPage extends FieldIDPage {

	public IdentifyPage(Selenium selenium) {
		super(selenium);
	}
	
	public void selectProductType(String productType) {
		selenium.select("//select[@id='productType']", productType);
		selenium.fireEvent("//select[@id='productType']", "change");
		waitForAjax();
	}
	
	public void selectAttributeValue(String attribute, String value) {
		String selectXpath = "//div[@id='infoOptions']//div[@infofieldname='"+attribute+"']//select"; 
		selenium.select(selectXpath, value);
		selenium.fireEvent(selectXpath, "change");
		waitForAjax();
	}
	
	public String getAttributeValue(String attribute) {
		return selenium.getValue("//div[@id='infoOptions']//div[@infofieldname='"+attribute+"']//input[@type='text']");
	}

	public String getAttributeSelectValue(String attribute) {
		return selenium.getSelectedLabel("//div[@id='infoOptions']//div[@infofieldname='"+attribute+"']//select");
	}

}
