package com.n4systems.notifiers;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import javax.mail.MessagingException;

import org.junit.Test;

import com.n4systems.ejb.MailManagerTestDouble;
import com.n4systems.mail.MailManager;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.test.helpers.FluentHashSet;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;


public class EmailNotifierTest {

	
	private final class TestNotification extends Notification {
		
		public TestNotification() {
			super();
			this.notifiyUser(anEmployee().build());
		}

		@Override
		public String notificationName() {
			return "some_notification_name";
		}

		@Override
		public String subject() {
			return "subject";
		}
		
	}

	@Test
	public void should_send_email_notification_when_you_are_passed() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		
		sut.notify(new TestNotification());
		
		assertNotNull(mailManager.message);
	}
	
	@Test
	public void should_send_a_template_email_with_the_name_from_the_notification() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		sut.notify(new TestNotification());
		
		TemplateMailMessage message = (TemplateMailMessage)mailManager.message;
		
		assertEquals("some_notification_name", message.getTemplatePath());
	}
	
	
	@Test
	public void should_send_email_to_the_user_in_the_notification() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		TestNotification notification = new TestNotification();
		notification.notifiyUser(anEmployee().withEmailAddress("me@me.com").build());
		sut.notify(notification);
		
		TemplateMailMessage message = (TemplateMailMessage)mailManager.message;
		
		assertEquals(new FluentHashSet<String>("me@me.com"), message.getToAddresses());
	}
	
	
	@Test
	public void should_send_a_template_email_with_the_subject_from_the_notification() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		sut.notify(new TestNotification());
		
		TemplateMailMessage message = (TemplateMailMessage)mailManager.message;
		
		assertEquals("subject", message.getSubject());
	}
	

	@Test
	public void should_send_a_template_email_with_the_notification_handed_to_the_template_map() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		TestNotification notification = new TestNotification();
		sut.notify(notification);
		
		TemplateMailMessage message = (TemplateMailMessage)mailManager.message;
		
		assertEquals(notification, message.getTemplateMap().get("notification"));
	}
	
	
	@Test
	public void should_return_true_if_there_were_no_errors_from_the_mail_manager() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		
		assertTrue(sut.notify(new TestNotification()));
		
	}
	
	@Test
	public void should_return_false_if_there_is_an_exception_from_the_mail_manager() throws Exception {
		MailManager mailManager = createMock(MailManager.class);
		mailManager.sendMessage((MailMessage)anyObject());
		expectLastCall().andThrow(new MessagingException());
		replay(mailManager);
		
		EmailNotifier sut = new EmailNotifier(mailManager);
		
		assertFalse(sut.notify(new TestNotification()));
	}
	
	
	
	
	
	
}
