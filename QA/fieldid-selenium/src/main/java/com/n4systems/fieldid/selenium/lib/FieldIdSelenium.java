package com.n4systems.fieldid.selenium.lib;

import com.thoughtworks.selenium.Selenium;

public interface FieldIdSelenium extends Selenium {

	
	public void waitForAjax(String timeout) throws InterruptedException;

	
}
