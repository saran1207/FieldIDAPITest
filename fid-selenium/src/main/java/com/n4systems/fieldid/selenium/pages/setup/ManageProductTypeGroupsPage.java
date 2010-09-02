package com.n4systems.fieldid.selenium.pages.setup;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageProductTypeGroupsPage extends FieldIDPage {
	
	public static final String GROUP_NAME_FIELD = "//form//input[@name='name']";
	
	public ManageProductTypeGroupsPage(Selenium selenium) {
		super(selenium);
	}
	
	public List<String> getProductTypeGroups() {
		return getColumnFromTableStartingAtRow("//div[@id='pageContent']//table[@class='list']", 1, 3);
	}

	public void deleteProductTypeGroup(String productTypeGroup) {
		clickDeleteGroup(productTypeGroup);
		confirmDeleteGroup();
	}

	public void confirmDeleteGroup() {
		selenium.click("//input[@type='submit' and @value='Delete']");
		waitForPageToLoad();
	}
	
	public void clickDeleteGroup(String productTypeGroup) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position() = 1 and . = '"+productTypeGroup+"']/parent::tr/td[3]/a[.='Delete']");
		waitForPageToLoad();
	}
	
	public void clickAddTab() {
		clickNavOption("Add");
	}
	
	public void enterName(String groupName) {
		selenium.type(GROUP_NAME_FIELD, groupName);
	}
	
	public void clickSave() {
		selenium.click("//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}

	public void clickViewAllTab() {
		clickNavOption("View All");
	}

	public void clickEditGroup(String groupName) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position() = 1 and . = '"+groupName+"']/parent::tr/td[3]/a[.='Edit']");
		waitForPageToLoad();
	}
	
	public int getWarningNumberOfAttachedProductTypes() {
		return Integer.parseInt(selenium.getText("//span[contains(., 'Product Types being detached')]/../label").trim());
	}
	
}
