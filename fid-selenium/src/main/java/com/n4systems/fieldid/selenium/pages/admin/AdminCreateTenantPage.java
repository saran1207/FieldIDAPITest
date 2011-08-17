package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminCreateTenantPage extends WicketFieldIDPage {

    public AdminCreateTenantPage(Selenium selenium) {
        super(selenium);
    }

    public void selectPackage(String packageName) {
        selenium.select("//select[@name='primaryOrg.signUpPackage']", packageName);
        waitForWicketAjax();
    }

    public void enterName(String name) {
        selenium.type("//input[@name='tenant.name']", name);
    }

    public void enterNumEmployeeUsers(int numUsers) {
        selenium.type("//input[@name='tenant.settings.userLimits.maxEmployeeUsers']", numUsers+"");
    }

    public void enterUserId(String userId) {
        selenium.type("//input[@name='adminUser.userID']", userId);
    }

    public void enterFirstName(String firstName) {
        selenium.type("//input[@name='adminUser.firstName']", firstName);
    }

    public void enterLastName(String lastName) {
        selenium.type("//input[@name='adminUser.lastName']", lastName);
    }

    public void enterEmailAddress(String emailAddress) {
        selenium.type("//input[@name='adminUser.emailAddress']", emailAddress);
    }

    public void enterPrimaryOrgName(String name) {
        selenium.type("//input[@name='primaryOrg.name']", name);
    }

    public void enterPrimaryNetsuiteUser(String externalUserName) {
        selenium.type("//input[@name='primaryOrg.externalUserName']", externalUserName);
    }

    public void enterPrimaryNetsuitePassword(String externalPassword) {
        selenium.type("//input[@name='primaryOrg.externalPassword']", externalPassword);
    }

    public void enterPrimaryOrgAddress(String address) {
        selenium.type("//input[@name='addressInfoContainer:streetAddress']", address);
    }

    public void enterPrimaryOrgCity(String city) {
        selenium.type("//input[@name='addressInfoContainer:city']", city);
    }

    public void enterPrimaryOrgState(String state) {
        selenium.type("//input[@name='addressInfoContainer:state']", state);
    }

    public void enterPrimaryOrgZip(String zip) {
        selenium.type("//input[@name='addressInfoContainer:zip']", zip);
    }

    public void enterPrimaryOrgCountry(String country) {
        selenium.type("//input[@name='addressInfoContainer:country']", country);
    }

    public void enterPrimaryOrgPhone(String phone) {
        selenium.type("//input[@name='addressInfoContainer:phone']", phone);
    }

    public void enterPrimaryOrgFax(String fax) {
        selenium.type("//input[@name='addressInfoContainer:fax']", fax);
    }

    public AdminOrgsListPage submitForm() {
        selenium.click("//input[@value='Save']");
        return new AdminOrgsListPage(selenium);
    }

}
