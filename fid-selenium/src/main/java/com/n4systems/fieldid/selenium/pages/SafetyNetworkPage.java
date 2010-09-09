package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.safetynetwork.FindConnectionResultsPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkPage extends FieldIDPage {

    public SafetyNetworkPage(Selenium selenium) {
        super(selenium);
    }

    public FindConnectionResultsPage findConnections(String searchText) {
        selenium.type("//input[@id='companySearchBox']", searchText);
        selenium.click("//input[@id='searchForCompanyButton']");
        return new FindConnectionResultsPage(selenium);
    }

}
