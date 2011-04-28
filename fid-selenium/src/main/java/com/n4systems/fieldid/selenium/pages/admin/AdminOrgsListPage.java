package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminOrgsListPage extends FieldIDPage {

    public AdminOrgsListPage(Selenium selenium) {
        super(selenium, false);
        waitForPageToLoad("60000");
    }

    public AdminOrgPage clickEditOrganization(String orgName) {
        selenium.click("//table//a[contains(.,'"+orgName+"')]/../..//a[.='Edit']");
        return new AdminOrgPage(selenium);
    }
    
    public AdminOrgsListPage clickLastPage() {
    	selenium.click("//a[.='Last']");
        waitForPageToLoad("60000");    
        return this;
    }

}
