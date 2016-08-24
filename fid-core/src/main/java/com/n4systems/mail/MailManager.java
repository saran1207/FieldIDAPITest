package com.n4systems.mail;

import com.n4systems.util.mail.MailMessage;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;


public interface MailManager {
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException;
}
