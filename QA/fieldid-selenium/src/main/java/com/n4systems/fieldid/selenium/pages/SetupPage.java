package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
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
	
}
