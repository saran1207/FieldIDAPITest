package com.n4systems.notifiers;

import com.n4systems.ejb.MailManager;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;

public class EmailNotifier implements Notifier {

	private final MailManager mailManager;

	public EmailNotifier(MailManager mailManager) {
		this.mailManager = mailManager;
		
	}

	public boolean success(Notification notification) {
		try {
			mailManager.sendMessage(createMessage(notification));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private MailMessage createMessage(Notification notification) {
		TemplateMailMessage mailMessage = new TemplateMailMessage();
		
		mailMessage.setTemplatePath(notification.notificationName());
		mailMessage.setSubject(notification.subject());
		mailMessage.getTemplateMap().put("notification", notification);
		
		
		return mailMessage;
	}

	
	
}
