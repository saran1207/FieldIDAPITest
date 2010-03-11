package com.n4systems.mail;

import static com.n4systems.model.builders.UserBuilder.*;
import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.notifiers.EmailNotifier;
import com.n4systems.notifiers.notifications.UserWelcomeEmail;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.NonDataSourceBackedConfigContext;

public class SendWelcomeMessageTest {
	
	@Before
	public void createConfigContext() {
		NonDataSourceBackedConfigContext config = new NonDataSourceBackedConfigContext();
		ConfigContext.setCurrentContext(config);
	}
	
	@Test
	public void should_correctly_send_the_mail_message_to_the_log() throws Exception {
		
		EmailNotifier emailNotifier = new EmailNotifier(new FileSystemLoggingMailManager());
		
		UserWelcomeEmail notification = new UserWelcomeEmail(aUser().build());
		
		assertTrue(emailNotifier.notify(notification));
	}

	
}
