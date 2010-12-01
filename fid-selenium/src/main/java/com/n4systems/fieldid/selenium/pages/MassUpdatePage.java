package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public abstract class MassUpdatePage<T extends WebPage> extends FieldIDPage {

    private Class<T> searchResultsPageClass;

    public MassUpdatePage(Selenium selenium, Class<T> searchResultsPageClass) {
        super(selenium);
        this.searchResultsPageClass = searchResultsPageClass;
    }

    public T clickSaveButtonAndConfirm() {
        selenium.chooseOkOnNextConfirmation();
        selenium.click("//input[@type='submit' and @value='Update']");
        selenium.getConfirmation();
        return PageFactory.createPage(searchResultsPageClass, selenium); 
    }
    
    public T clickSaveButtonAndConfirmMassDelete() {
        selenium.chooseOkOnNextConfirmation();
        selenium.click("//input[@type='submit' and @value='Update']");
        selenium.getConfirmation();
        waitForPageToLoad();
        selenium.click("//input[@id='label_delete']");
        return PageFactory.createPage(searchResultsPageClass, selenium); 
    }
    
    

}
