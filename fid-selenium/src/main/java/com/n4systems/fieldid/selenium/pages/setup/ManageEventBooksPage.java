package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.List;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.EventBook;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageEventBooksPage extends FieldIDPage {

	private static final String FIRST_LIST_ITEM = "//table[@class='list']//tr[2]//td[1]//a";

	public ManageEventBooksPage(Selenium selenium) {
		super(selenium);
		if(!checkOnManageInspectionBooksPage()){
			fail("Expected to be on Manage Event Books page!");
		}
	}
	
	public boolean checkOnManageInspectionBooksPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Manage Event Books')]");
	}
	
	public void clickViewAllTab() {
		clickNavOption("View All");
	}
	
	public void clickAddTab() {
		clickNavOption("Add");
	}

	public String getFirstListItemName() {
		return selenium.getText(FIRST_LIST_ITEM);
	}

	public String getFirstListItemStatus() {
		return selenium.getText("//table[@class='list']//tr[2]//td[4]/span");
	}

	public void clickFirstListItemClose() {
		selenium.click("//table[@class='list']//tr[2]//td[5]//a[contains(text(),'Close')]");
		waitForAjax();
	}

	public void clickFirstListItemOpen() {
		selenium.click("//table[@class='list']//tr[2]//td[5]//a[contains(text(),'Open')]");
		waitForAjax();
	}

	public String clickFirstListItem() {
		String eventName = getFirstListItemName();
		selenium.click(FIRST_LIST_ITEM);
		waitForPageToLoad();
		return eventName;
	}

	public void verifyInspectionBookSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Book saved.", actionMessages.get(0).trim());		
	}

	public void clickSave() {
		selenium.click("//input[@name='hbutton.save']");
		waitForPageToLoad();
	}

	public void setInspectionBookFormFields(EventBook book) {
		if(book.name != null) {
			selenium.type("//input[@name='name']", book.name);
		}
		if(book.owner != null) {
			OrgPicker orgPicker = new OrgPicker(selenium);
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(book.owner);
			orgPicker.clickSelectOwner();
		}
		if(!book.status) {
			selenium.check("//input[@name='open']");
		}
	}

	public void clickDelete(String book) {
		selenium.click("//table[@class='list']//tr//td/a[text()='" + book + "']/../../td[5]/a[text()='Delete']");
		waitForAjax();
	}
	
	public void verifyInspectionBookDeleted() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Book deleted.", actionMessages.get(0).trim());		
	}
	
	public boolean listItemExists(String book) {
		return selenium.isElementPresent("//table[@class='list']//tr//td/a[text()='" + book + "']");
	}
	
}
