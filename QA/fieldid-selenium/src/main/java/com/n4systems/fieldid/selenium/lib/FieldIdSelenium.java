package com.n4systems.fieldid.selenium.lib;

import com.thoughtworks.selenium.Selenium;

public interface FieldIdSelenium extends Selenium {

	
	public void waitForAjax(String timeout);

	
	public void waitForElementToBePresent(String locator, String timeout);

	
	public void waitForAjax();
	
	public void waitForElementToBePresent(String locator);

	public void waitForPageToLoad();
}
