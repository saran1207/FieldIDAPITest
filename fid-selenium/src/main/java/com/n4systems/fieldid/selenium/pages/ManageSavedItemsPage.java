package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ManageSavedItemsPage extends FieldIDPage {

    public ManageSavedItemsPage(Selenium selenium) {
        super(selenium);
        assertTrue("Should be on manage saved items page", selenium.isElementPresent("//input[@name='displayLastRunSearches']"));
    }

    public List<String> getSavedItemNames() {
        return collectTableValuesUnderCellForCurrentPage(1, 1, "a");
    }

}
