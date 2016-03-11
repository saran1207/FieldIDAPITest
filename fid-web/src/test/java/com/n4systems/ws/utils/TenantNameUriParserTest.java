package com.n4systems.ws.utils;

import com.n4systems.ws.exceptions.WsSecurityException;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class TenantNameUriParserTest {
	private TenantNameUriParser parser = new TenantNameUriParser();
	
	@Test
	public void extract_first_subdomain_from_uri() {
		assertEquals("n4", parser.parseTenantName(URI.create("http://n4.fieldid.com/bleh")));
	}
	
	@Test
	public void handles_multi_sub_domains() {
		assertEquals("n4", parser.parseTenantName(URI.create("http://n4.hat.fieldid.com")));
	}
	
	@Test(expected=WsSecurityException.class)
	public void throws_security_exception_on_no_subdomain() {
		parser.parseTenantName(URI.create("http://fieldid.com"));
	}
}
