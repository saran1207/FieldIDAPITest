package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsSearchPage extends FieldIDPage {

    public AssetsSearchPage(Selenium selenium) {
        super(selenium);
    }

    public void enterSerialNumber(String serialNumber) {
        selenium.type("//input[@id='reportForm_criteria_serialNumber']", serialNumber);
    }

    public AssetsSearchResultsPage clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return new AssetsSearchResultsPage(selenium);
    }

}
