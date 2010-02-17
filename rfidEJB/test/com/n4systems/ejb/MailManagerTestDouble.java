package com.n4systems.ejb;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.n4systems.ejb.MailManager;
import com.n4systems.util.mail.MailMessage;

public class MailManagerTestDouble implements MailManager {
	public MailMessage message;

	@Override
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		message = mailMessage;
	}
}