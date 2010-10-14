package com.n4systems.fieldid.selenium.components;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class LocationPicker extends WebEntity {

	public LocationPicker(Selenium selenium) {
		super(selenium);
	}

	private void waitForLocationPickerLoadingToFinish() {
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return !selenium.isElementPresent("//div[@id='location_locationSelection' and not(contains(@style,'left: -10000px;'))]");
			}
		}).run("Location picker loading image never went away");
	}
	
	public void clickChooseLocation() {
		selenium.click("//a[@id='location_showLocationSelection']");
		waitForLocationPickerLoadingToFinish();
	}
	
	public void setFreeFormLocation(String location) {
		selenium.type("//input[@id='location_freeformInput']", location);
	}
	
	public void clickSetLocation() {
		selenium.click("//input[@id='location_locationSelection_select']");
	}
}
