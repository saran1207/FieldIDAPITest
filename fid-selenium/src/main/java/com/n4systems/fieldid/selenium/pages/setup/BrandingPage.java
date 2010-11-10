package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class BrandingPage extends FieldIDPage {

    public BrandingPage(Selenium selenium) {
        super(selenium);
    }

    public String getWebSiteAddress() {
        return selenium.getValue("//input[@id='brandingUpdate_webSite']");
    }

}
