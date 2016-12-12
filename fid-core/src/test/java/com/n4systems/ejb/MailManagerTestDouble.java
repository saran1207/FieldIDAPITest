package com.n4systems.ejb;

import com.n4systems.mail.MailManager;
import com.n4systems.util.mail.MailMessage;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

public class MailManagerTestDouble implements MailManager {
	public MailMessage message;

	@Override
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		message = mailMessage;
	}
}