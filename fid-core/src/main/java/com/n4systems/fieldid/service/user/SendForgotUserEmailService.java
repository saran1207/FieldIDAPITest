package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.net.URI;


public class SendForgotUserEmailService {

    private static final Logger logger = Logger.getLogger(SendForgotUserEmailService.class);
    @Autowired
    private MailService mailService;

    public void sendForgotUserEmail(String emailAddress,User user, URI uri) {

        StringBuilder sb = new StringBuilder();
        sb.append("Dear ");
        sb.append(user.getFullName());
        sb.append(",<br/><br/>");
        sb.append("You're current user name is ");
        sb.append(user.getUserID());
        sb.append(".<br/><br/>");
        sb.append("You can now login securely to your account at:<br/><br/>" + "<a href=\"" + uri.toString() + "\">" + uri.toString() + "</a>");

        String messageBody = sb.toString();

        MailMessage message = new MailMessage("Password Reset ", messageBody);
        message.getToAddresses().add(emailAddress);

        try {
            mailService.sendMessage(message);
        } catch (MessagingException e) {
            logger.error("Could not notify user", e);
        }

    }

}
