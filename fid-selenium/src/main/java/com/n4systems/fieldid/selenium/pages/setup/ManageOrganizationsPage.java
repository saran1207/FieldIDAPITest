package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageOrganizationsPage extends FieldIDPage {
	
	public ManageOrganizationsPage(Selenium selenium) {
		super(selenium);
		if(!selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Manage Organizations')]")) {
			fail("Could not find the header for 'Manage Organizations'.");
		}
	}

	public void clickAdd() {
		clickNavOption("Add");
	}

    public void clickEditPrimaryOrganization() {
        selenium.click("//h2[contains(text(), 'Primary Organization')]//a[.='Edit']");
        waitForPageToLoad();
    }

    public void enterOrganizationName(String name) {
        selenium.type("//input[@name='displayName']", name);
    }

	public void clickSave() {
		selenium.click("//input[@value='Save']");
		waitForPageToLoad();
	}

	public List<String> getOrganizationNames() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount("//tr[contains(@id,'organization_')]");
		int numRows = n.intValue();
		for(int i = 0; i < numRows; i++) {
			int row = i + 1;	// first row is the column headers so start at + 1
			String cellLocator = "//div[@id='pageContent']/table[@class='list']." + row + ".0";
			String orgName = selenium.getTable(cellLocator);
			result.add(orgName.trim());
		}
		return result;
	}	
	
	public void clickEditOrganization(String name) {
		selenium.click("//table[@class='list']//td[.='" + name + "']//..//a[.='Edit']");
		waitForPageToLoad();
	}
	
	public void clickArchiveOrganization(String name) {
		selenium.click("//table[@class='list']//td[.='" + name + "']//..//a[.='Archive']");
		waitForPageToLoad();
	}
	
	public int getCustomersToBeArchived() {
		return Integer.parseInt(selenium.getText("//label[@for='customersToArchive']"));
	}

	public int getDivisonsToBeArchived() {
		return Integer.parseInt(selenium.getText("//label[@for='divisionsToArchive']"));
	}

	public int getUsersToBeArchived() {
		return Integer.parseInt(selenium.getText("//label[@for='usersToArchive']"));
	}

	public void confirmArchive(boolean confirm) {
		if(confirm) {
			selenium.click("//input[@value='Archive']");
		}else {
			selenium.click("//a[.='Cancel']");
		}
		waitForPageToLoad();
	}

    // Edit Page

    public String getCompanyAddress() {
        return selenium.getValue("//input[@name='addressInfo.streetAddress']");
    }

    public String getCompanyCity() {
        return selenium.getValue("//input[@name='addressInfo.city']");
    }

    public String getCompanyState() {
        return selenium.getValue("//input[@name='addressInfo.state']");
    }

    public String getCompanyCountry() {
        return selenium.getValue("//input[@name='addressInfo.country']");
    }

    public String getCompanyZip() {
        return selenium.getValue("//input[@name='addressInfo.zip']");
    }

    public String getCompanyPhoneNumber() {
        return selenium.getValue("//input[@name='addressInfo.phone1']");
    }

}
