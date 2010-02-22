package com.n4systems.notifiers;

import org.apache.log4j.Logger;

import com.n4systems.ejb.MailManager;
import com.n4systems.ejb.MailManagerImpl;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;

public class EmailNotifier implements Notifier {
	private Logger logger = Logger.getLogger(EmailNotifier.class);
	private final MailManager mailManager;

	public EmailNotifier(MailManager mailManager) {
		this.mailManager = mailManager;	
	}
	
	public EmailNotifier() {
		this(new MailManagerImpl());
	}

	public boolean notify(Notification notification) {
		try {
			mailManager.sendMessage(createMessage(notification));
			return true;
		} catch (Exception e) {
			logger.error("Failed sending email notification", e);
			return false;
		}
	}

	private MailMessage createMessage(Notification notification) {
		TemplateMailMessage mailMessage = new TemplateMailMessage();
		mailMessage.getToAddresses().add(notification.getTo().getEmailAddress());
		mailMessage.setTemplatePath(notification.notificationName());
		mailMessage.setSubject(notification.subject());
		mailMessage.getTemplateMap().put("notification", notification);
		
		
		return mailMessage;
	}

	
	
}
