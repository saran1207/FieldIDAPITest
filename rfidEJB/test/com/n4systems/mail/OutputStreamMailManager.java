package com.n4systems.mail;

import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.n4systems.ejb.MailManager;
import com.n4systems.util.mail.MailMessage;

public class OutputStreamMailManager implements MailManager {

	
	
	
	private final OutputStream outputStream;

	public OutputStreamMailManager(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
		
	}

	@Override
	public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
		try {
			outputStream.write(mailMessage.toString().getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
