package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.List;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageEventBooksPage extends FieldIDPage {

	private static final String FIRST_LIST_ITEM = "//table[@class='list']//tr[2]//td[1]//a";

	public ManageEventBooksPage(Selenium selenium) {
		super(selenium);
		if(!checkOnManageEventBooksPage()){
			fail("Expected to be on Manage Event Books page!");
		}
	}
	
	public boolean checkOnManageEventBooksPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Event Books')]");
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

    public String getStatusForBookNamed(String bookName) {
        return selenium.getText("//table[@class='list']//td[1]//a[.='"+bookName+"']/../../td[4]/span");
    }

    public void clickCloseForBookNamed(String bookName) {
        selenium.click("//table[@class='list']//td[1]//a[.='"+bookName+"']/../../td[5]//a[contains(text(),'Close')]");
        waitForAjax();
    }

    public void clickOpenForBookNamed(String bookName) {
        selenium.click("//table[@class='list']//td[1]//a[.='"+bookName+"']/../../td[5]//a[contains(text(),'Open')]");
        waitForAjax();
    }

    public void clickDeleteForBookNamed(String bookName) {
        selenium.click("//table[@class='list']//td[1]//a[.='"+bookName+"']/../../td[5]//a[contains(text(),'Delete')]");
        waitForAjax();
    }
    
    public void clickArchiveForBookNamed(String bookName) {
        selenium.click("//table[@class='list']//td[1]//a[.='"+bookName+"']/../../td[5]//a[contains(text(),'Archive')]");
        waitForPageToLoad();
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

	public void verifyEventBookSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Book saved.", actionMessages.get(0).trim());		
	}

	public void clickSave() {
		selenium.click("//input[@name='hbutton.save']");
		waitForPageToLoad();
	}
	
	
	public void setName(String name) {
		selenium.type("//input[@name='name']", name);
	}
	
	public void setOwner(Owner owner) {
		OrgPicker orgPicker = new OrgPicker(selenium);
		orgPicker.clickChooseOwner();
		orgPicker.setOwner(owner);
		orgPicker.clickSelectOwner();		
	}
	
	public void setStatus(boolean status) {
		if (status) {
			selenium.check("//input[@name='open']");
		}else{
			selenium.uncheck("//input[@name='open']");
		}
	}

	public void clickDelete(String book) {
		selenium.click("//table[@class='list']//tr//td/a[text()='" + book + "']/../../td[5]/a[text()='Delete']");
		waitForAjax();
	}
	
	public void verifyEventBookDeleted() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Book deleted.", actionMessages.get(0).trim());		
	}
	
	public boolean eventBookExists(String book) {
		return selenium.isElementPresent("//table[@class='list']//tr//td/a[text()='" + book + "']");
	}

}
