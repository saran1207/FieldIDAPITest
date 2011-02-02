package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminOrgsListPage extends FieldIDPage {

    public AdminOrgsListPage(Selenium selenium) {
        super(selenium);
    }

    public AdminOrgPage clickEditOrganization(String orgName) {
        selenium.click("//div[@id='content']//table//td[position() = 2 and text() ='"+orgName+"']/..//a[.='Edit']");
        return new AdminOrgPage(selenium);
    }

}
