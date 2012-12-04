package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class AdminLoginPage extends FieldIDPage {

    public AdminLoginPage(Selenium selenium) {
        super(selenium);
        assertTrue("Expected to be on admin login page",  selenium.isElementPresent("//div[@id='header']//h1[.='Field ID Admin Console']"));
        waitForElementToBePresent("//form[@id='loginForm']//input[@id='loginForm_username']", DEFAULT_TIMEOUT);
    }

    public AdminOrgsListPage login() {
        return login("n4systems", "f0rM@t!!");
    }

    public AdminOrgsListPage login(String userName, String password) {
        selenium.type("//form[@id='loginForm']//input[@id='loginForm_username']", userName);
        selenium.type("//form[@id='loginForm']//input[@id='loginForm_password']", password);
        selenium.click("//form[@id='loginForm']/input[@type='submit']");
        return new AdminOrgsListPage(selenium);
    }

}
