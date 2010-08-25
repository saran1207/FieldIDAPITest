package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageCustomersPage extends FieldIDPage {

	public ManageCustomersPage(Selenium selenium) {
		super(selenium);
	}
	
	public void clickViewAll() {
		clickNavOption("View All");
	}
	
	public void clickViewArchived() {
		clickNavOption("View Archived");
	}
	
	public void filterByName(String name) {
		selenium.type("//div[contains(@class, 'listFilter')]//form//input[@id='listFilter']", name);
		selenium.click("//div[contains(@class, 'listFilter')]//form//input[@type='submit']");
		waitForPageToLoad();
	}
	
	public void archiveCustomerNamed(String name, boolean confirm) {
		confirmNextDialog(confirm);
		selenium.click("//table[@id='customerTable']//td/a[.='" + name + "']/../parent::tr/td[4]/a[.='Archive']");
		selenium.getConfirmation();
		if(confirm) {
			waitForPageToLoad();
		}
	}
	
	public int getNumberOfResultsOnPage() {
		if (!selenium.isElementPresent("//table[@id='customerTable']")) {
			return 0;
		}
		return selenium.getXpathCount("//table[@id='customerTable']//tr").intValue() - 1;
	}

	public void unarchiveCustomerNamed(String name) {
		selenium.click("//table[@id='customerTable']//td[contains(.,'" + name + "')]/parent::tr/td[4]/a[.='Unarchive']");
		waitForPageToLoad();
	}

}
