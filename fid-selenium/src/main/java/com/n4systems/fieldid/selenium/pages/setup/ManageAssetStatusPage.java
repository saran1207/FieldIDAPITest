package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.fail;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageAssetStatusPage extends FieldIDPage {

	public ManageAssetStatusPage(Selenium selenium) {
		super(selenium);
		if(checkOnManageAssetStatusPage()) {
			fail("Expected to be on Manage Asset Status page!");
		}
	}

	public boolean checkOnManageAssetStatusPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Asset Statuses ')]");
	}
	
	public void clickViewAllTab() {
		clickNavOption("View All");
	}

	public void clickViewArchivedTab() {
		clickNavOption("View Archived");
	}
	
	public void clickAddTab() {
		clickNavOption("Add");
	}
	
	public void clickSave(){
		selenium.click("//input[@name='hbutton.save']");
		waitForPageToLoad();
	}

	public void clickCancel(){
		selenium.click("//a[text()='Cancel']");
		waitForPageToLoad();
	}

	public void enterAssetStatusName(String value) {
		selenium.type("//input[@name='name']", value);
	}
	
	public List<String> getAssetStatuses() {
		return getColumnFromTableStartingAtRow("//div[@id='pageContent']//table[@class='list']", 1, 2);
	}
	
	public void archive(String name) {
		selenium.click("//table[@class='list']//td[text()='" + name + "']/../td//a[text()='Archive']");
		waitForPageToLoad();
	}
	
	public void unarchive(String name) {
		selenium.click("//table[@class='list']//td[text()='" + name + "']/../td//a[text()='Unarchive']");
		waitForPageToLoad();
	}
}
