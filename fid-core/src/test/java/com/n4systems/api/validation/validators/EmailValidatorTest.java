package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;

public class EmailValidatorTest {
	private EmailValidator validator = new EmailValidator();
	
	private void testEmailPasses(String email) {
		assertTrue("[" + email + "] should have passed validation", validator.validate(email, null, null, null, null, null).isPassed());
	}
	
	private void testEmailFails(String email) {
		assertTrue("[" + email + "] should have failed validation", validator.validate(email, null, null, null, null, null).isFailed());
	}
	
	@Test
	public void test_pass_conditions() {
		String[] emails = {
				"flast@example.com",
				"CaSE@InsensItiVe.com",
				"1user1@10domin99.com",
				"first.last@example.com",
				"first_last@example.com",
				"first-last@example.com",
				"first.middle.last@example.com",
				"first@my.example.com",
				"first@my.other.example.com",
				"first.middle.last@my.other.example.com",
				// test some common tlds
				"joe@example.net",
				"joe@example.gov",
				"joe@example.ca",
				"joe@example.us",
				"joe@example.uk",
				"joe@example.org",
				"joe@example.mil"
		};
		
		for (String email: emails) {
			testEmailPasses(email);
		}
	}
	
	@Test
	public void test_fail_conditions() {
		String[] emails = {
				"",
				"@",
				"@.",
				"bleh",
				"@no_user.com",
				"no_domain@",
				"no_domain@.com",
				".starts_with_dot@example.com",
				"asd@example.badtld",
				"asd?dsm@example.com",
				"name@example."
		};
		
		for (String email: emails) {
			testEmailFails(email);
		}
	}
	
	
}
