package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

public class WebEntity {
	
	protected Selenium selenium;
	
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
		for(int i = 0; i < options.length; i++) {
			if(options[i].equals(option)) {
				return true;
			}
		}
		return false;
	}
	
	protected void waitForAjax() {
		waitForAjax(MiscDriver.AJAX_TIMEOUT);
	}

	protected void waitForAjax(String timeout)  {
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().Ajax.activeRequestCount == 0;", timeout);
	}

}
