package com.n4systems.fieldid.actions.safetynetwork;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.fieldid.actions.safetyNetwork.SignupUrlBuilder;

public class SignupUrlBuilderTest {
	private final UserBean user = new UserBean();
	
	@Before
	public void setup() {
		user.setReferralKey("12345");
	}
	
	@Test
	public void build_signup_resolves_url_correctly_1() {
		assertEquals("https://test.somedomain.com/signup/12345", (new SignupUrlBuilder(URI.create("https://test.somedomain.com/bleh/geh.action"), user, "/signup")).toString());
	}
	
	@Test
	public void build_signup_resolves_url_correctly_2() {
		assertEquals("http://mark/signup/fieldid/12345", (new SignupUrlBuilder(URI.create("http://mark//"), user, "/signup/fieldid")).toString());
	}
}
