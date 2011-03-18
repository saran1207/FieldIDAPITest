package com.n4systems.fieldid.selenium.pages.setup.eventtypes;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventTypeAssetTypeAssociationsPage extends FieldIDPage {

	public EventTypeAssetTypeAssociationsPage(Selenium selenium, boolean waitForLoad) {
		super(selenium, waitForLoad);
	}
	
	public void selectAllAssetTypes(){
		selenium.click("//tr[1]/th[1]/a[1]");
		waitForAjax();
	}
	
	public EventTypeAssetTypeAssociationsPage clickSave(){
		selenium.click("//input[@name='hbutton.save']");
		throwExceptionOnFormError(true);
		return new EventTypeAssetTypeAssociationsPage(selenium, false);
	}

}
