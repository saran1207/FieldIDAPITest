package com.n4systems.fieldid.service.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;

@Transactional(readOnly = true)
public class MailService extends FieldIdPersistenceService {

	@Autowired
	private ConfigService configService;
	
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
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
