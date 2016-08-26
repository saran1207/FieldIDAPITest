package com.n4systems.mail;

import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

public class FileSystemLoggingMailManager implements MailManager {
	private static Logger logger = Logger.getLogger("mailLogger");
	
	@Override
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		logger.info(mailMessage.toString());
	}

}
