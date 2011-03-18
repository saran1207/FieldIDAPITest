package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class WebEntity {
	
	protected Selenium selenium;
    public static final String DEFAULT_TIMEOUT = "60000";
    public static final String JS_TIMEOUT = "1000";
    public static final String AJAX_TIMEOUT = "10000";

	public WebEntity(Selenium selenium) {
		this.selenium = selenium;
	}
	
	protected void confirmNextDialog(boolean confirm) {
		if (confirm) {
			selenium.chooseOkOnNextConfirmation();
		} else {
			selenium.chooseCancelOnNextConfirmation();
		}
	}
	
	protected String getSelectedValueIfPresent(String xpath) {
		if (!selenium.isElementPresent(xpath))
			return null;
		return selenium.getSelectedValue(xpath);
	}
	
	protected String getSelectedLabelIfPresent(String xpath) {
		if (!selenium.isElementPresent(xpath))
			return null;
		return selenium.getSelectedLabel(xpath);
	}
	
	protected String getValueIfPresent(String xpath) {
		if (!selenium.isElementPresent(xpath))
			return null;
		return selenium.getValue(xpath);
	}
	
	protected boolean isOptionPresent(String locator, String option) {
		String options[] = selenium.getSelectOptions(locator);
        for (String availableOption : options) {
            if (availableOption.equals(option)) {
                return true;
            }
        }
		return false;
	}
	
	protected final void waitForAjax() {
		waitForAjax(AJAX_TIMEOUT);
	}

	protected void waitForAjax(String timeout)  {
		selenium.waitForCondition("typeof(selenium.browserbot.getCurrentWindow().Ajax) == 'object' && selenium.browserbot.getCurrentWindow().Ajax.activeRequestCount == 0;", timeout);
	}

    protected void dragAndDropFromTo(String sourceXpath, String destXpath) {
        selenium.mouseDownAt(sourceXpath, "1,1");
        selenium.mouseMoveAt(destXpath, "1,1");
        selenium.mouseOver(destXpath);
        selenium.mouseUpAt(destXpath, "1,1");
    }

	protected void waitForElementToBePresent(String locator)  {
		waitForElementToBePresent(locator, DEFAULT_TIMEOUT);
	}

	protected void waitForElementToBePresent(String locator, String timeout)  {
		selenium.waitForCondition("var value = selenium.isElementPresent( '" + locator.replace("'", "\\'") + "'); value == true", timeout);
	}

}
