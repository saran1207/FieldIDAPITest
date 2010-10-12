package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public abstract class EntitySearchPage<T extends WebPage> extends FieldIDPage {

    private Class<T> clazz;

    public EntitySearchPage(Selenium selenium, Class<T> clazz) {
        super(selenium);
        this.clazz = clazz;
    }

    public void enterSerialNumber(String serialNumber) {
        selenium.type("//input[@id='reportForm_criteria_serialNumber']", serialNumber);
    }

    public T clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return createResultsPage();
    }

    private T createResultsPage() {
        return PageFactory.createPage(clazz, selenium);
    }

}
