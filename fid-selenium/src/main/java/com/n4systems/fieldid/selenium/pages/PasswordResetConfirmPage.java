package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class PasswordResetConfirmPage extends FieldIDPage {

    public PasswordResetConfirmPage(Selenium selenium) {
        super(selenium);
        verifyResetConfirmPage();
    }

    private void verifyResetConfirmPage() {
        assertTrue(selenium.isElementPresent("//h1[.='Password Reset Email Sent']"));
    }

    public LoginPage clickReturnToSignIn() {
        selenium.click("//input[@value='Return to Login']");
        return new LoginPage(selenium);
    }

}
