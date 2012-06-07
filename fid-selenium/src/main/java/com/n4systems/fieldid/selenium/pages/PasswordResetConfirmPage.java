package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class PasswordResetConfirmPage extends FieldIDPage {

    public PasswordResetConfirmPage(Selenium selenium) {
        super(selenium);
        verifyResetConfirmPage();
    }

    private void verifyResetConfirmPage() {
        assertTrue(selenium.isElementPresent("//div[@id='message']//span[.='Thank you. An email has been sent to you with instructions on resetting your password']"));
    }

    public LoginPage clickReturnToSignIn() {
        selenium.click("//a[.='Return to Login']");
        return new LoginPage(selenium);
    }

}
