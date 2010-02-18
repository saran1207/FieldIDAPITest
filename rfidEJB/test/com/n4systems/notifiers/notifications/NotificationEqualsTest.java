package com.n4systems.notifiers.notifications;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.notifiers.notifications.Notification;

public class NotificationEqualsTest {

	private final class NotificationExtension extends Notification {
		private String subject = "some_subject";;

		@Override
		public String subject() {
			return subject;
		}

		@Override
		public String notificationName() {
			return "some_notification_name";
		}
	}

	@Test
	public void testEqualsObject() {
		Notification one = new NotificationExtension();
		UserBean user = anEmployee().build();
		one.notifiyUser(user);
		Notification two = new NotificationExtension();
		two.notifiyUser(user);
		
		
		assertEquals(two, one);
	}
	
	@Test
	public void testNotEqual() {
		Notification one = new NotificationExtension();
		UserBean user = anEmployee().build();
		one.notifiyUser(user);
		Notification two = new NotificationExtension();
		two.notifiyUser(anEmployee().build());
		
		
		assertFalse(two.equals(one));
	}
	
	@Test
	public void testNotEqual_when_subjects_are_different() {
		Notification one = new NotificationExtension();
		UserBean user = anEmployee().build();
		one.notifiyUser(user);
		NotificationExtension two = new NotificationExtension();
		two.notifiyUser(user);
		two.subject = "other subject";
		
		
		assertFalse(two.equals(one));
	}

}
