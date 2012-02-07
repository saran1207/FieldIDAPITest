package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class SavedItemsDropDown extends WebEntity {

    public SavedItemsDropDown(final Selenium selenium) {
        super(selenium);
        new ConditionWaiter(new Predicate() {
            @Override
            public boolean evaluate() {
                return selenium.isVisible("//div[@id='mySavedItemsBox']");
            }
        }).run("Saved items dropdown should become visible!");
    }

    public ManageSavedItemsPage clickManageSavedItems() {
        selenium.click("//div[@id='mySavedItemsBox']//a[.='Manage Saved Items']");
        return new ManageSavedItemsPage(selenium);
    }

}
