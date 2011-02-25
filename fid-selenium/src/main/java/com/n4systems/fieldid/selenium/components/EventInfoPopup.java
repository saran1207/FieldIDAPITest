package com.n4systems.fieldid.selenium.components;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.thoughtworks.selenium.Selenium;

public class EventInfoPopup extends WebEntity {

    public EventInfoPopup(Selenium selenium) {
        super(selenium);
        waitForAjax();
        waitForElementToBePresent("//iframe");
        selenium.selectFrame("//iframe");
        waitForElementToBePresent("//div[@class='eventForm']");
        selenium.selectFrame("relative=up");
    }

    public String getTextValueForCriteria(String criteriaName) {
        selenium.selectFrame("//iframe");
        String textValue = selenium.getText("//div[@class='eventForm']//label[contains(@class,'eventFormLabel') and .='"+criteriaName+"']/../div[@class='textCriteriaDisplayContainer']");
        selenium.selectFrame("relative=up");
        return textValue;
    }

}
