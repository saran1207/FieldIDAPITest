package com.n4systems.ejb;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.n4systems.util.mail.MailMessage;


public interface MailManager {
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException;
}
