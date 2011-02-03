package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class ForgotPasswordPage extends FieldIDPage {

    public ForgotPasswordPage(Selenium selenium) {
        super(selenium);
    }

    public LoginPage clickReturnToLogin() {
        selenium.click("//a[.='Return to Sign In']");
        return new LoginPage(selenium);
    }

    public void enterUserName(String userName) {
        selenium.type("//input[@id='userName']", userName);
    }

    public PasswordResetConfirmPage clickResetPassword() {
        selenium.click("//input[@value='Reset Password']");
        return new PasswordResetConfirmPage(selenium);
    }

    public void clickResetPasswordValidationError() {
        selenium.click("//input[@value='Reset Password']");
        waitForPageToLoad();
    }

    public boolean isUsernameRequiredErrorDisplayed() {
        List<String> errors = getFormErrorMessages();
        return errors.contains("User Name is required.");
    }

}
