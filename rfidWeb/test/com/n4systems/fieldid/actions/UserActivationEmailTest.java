package com.n4systems.fieldid.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.notifiers.TestSingleNotifier;
import com.n4systems.notifiers.notifications.UserWelcomeEmail;


public class UserActivationEmailTest {

	
	@Test
	public void should_create_notification_for_given_user() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		UserBean user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier);
		sut.sendWelcomeNotificationTo(user);
		
		
		assertEquals(user, notifier.notification.getTo());
	}
	
	
	@Test
	public void should_create_notification_for_given_user_when_personalized() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		UserBean user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier);
		sut.sendPersonalizedWelcomeNotificationTo(user, "this is my personal note");
		
		assertEquals(user, notifier.notification.getTo());
	}
	
	@Test
	public void should_create_non_personalized_notification() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		UserBean user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier);
		sut.sendWelcomeNotificationTo(user);
		
		UserWelcomeEmail notification = (UserWelcomeEmail)notifier.notification;
		assertFalse(notification.isPersonalized());
	}
	
	
	@Test
	public void should_create_a_personalized_notification() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		UserBean user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier);
		sut.sendPersonalizedWelcomeNotificationTo(user, "Personalized Message");
		
		UserWelcomeEmail notification = (UserWelcomeEmail)notifier.notification;
		assertTrue(notification.isPersonalized());
		assertEquals("Personalized Message", notification.getPersonalMessage());
	}
	
	
}
