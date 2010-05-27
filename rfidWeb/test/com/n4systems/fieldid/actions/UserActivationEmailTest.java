package com.n4systems.fieldid.actions;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;


import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.TestSingleNotifier;
import com.n4systems.notifiers.notifications.UserWelcomeEmail;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.uri.ActionURLBuilder;


public class UserActivationEmailTest {

	
	@Test
	public void should_create_notification_for_given_user() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		User user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier, createActionUrlBuilder());
		sut.sendWelcomeNotificationTo(user);
		
		
		assertEquals(user, notifier.notification.getTo());
	}
	
	
	@Test
	public void should_create_notification_for_given_user_when_personalized() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		User user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier, createActionUrlBuilder());
		sut.sendPersonalizedWelcomeNotificationTo(user, "this is my personal note");
		
		assertEquals(user, notifier.notification.getTo());
	}
	
	@Test
	public void should_create_non_personalized_notification() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		User user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier, createActionUrlBuilder());
		sut.sendWelcomeNotificationTo(user);
		
		UserWelcomeEmail notification = (UserWelcomeEmail)notifier.notification;
		assertFalse(notification.isPersonalized());
	}
	
	
	@Test
	public void should_create_a_personalized_notification() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		User user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier, createActionUrlBuilder());
		sut.sendPersonalizedWelcomeNotificationTo(user, "Personalized Message");
		
		UserWelcomeEmail notification = (UserWelcomeEmail)notifier.notification;
		assertTrue(notification.isPersonalized());
		assertEquals("Personalized Message", notification.getPersonalMessage());
	}
	
	
	@Test
	public void should_provide_sign_in_url_to_notification() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		User user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier, createActionUrlBuilder());
		sut.sendWelcomeNotificationTo(user);
		
		UserWelcomeEmail notification = (UserWelcomeEmail)notifier.notification;
		assertThat(notification.getSignInUrl(), endsWith("/login.action"));
	}
	
	@Test
	public void should_provide_forgot_password_url_to_notification() throws Exception {
		TestSingleNotifier notifier = new TestSingleNotifier();
		User user = UserBuilder.aUser().build();
		
		UserWelcomeNotificationProducer sut = new UserWelcomeNotificationProducer(notifier, createActionUrlBuilder());
		sut.sendWelcomeNotificationTo(user);
		
		UserWelcomeEmail notification = (UserWelcomeEmail)notifier.notification;
		assertThat(notification.getForgotPasswordUrl(), endsWith("/forgotPassword.action"));
	}
	

	private ActionURLBuilder createActionUrlBuilder() {
		return new ActionURLBuilder(URI.create("https://fieldid.fieldid.com/fieldid"), new ConfigContextOverridableTestDouble());
	}
}
