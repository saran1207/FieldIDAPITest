package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.*;

import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

public class WebPage extends WebEntity {

    private static final String ERROR_MESSAGE_COUNT_XPATH = "//*[@class='errorMessage' and not(contains(@style,'display: none'))]";
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

    public void assertSystemLogoIsUsed() {
        assertTrue("This page should display the system logo", selenium.isElementPresent("systemLogo"));
        assertFalse("This page should not display a branded company logo", selenium.isElementPresent("companyLogo"));
    }

    public List<String> getFormErrorMessages() {	// gets class="errorMessage"
        List<String> result = new ArrayList<String>();

        int maxIndex = selenium.getXpathCount(ERROR_MESSAGE_COUNT_XPATH).intValue();
        for(int i = 1; i <= maxIndex; i++) {
            String iterableErrorMessageLocator = "//ul/li["+i+"]/*[@class='errorMessage' and not(contains(@style,'display: none'))]";
            String s = selenium.getText(iterableErrorMessageLocator);
            result.add(s);
        }
        return result;
    }

    public List<String> getActionMessages() {
        List<String> result = new ArrayList<String>();

        int maxIndex = selenium.getXpathCount("//span[@class='actionMessage']").intValue();
        for(int i = 1; i <= maxIndex; i++) {
            String iterableActionMessageLocator = "//ul/li["+i+"]/*[@class='actionMessage']";
            String s = selenium.getText(iterableActionMessageLocator);
            result.add(s);
        }

        return result;
    }

	protected int countNonFormErrorMessages() {
		return selenium.getXpathCount(ERROR_MESSAGE_COUNT_XPATH).intValue();
	}

	protected void checkForErrorMessages(String png) {
		List<String> errors = getFormErrorMessages();
		int otherErrors = countNonFormErrorMessages();
		if(isOopsPage()) {
			fail("Got the Oops page. Check the fieldid.log.");
		} else if(errors.size() > 0) {
			fail("There were errors on the page: " + errors);
		} else if(otherErrors > 0) {
			fail("There were non-form errors on the page");
		}
	}

}
