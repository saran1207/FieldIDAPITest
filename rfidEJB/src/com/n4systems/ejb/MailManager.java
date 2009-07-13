package com.n4systems.ejb;

import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.n4systems.util.mail.MailMessage;

@Local
public interface MailManager {
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException;
	public void sendMessage(MailMessage mailMessage, boolean setDefaults) throws NoSuchProviderException, MessagingException;
}
