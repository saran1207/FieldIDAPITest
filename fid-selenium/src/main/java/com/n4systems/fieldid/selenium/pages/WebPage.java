package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

public class WebPage extends WebEntity {
	
	public static final String DEFAULT_TIMEOUT = "20000";

	public WebPage(Selenium selenium, boolean waitForLoad) {
		super(selenium);
		if (waitForLoad) {
			waitForPageToLoad();
		}
	}

	public WebPage(Selenium selenium) {
		this(selenium, true);
	}
	
	protected boolean isOopsPage() {
		return selenium.isTextPresent("Oops - Page does not exist");
	}
	
	protected void waitForPageToLoad() {
		waitForPageToLoad(DEFAULT_TIMEOUT);
	}
	
	protected void waitForPageToLoad(String timeout) {
		selenium.waitForPageToLoad(timeout);
		if(isOopsPage()) {
			fail("Got an Oops page. Check Field ID logs.");
		}
	}

	protected String getCurrentTab() {
		return selenium.getText("//ul[@class='options ']/li[@class = ' selected']").trim();
	}
	
	public String getAlert() {
		return selenium.getAlert();
	}

    public void goBack() {
        selenium.goBack();
        waitForPageToLoad();
    }
	
}
