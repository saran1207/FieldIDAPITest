package com.n4systems.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class HostNameParserTest {
	
	@Test
	public void test_get_domain_name() {
		assertEquals("fieldid.com", new HostNameParser("markro.testing.fieldid.com").getDomainName());
	}

	@Test
	public void test_has_sub_domain() {
		assertTrue(new HostNameParser("www.fieldid.com").hasSubDomain());
		assertFalse(new HostNameParser("fieldid.com").hasSubDomain());
	}

	@Test
	public void test_get_first_subdomain() {
		assertEquals("www", new HostNameParser("www.fieldid.com").getFirstSubDomain());
	}
	
	@Test
	public void test_replace_first_subdomain() {
		assertEquals("unirope.fieldid.com", new HostNameParser("www.fieldid.com").replaceFirstSubDomain("unirope"));
		assertEquals("unirope.fieldid.com", new HostNameParser("fieldid.com").replaceFirstSubDomain("unirope"));
	}
}
