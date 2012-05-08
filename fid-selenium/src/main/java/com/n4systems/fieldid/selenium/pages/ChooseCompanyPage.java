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
        assertTrue(selenium.isElementPresent("//input[@value='Login']"));
    }

    public void enterCompanyId(String companyID) {
        selenium.type("//input[@name='companyID']", companyID);
    }
    
    public void enterUserEmail(String email){
    	selenium.type("//input[@id='email']", email);
    }
    
    public TenantListPage clickFindByEmail(){
    	selenium.click("//input[@id='emailForm_label_find_my_site_address']");
    	return new TenantListPage(selenium);
    }

    public LoginPage clickFindSignInPage() {
        selenium.click("//input[@value='Find Sign In Page']");
        return new LoginPage(selenium);
    }

    public boolean isUnableToDetermineCompanyErrorDisplayed() {
        return selenium.isElementPresent("//span[@class='errorMessage' and contains(text(), 'can not determine what Company')]");
    }

    public boolean isUnableToDetermineCompanyByEmailErrorDisplayed() {
    	  return selenium.isElementPresent("//span[@class='errorMessage' and contains(text(), 'find a site address linked to that email')]");
    }
}
