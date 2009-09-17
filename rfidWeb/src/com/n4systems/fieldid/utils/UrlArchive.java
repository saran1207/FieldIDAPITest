package com.n4systems.fieldid.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// TODO - this should be made compatible with WebSession (possibly part of WebSession)
public class UrlArchive {
	private final String storageName;
	private final HttpServletRequest request;
	private final HttpSession session;
	
	public UrlArchive(String storageName, HttpServletRequest request, HttpSession session) {
		super();
		this.storageName = storageName;
		this.request = request;
		this.session = session;
	}

	public void storeUrl() {
		String redirectLink = extractCurrentUrl();
		replaceUrl(redirectLink);
	}

	public String extractCurrentUrl() {
		String redirectLink = request.getRequestURI();
		if (redirectLink.startsWith(request.getContextPath())) {
			redirectLink = redirectLink.substring(request.getContextPath().length());
		}

		if (request.getQueryString() != null) {
			redirectLink += "?" + request.getQueryString();
		}
		return redirectLink;
	}
	
	public void replaceUrl(String url) {
		session.setAttribute(storageName, url);
	}
	
	public String fetchUrl() {
		return (String) session.getAttribute(storageName);
	}
	
	public void clearUrl() {
		session.removeAttribute(storageName);
	}	
}
