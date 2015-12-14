package com.n4systems.mail;

import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;

import javax.mail.*;
import java.util.Properties;

public class SMTPMailManager implements MailManager {
	private Logger logger = Logger.getLogger(SMTPMailManager.class);

	private final ConfigurationProvider config;

	public SMTPMailManager(ConfigurationProvider config) {
		this.config = config;
	}

	public SMTPMailManager() {
		this(ConfigService.getInstance());
	}

	private Authenticator getAuthenticator() {
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				String user = config.getString(ConfigEntry.MAIL_AUTH_USER);
				String pass = config.getString(ConfigEntry.MAIL_AUTH_PASS);
				return new PasswordAuthentication(user, pass);
			}
		};
		return auth;
	}

	private Properties getMailProperties() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", config.getString(ConfigEntry.MAIL_HOST));
		props.setProperty("mail.smtp.port", config.getString(ConfigEntry.MAIL_PORT));
		props.setProperty("mail.host", config.getString(ConfigEntry.MAIL_HOST));
		return props;
	}

	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		logger.debug("Sending message: " + mailMessage);
		mailMessage(mailMessage);
		logger.debug("Message sent");
	}

	protected Message mailMessage(MailMessage mailMessage) throws MessagingException {
		Session mailSession = Session.getInstance(getMailProperties(), getAuthenticator());
		Message message = mailMessage.compileMessage(mailSession);

		Transport.send(message);
		return message;
	}

}
