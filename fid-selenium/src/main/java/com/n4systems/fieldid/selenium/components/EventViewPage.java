package com.n4systems.fieldid.selenium.components;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.fail;

public class EventViewPage extends FieldIDPage {

    public EventViewPage(Selenium selenium) {
        super(selenium);
        if(!selenium.isElementPresent("//div[@class='resultContainer']")) {
            fail("Expected to be on event view page");
        }
    }

    public String getTextValueForCriteria(String criteriaName) {
        return selenium.getText("//div[@class='eventForm']//label[contains(@class,'eventFormLabel') and .='"+criteriaName+"']/../div[@class='textCriteriaDisplayContainer']");
    }

}
