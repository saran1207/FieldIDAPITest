package com.n4systems.util;

import java.net.URI;
import java.net.URL;


public class HostNameParser {
	private String[] hostParts;
	
	public static HostNameParser create(String requestURL) {
		return create(URI.create(requestURL));
	}
	
	public static HostNameParser create(URI uri) {
		return new HostNameParser(uri.getHost());
	}
	
	public static HostNameParser create(URL url) {
		return new HostNameParser(url.getHost());
	}
	
	public HostNameParser(String hostname) {
		hostParts = hostname.split("\\.");
	}
	
	/**
	 * @return Returns the Domain Name portion of the Host Name
	 */
	public String getDomainName() {
		StringBuilder domainName;
		
		// if the host name has two parts (eg fieldid.com) take the last two
		// XXX - note this does not handle domains with SLD's (eg bleh.gov.ca), we'd need to lookup against a TLD/SLD map for that
		if (hostParts.length >= 2) {
			domainName = new StringBuilder(hostParts[hostParts.length - 2]);
			domainName.append('.');
			domainName.append(hostParts[hostParts.length - 1]);
		} else {
			// if the hostname was not fully qualified, just return the hostname
			domainName = new StringBuilder(hostParts[0]);
		}
		
		return domainName.toString(); 
	}
	
	/**
	 * @return Returns the first sub-domain of the url
	 */
	public String getFirstSubDomain() {
		return hostParts[0];
	}
	
	/**
	 * @return Tests if this host name has a sub domain.  This test only works on fully qualified domain names.
	 */
	public boolean hasSubDomain() {
		return (hostParts.length >= 3);
	}
	
	/**
	 * Replaces the first sub domain in the hostname with the one specified.  If the hostname does not have a subdomain portion,
	 * firstSubDomain is prepended.
	 * @param firstSubDomain new sub domain
	 * @return the new hostname
	 */
	public String replaceFirstSubDomain(String firstSubDomain) {
		StringBuilder newHostname = new StringBuilder(firstSubDomain);
		
		// if the host has a subdomain, we replace the first element, otherwise we take the whole thing
		int startIdx = (hasSubDomain()) ? 1 : 0;
		
		for (int i = startIdx; i < hostParts.length; i++) {
			newHostname.append('.').append(hostParts[i]);
		}
		
		return newHostname.toString();
	}
}
