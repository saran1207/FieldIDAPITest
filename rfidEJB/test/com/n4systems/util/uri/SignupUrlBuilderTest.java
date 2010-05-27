package com.n4systems.util.uri;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;


import com.n4systems.model.user.User;
import com.n4systems.util.NonDataSourceBackedConfigContext;
import com.n4systems.util.uri.SignupUrlBuilder;

public class SignupUrlBuilderTest {
	private final User user = new User();
	
	@Before
	public void setup() {
		user.setReferralKey("12345");
	}
	
	@Test
	public void build_signup_resolves_url_correctly_1() {
		assertEquals("https://test.somedomain.com/signup/12345", (new SignupUrlBuilder(URI.create("https://test.somedomain.com/bleh/geh.action"),  new NonDataSourceBackedConfigContext(), user, "/signup")).toString());
	}
	
	@Test
	public void build_signup_resolves_url_correctly_2() {
		assertEquals("https://mark/signup/fieldid/12345", (new SignupUrlBuilder(URI.create("https://mark//"), new NonDataSourceBackedConfigContext(), user, "/signup/fieldid")).toString());
	}
	
	
}
