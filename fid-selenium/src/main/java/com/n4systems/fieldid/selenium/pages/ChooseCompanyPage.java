package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class ChooseCompanyPage extends FieldIDPage {

    public ChooseCompanyPage(Selenium selenium) {
        super(selenium);
        verifyChooseCompany();
    }

    public void verifyChooseCompany() {
        assertTrue(selenium.isElementPresent("//input[@id='companyId']"));
        assertTrue(selenium.isElementPresent("//input[@id='signInToCompany_label_find_sign_in']"));
    }

    public void enterCompanyId(String companyID) {
        selenium.type("//input[@name='companyID']", companyID);
    }

    public LoginPage clickFindSignInPage() {
        selenium.click("//input[@value='Find Sign In Page']");
        return new LoginPage(selenium);
    }

    public boolean isUnableToDetermineCompanyErrorDisplayed() {
        return selenium.isElementPresent("//span[@class='errorMessage' and contains(text(), 'can not determine what Company')]");
    }

}
