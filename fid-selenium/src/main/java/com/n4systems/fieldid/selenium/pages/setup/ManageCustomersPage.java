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
	
	
	public void clickAdd() {
		clickNavOption("Add");	
	}

	
	public void clickUsersTab() {
		clickNavOption("Users");
	}
	
	public ManageCustomersPage clickDivisionsTab() {
		clickNavOption("Divisions");
		return this;
	}
	
	public void filterByName(String name) {
		selenium.type("//div[contains(@class, 'listFilter')]//form//input[@id='nameFilter']", name);
		selenium.click("//div[contains(@class, 'listFilter')]//form//input[@type='submit']");
		waitForPageToLoad();
	}
	
	public void archiveCustomerNamed(String name, boolean confirm) {
		confirmNextDialog(confirm);
		selenium.click("//table[@id='customerTable']//td/a[.='" + name + "']/../parent::tr/td[6]/a[.='Archive']");
		selenium.getConfirmation();
		if(confirm) {
			waitForPageToLoad();
		}
	}
	
	public void archiveDivision(String name) {
		selenium.click("//table[@class='list']//td/a[.='" + name + "']//..//..//a[.='Archive']");
		waitForPageToLoad();
	}
	
	
	public int getNumberOfCustomersOnPage() {
		return getNumberOfItemsInTableList("customerTable");
	}
	
	public int getNumberOfDivisionsOnPage() {
		return getNumberOfItemsInTableList("divisionList");
	}
	
	public int getNumberOfUsersOnPage() {
		return getNumberOfItemsInTableList("userList");
	}
	
	public int getNumberOfItemsInTableList(String tableId) {
		if (!selenium.isElementPresent("//table[@id='"+ tableId +"']")) {
			return 0;
		}
		return selenium.getXpathCount("//table[@id='"+ tableId +"']//tr").intValue() - 1;
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
	
	public ManageCustomersPage clickViewTab(){
		clickNavOption("View");
		return this;
	}
	
	public ManageCustomersPage clickMerge(){
		selenium.click("//a[.='Merge']");
		waitForPageToLoad();
		return this;
	}
	
	public void mergeWithCustomer(String customerName){
		selenium.click("//input[@id='label_confirm_as_losing_customer']");
		selenium.type("//input[@id='search']", customerName);
		selenium.click("//input[@type='button']");
		waitForAjax();
		selenium.click("//td/button");
		selenium.click("//input[@id='merge']");
		waitForAjax();
        waitForPageToLoad();
	}

	public ManageCustomersPage editCustomer(String customerName) {
		selenium.click("//table[@id='customerTable']//td/a[.='" + customerName + "']//..//..//a[.='Edit']");
		waitForPageToLoad();
		return this;
	}
	
	public ManageCustomersPage clickAddDivision() {
		selenium.click("//a[.='Add Division']");
		waitForPageToLoad();
		return this;
	}

	public List<String> getOrgNames() {
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

	public void clickSave() {
		selenium.click("//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}

	public List<String> getUserIds() {
		return getColumnFromTableStartingAtRow("//table[@id='userList']", 1, 2);
	}

	public void clickDeleteUser(String userId) {
		selenium.click("//table[@id='userList']//td[position()=1]//a[.='"+userId+"']/../../td[6]/a[.='Remove']");
		waitForPageToLoad();
	}

	public void clickAddUser() {
		selenium.click("//a[.='Add User']");
		waitForPageToLoad();
	}

	public void enterCustomerID(String id) {
		selenium.type("//input[@name='customerId']", id);
	}
	
	public void selectOrganizationalUnit(String option) {
		selenium.select("//select[@name='parentOrgId']", option);
	}
	
	public void enterCustomerName(String name) {
		selenium.type("//input[@name='customerName']", name);
	}

	public void enterDivisionId(String divisionID) {
		selenium.type("//input[@name='divisionID']", divisionID);
	}

	public void enterDivisionName(String name) {
		selenium.type("//input[@name='name']", name);		
	}
	
	public void enterNotes(String notes) { 
		selenium.type("//textarea[@name='customerNotes']", notes);
	}
	
}
