package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminLoginPage extends FieldIDPage {

    public AdminLoginPage(Selenium selenium) {
        super(selenium);
    }

    public AdminOrgsListPage login() {
        return login("n4systems", "TheD1zb0t!");
    }

    public AdminOrgsListPage login(String userName, String password) {
        selenium.type("//form[@id='loginForm']//input[@id='loginForm_username']", userName);
        selenium.type("//form[@id='loginForm']//input[@id='loginForm_password']", password);
        selenium.click("//form[@id='loginForm']/input[@type='submit']");
        return new AdminOrgsListPage(selenium);
    }

}
