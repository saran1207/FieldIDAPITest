package com.n4systems.ejb;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.log4j.Logger;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;

public class MailManagerImpl implements MailManager {
	private Logger logger = Logger.getLogger(MailManagerImpl.class);
	private Logger mailLog = Logger.getLogger("mailLog");

	private final ConfigContext config;
	
	public MailManagerImpl(ConfigContext config) {
		this.config = config;
	}
	
	public MailManagerImpl() {
		this(ConfigContext.getCurrentContext());
	}
	
	protected void setMailLogger(Logger mailLog) {
		this.mailLog = mailLog;
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
		props.setProperty("mail.host", config.getString(ConfigEntry.MAIL_HOST));
		
		return props;
	}
	
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		logger.debug("Sending message: " + mailMessage);
		
		if (config.getBoolean(ConfigEntry.MAIL_DELIVERY_ON)) {
			mailMessage(mailMessage);
		} else {
			logMessage(mailMessage);
		}
		
		logger.debug("Message sent");
	}

	private void logMessage(MailMessage mailMessage) {
		mailLog.info(mailMessage.toString());
	}

	protected Message mailMessage(MailMessage mailMessage) throws MessagingException {
		Session mailSession = Session.getInstance(getMailProperties(), getAuthenticator());
		Message message = mailMessage.compileMessage(mailSession);
		
		
		Transport.send(message);
		return message;
	}

}
