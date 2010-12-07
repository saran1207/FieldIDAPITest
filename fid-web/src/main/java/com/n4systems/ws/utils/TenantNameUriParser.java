package com.n4systems.ws.utils;

import java.net.URI;

import com.n4systems.util.HostNameParser;
import com.n4systems.ws.exceptions.WsSecurityException;

public class TenantNameUriParser {

	public String parseTenantName(URI requestUri) {
		HostNameParser parser = HostNameParser.create(requestUri);
		
		if (!parser.hasSubDomain()) {
			throw new WsSecurityException("Missing Tenant subdomain");
		}

		return parser.getFirstSubDomain();
	}
	
}
