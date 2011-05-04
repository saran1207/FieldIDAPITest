package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminOrgsListPage extends FieldIDPage {

    public AdminOrgsListPage(Selenium selenium) {
        super(selenium, false);
        waitForPageToLoad("60000");
    }

    public AdminOrgPage clickEditOrganization(String orgName) {
        selenium.click("//table//a[.='"+orgName+"']");
        return new AdminOrgPage(selenium);
    }
    
    public AdminOrgsListPage clickLastPage() {
    	selenium.click("//a[.='Last']");
        waitForPageToLoad("60000");    
        return this;
    }
    
    public AdminOrgsListPage filterByCompanyName(String name) {
    	selenium.type("//input[@id='nameFilter']", name);
    	selenium.click("//input[@name='search']");
        waitForPageToLoad();    
        return this;
    }
}
