package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class ManageInspectionsPage extends FieldIDPage {

	public ManageInspectionsPage(Selenium selenium) {
		super(selenium);
	}

	public InspectPage clickFirstInspectionLink() {
		selenium.click("//span[@class='inspectionType']/a");
		return new InspectPage(selenium);
	}

	public InspectPage clickStartNewInspection(String inspectionType) {
		selenium.click("//a[contains(., 'Start New Inspection')]");
		selenium.click("//a[contains(., '" + inspectionType + "')]");
		return new InspectPage(selenium);
	}

}
