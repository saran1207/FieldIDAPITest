package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.pages.ForgotPasswordPage;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoginTest extends FieldIDTestCase {

	@Test
	public void shouldBeAbleToGoToIForgotMyPasswordAndReturnToLoginWithoutResettingPassword() throws Exception {
        ForgotPasswordPage forgotPasswordPage = start().clickForgotMyPassword();
        forgotPasswordPage.clickReturnToLogin().login();
	}
	
	@Test
	public void shouldBeAbleToResetPassword() throws Exception {
        ForgotPasswordPage forgotPasswordPage = startAsCompany("test1").clickForgotMyPassword();
        forgotPasswordPage.enterUserName("n4systems");
        forgotPasswordPage.clickResetPassword().clickReturnToSignIn();

        mailServer.waitForMessages(1);
        MailMessage message = mailServer.getAndClearMessages().get(0);
        assertTrue(message.getSubject().contains("Password Reset"));
    }

	@Test
	public void shouldGetAGoodWarningMessageIfIAttemptToResetPasswordWithoutInputtingAUserName() throws Exception {
        ForgotPasswordPage forgotPasswordPage = startAsCompany("test1").clickForgotMyPassword();
        forgotPasswordPage.clickResetPasswordValidationError();
        assertTrue(forgotPasswordPage.isUsernameRequiredErrorDisplayed());
	}

}
