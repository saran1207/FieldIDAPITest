package com.n4systems.ejb;

import static org.easymock.classextension.EasyMock.*;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.testutils.TestConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.MailMessage.MessageType;

public class MailManagerImplTest {
	private TestConfigContext config;
	
	@Before
	public void setup_config_context() {
		config = TestConfigContext.newContext();
	}

	@After
	public void reset_config_context() {
		TestConfigContext.resetToDefaultContext();
	}
	
	@SuppressWarnings("serial")
	@Test
	public void send_message_loggs_when_delivery_off() throws NoSuchProviderException, MessagingException {
		config.setEntry(ConfigEntry.MAIL_DELIVERY_ON, false);
		
		MailManagerImpl mailMan = new MailManagerImpl(config);
		
		Logger logger = createMock(Logger.class);
		
		final String messageText = "Hello World";
		
		MailMessage msg = new MailMessage(MessageType.HTML, config) {

			@Override
			public String toString() {
				return messageText;
			}
			
		};
		
		logger.info(messageText);
		
		replay(logger);
		
		mailMan.setMailLogger(logger);
		mailMan.sendMessage(msg);
		
		verify(logger);
	}
}
