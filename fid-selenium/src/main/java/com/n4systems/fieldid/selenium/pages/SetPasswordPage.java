package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class SetPasswordPage extends FieldIDPage {

    public SetPasswordPage(Selenium selenium) {
        super(selenium);
    }

    public void enterAndConfirmPassword(String password) {
        enterPassword(password);
        enterConfirmPassword(password);
    }

    public void enterPassword(String password) {
        selenium.type("//input[@name='newPassword']", password);
    }

    public void enterConfirmPassword(String password) {
        selenium.type("//input[@name='confirmPassword']", password);
    }

    public LoginPage submitConfirmPassword() {
        selenium.click("//input[@type='submit']");
        return new LoginPage(selenium);
    }


}
