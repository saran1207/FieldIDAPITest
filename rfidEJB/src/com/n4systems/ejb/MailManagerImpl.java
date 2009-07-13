package com.n4systems.ejb;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.log4j.Logger;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;

@Interceptors({TimingInterceptor.class})
@Stateless
public class MailManagerImpl implements MailManager {
	private static Logger logger = Logger.getLogger(MailManagerImpl.class);

	private Authenticator getAuthenticator() {
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				String user = ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_AUTH_USER);
				String pass = ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_AUTH_PASS);
				return new PasswordAuthentication(user, pass);
			}
		};
		
		return auth;
	}
	
	private Properties getMailProperties() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_HOST));
		props.setProperty("mail.host", ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_HOST));
		
		return props;
	}
	
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		sendMessage(mailMessage, true);
	}
	
	public void sendMessage(MailMessage mailMessage, boolean setDefaults) throws NoSuchProviderException, MessagingException {
		
		if(setDefaults) {
			mailMessage.setFromAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_FROM_ADDR));
			mailMessage.setSubjectPrefix(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_SUBJECT_PREFIX));
			mailMessage.setReplyTo(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_REPLY_TO));
			
			if(mailMessage.getContentType().isHtml()) {
				mailMessage.setBodyHeader(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_BODY_HTML_HEADER));
				mailMessage.setBodyFooter(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_BODY_HTML_FOOTER));
			} else {
				mailMessage.setBodyHeader(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_BODY_PLAIN_HEADER));
				mailMessage.setBodyFooter(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_BODY_PLAIN_FOOTER));			
			}
			
			StringTokenizer st = new StringTokenizer(ConfigContext.getCurrentContext().getString(ConfigEntry.MAIL_ATTACHMENT_LIST), ",");
			while(st.hasMoreTokens()) {
				try {
					mailMessage.addAttachment(st.nextToken());
				} catch(Exception e) {
					logger.warn("Failed attaching default attachment", e);
				}
			}
		}
		
		logger.debug("Sending message: " + mailMessage);
		Session mailSession = Session.getInstance(getMailProperties(), getAuthenticator());
		Message message = mailMessage.compileMessage(mailSession);
		Transport.send(message);
		logger.debug("Message sent");
	}
	
}
