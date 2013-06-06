package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.notifiers.notifications.UserPasswordWelcomeEmail;
import com.n4systems.notifiers.notifications.UserWelcomeEmail;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

public class SendWelcomeEmailService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(SendWelcomeEmailService.class);

    private static final String LOGIN_ACTION = "login";
    private static final String FORGOT_PASSWORD_ACTION = "forgotPassword";
    private static final String FIRST_TIME_LOGIN_ACTION = "firstTimeLogin";

    @Autowired
    private MailService mailService;

    public void sendUserWelcomeEmail(boolean sendPasswordWelcome, User user, String personalMessage, ActionURLBuilder urlBuilder) {
        TemplateMailMessage message;

        if(sendPasswordWelcome)
            message = createUserPasswordWelcomeEmail(user, personalMessage, urlBuilder);
        else
            message = createUserWelcomeEmail(user, personalMessage, urlBuilder);

        try {
            mailService.sendMessage(message);
        } catch (MessagingException e) {
            logger.error("Could not notify user", e);
        }
    }


    private TemplateMailMessage createUserPasswordWelcomeEmail(User user, String personalMessage, ActionURLBuilder urlBuilder) {
        UserPasswordWelcomeEmail notification = new UserPasswordWelcomeEmail(user, urlBuilder.setAction(LOGIN_ACTION).build(), getForgetPasswordUrl(user, urlBuilder));
        notification.setPersonalMessage(personalMessage);
        return createMailMessage(notification, user);
    }

    private TemplateMailMessage createUserWelcomeEmail(User user, String personalMessage, ActionURLBuilder urlBuilder) {
        UserWelcomeEmail notification = new UserWelcomeEmail(user, urlBuilder.setAction(LOGIN_ACTION).build(), urlBuilder.setAction(FORGOT_PASSWORD_ACTION).build());
        notification.setPersonalMessage(personalMessage);
        return createMailMessage(notification, user);
    }

    private TemplateMailMessage createMailMessage(Notification notification, User user) {

        TemplateMailMessage mailMessage = new TemplateMailMessage(notification.subject(), notification.notificationName());
        mailMessage.getToAddresses().add(user.getEmailAddress());
        mailMessage.getTemplateMap().put("notification", notification);

        return mailMessage;
    }

    private String getForgetPasswordUrl(User user, ActionURLBuilder urlBuilder) {
        return urlBuilder.setAction(FIRST_TIME_LOGIN_ACTION)
                .addParameter("u", user.getUserID())
                .addParameter("k", user.getResetPasswordKey())
                .build();
    }

}
