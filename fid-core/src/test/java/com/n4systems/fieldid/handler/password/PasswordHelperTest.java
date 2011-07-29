package com.n4systems.fieldid.handler.password;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;


public class PasswordHelperTest {

	PasswordHelper fixture= new PasswordHelper(new PasswordPolicy());
	private PasswordPolicy passwordPolicy;

	@Before
	public void setup() {
		passwordPolicy = new PasswordPolicy();
		fixture= new PasswordHelper(passwordPolicy);		
	}
	
	@Test
	public void test_IsPasswordExpired() {
		User user = UserBuilder.aFullUser().build();
		user.setPasswordChanged(DateUtils.addDays(new Date(), -2));
		passwordPolicy.setExpiryDays(1);
		assertTrue(fixture.isPasswordExpired(user));

		// user still has 3 days till expired...
		passwordPolicy.setExpiryDays(5);
		assertFalse(fixture.isPasswordExpired(user));
		
		// user will never expire (0=no check).
		passwordPolicy.setExpiryDays(0);
		assertFalse(fixture.isPasswordExpired(user));
	}
	
	@Test
	public void test_isValidPassword() {
		// underlying passwordComplexityChecker is already tested. just checking default values here.
		assertFalse(fixture.isValidPassword(null));	// too short
		assertFalse(fixture.isValidPassword("foob"));	// too short
		assertFalse(fixture.isValidPassword("fooba"));	// too short
		assertTrue(fixture.isValidPassword("foobar"));	// OK!
	}
	
	@Test
	public void test_isPasswordUnique() {
		User user = UserBuilder.aFullUser().build();
		// note that pw's are stored oldest to newest. (i.e. end of list is most recent value)
		user.setPreviousPasswords(Lists.newArrayList(User.hashPassword("a"),
				User.hashPassword("b"),
				User.hashPassword("c"),
				User.hashPassword("d"),
				User.hashPassword("e"),
				User.hashPassword("f"),
				User.hashPassword("g")));
		
		passwordPolicy.setUniqueness(3);
		
		assertTrue(fixture.isPasswordUnique(user, "a"));	// <-- good, previously used pw but not in the last 3. 
		
		assertFalse(fixture.isPasswordUnique(user, "e"));	// <-- no good, they are same as most recent pw's.
		assertFalse(fixture.isPasswordUnique(user, "f"));
		assertFalse(fixture.isPasswordUnique(user, "g"));
		
		passwordPolicy.setUniqueness(1);
		assertTrue(fixture.isPasswordUnique(user, "f"));	// this should be a good pw candidate now.
		
		passwordPolicy.setUniqueness(0);
		assertTrue(fixture.isPasswordUnique(user, "g"));	// this should be a good pw candidate now. (uniqueness==0 means no checking).
	}	
	
	
	
}
