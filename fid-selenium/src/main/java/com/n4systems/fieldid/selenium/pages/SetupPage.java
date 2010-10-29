package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.pages.setup.AutoAttributeWizardPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypeGroupsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageCommentTemplatesPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventBooksPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.pages.setup.MangageEventTypeGroupsPage;
import com.n4systems.fieldid.selenium.pages.setup.SystemSettingsPage;
import com.thoughtworks.selenium.Selenium;

public class SetupPage extends FieldIDPage {

	public SetupPage(Selenium selenium) {
		super(selenium);
		if (!selenium.isElementPresent("xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Setup')]")) {
			fail("Expected to be on setup page!");
		}
	}

	public ManageCustomersPage clickManageCustomers() {
		selenium.click("//a[.='Manage Customers']");
		return new ManageCustomersPage(selenium);
	}
	
	public SystemSettingsPage clickSystemSettings() {
		selenium.click("//a[.='Manage System Settings']");
		return new SystemSettingsPage(selenium);
	}

	public ManageUsersPage clickManageUsers() {
		selenium.click("//a[.='Manage Users']");
		return new ManageUsersPage(selenium);
	}

	public ManageOrganizationsPage clickManageOranizations() {
		selenium.click("//a[.='Manage Organizations']");
		return new ManageOrganizationsPage(selenium);
	}

	public AutoAttributeWizardPage clickAutoAttributeWizard() {
		selenium.click("//a[.='Auto Attribute Wizard']");
		return new AutoAttributeWizardPage(selenium);
	}

	public MangageEventTypeGroupsPage clickManageEventTypeGroups() {
		selenium.click("//a[.='Manage Event Type Groups']");
		return new MangageEventTypeGroupsPage(selenium);
	}
	
	public ManageCommentTemplatesPage clickManageCommentTemplates() {
		selenium.click("//a[.='Manage Comment Templates']");
		return new ManageCommentTemplatesPage(selenium);
	}
	
	public ManageAssetTypeGroupsPage clickManageAssetTypeGroups() {
		selenium.click("//a[.='Manage Asset Type Groups']");
		return new ManageAssetTypeGroupsPage(selenium);
	}
	
	public ManageAssetTypesPage clickManageAssetTypes() {
		selenium.click("//a[.='Manage Asset Types']");
		return new ManageAssetTypesPage(selenium);
	}
	
	public ManageEventTypesPage clickManageEventTypes() {
		selenium.click("//a[.='Manage Event Types']");
		return new ManageEventTypesPage(selenium);
	}

	public ManageEventBooksPage clickManageEventBooks() {
		selenium.click("//a[.='Manage Event Books']");
		return new ManageEventBooksPage(selenium);
	}
	
}
