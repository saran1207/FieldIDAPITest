package com.n4systems.mail;

import com.n4systems.util.mail.MailMessage;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.io.OutputStream;

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
