package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
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

    public void clickEditPrimaryOrg() {
        selenium.click("//h2[contains(text(), 'Primary Organization')]//a[.='Edit']");
        waitForPageToLoad();
    }

    public void enterOrganizationName(String name) {
        selenium.type("//input[@id='organizationCreate_displayName']", name);
    }

	public void clickSave() {
		selenium.click("//input[@id='organizationCreate_label_save']");
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
