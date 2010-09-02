package com.n4systems.fieldid.selenium.pages.setup;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageCustomersPage extends FieldIDPage {

	public ManageCustomersPage(Selenium selenium) {
		super(selenium);
	}
	
	public ManageCustomersPage clickViewAllTab() {
		clickNavOption("View All");
		return this;
	}
	
	public ManageCustomersPage clickViewArchivedTab() {
		clickNavOption("View Archived");
		return this;
	}
	
	public void clickUsersTab() {
		clickNavOption("Users");
	}
	
	public ManageCustomersPage clickDivisionsTab() {
		clickNavOption("Divisions");
		return this;
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
	
	public int getNumberOfCustomersOnPage() {
		if (!selenium.isElementPresent("//table[@id='customerTable']")) {
			return 0;
		}
		return selenium.getXpathCount("//table[@id='customerTable']//tr").intValue() - 1;
	}

	public ManageCustomersPage unarchiveCustomerNamed(String name) {
		selenium.click("//table[@id='customerTable']//td[contains(.,'" + name + "')]/parent::tr/td[4]/a[.='Unarchive']");
		waitForPageToLoad();
		return this;
	}

	public ManageCustomersPage clickCustomer(String customerName) {
		selenium.click("//table[@id='customerTable']//td/a[.='" + customerName + "']");
		waitForPageToLoad();
		return this;
	}
	
	public List<String> getDivisionNames() {
		return getColumnFromTableStartingAtRow("//div[@id='pageContent']//table[@class='list']", 1, 2);
	}

	public void enterUserFirstName(String firstName) {
		selenium.type("//form[@id='customersUserCreate']//input[@name='firstName']", firstName);
	}

	public void enterUserLastName(String lastName) {
		selenium.type("//form[@id='customersUserCreate']//input[@name='lastName']", lastName);
	}

	public void enterUserEmailAddress(String email) {
		selenium.type("//form[@id='customersUserCreate']//input[@name='emailAddress']", email);
	}

	public void enterUserUserID(String userId) {
		selenium.type("//form[@id='customersUserCreate']//input[@name='userId']", userId);
	}

	public void enterAndConfirmUserPassword(String password) {
		selenium.type("//form[@id='customersUserCreate']//input[@name='passwordEntry.password']", password);
		selenium.type("//form[@id='customersUserCreate']//input[@name='passwordEntry.passwordVerify']", password);
	}

	public void clickSaveUser() {
		selenium.click("//form[@id='customersUserCreate']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}

	public List<String> getUserIds() {
		return getColumnFromTableStartingAtRow("//div[@id='pageContent']//table[@id='userList']", 1, 3);
	}

	public void clickDeleteUser(String userId) {
		selenium.click("//table[@id='userList']//td[position()=1]//a[.='"+userId+"']/../../td[6]/a[.='Remove']");
		waitForPageToLoad();
	}

	public void clickAddUser() {
		selenium.click("//ul[@class='secondaryNav']//a[.='Add User']");
		waitForPageToLoad();
	}

}
