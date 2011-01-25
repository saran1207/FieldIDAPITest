package com.n4systems.fieldid.selenium.util;

import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.pages.SetPasswordPage;
import com.thoughtworks.selenium.Selenium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class SignUpEmailLoginNavigator {

    public SetPasswordPage navigateToSignInPageSpecifiedIn(MailMessage message, Selenium selenium) {
        String url = extractSignInPageUrlFrom(message);
        selenium.open(url);
        return new SetPasswordPage(selenium);
    }

    public String extractSignInPageUrlFrom(MailMessage message) {
        String body = message.getBody();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(body));
            String headerBeforeLoginUrl = "Login Here: ";
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(headerBeforeLoginUrl)) {
                    return line.substring(headerBeforeLoginUrl.length());
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
