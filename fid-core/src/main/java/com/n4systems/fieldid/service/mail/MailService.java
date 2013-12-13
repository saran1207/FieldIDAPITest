package com.n4systems.fieldid.service.mail;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import java.util.Map;
import java.util.Properties;

@Transactional(readOnly = true)
public class MailService extends FieldIdPersistenceService {

	@Autowired
	private ConfigService configService;

	public void sendNotification(String template, String toAddress, String subject, Map<String, Object> templateParams) throws MessagingException {
		TemplateMailMessage mailMessage = new TemplateMailMessage();
		mailMessage.getToAddresses().add(toAddress);
		mailMessage.setTemplatePath(template);
		mailMessage.setSubject(subject);
		mailMessage.getTemplateMap().putAll(templateParams);
		sendMessage(mailMessage);
	}

	public void sendMessage(MailMessage mailMessage) throws MessagingException {
		Session mailSession = Session.getInstance(getMailProperties(), getAuthenticator());
		Message message = mailMessage.compileMessage(mailSession);

		Transport.send(message);
	}
	
	private Authenticator getAuthenticator() {
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				String user = configService.getString(ConfigEntry.MAIL_AUTH_USER);
				String pass = configService.getString(ConfigEntry.MAIL_AUTH_PASS);
				return new PasswordAuthentication(user, pass);
			}
		};
		return auth;
	}

	private Properties getMailProperties() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", configService.getString(ConfigEntry.MAIL_HOST));
		props.setProperty("mail.smtp.port", configService.getString(ConfigEntry.MAIL_PORT));
		props.setProperty("mail.host", configService.getString(ConfigEntry.MAIL_HOST));
		return props;
	}
	
}
