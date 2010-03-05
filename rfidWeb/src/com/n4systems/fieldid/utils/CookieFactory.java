package com.n4systems.fieldid.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieFactory {
	public static final int TTL_DAYS_01 = 24 * 60 * 60;
	public static final int TTL_DAYS_30 = 30 * TTL_DAYS_01;
	public static final int TTL_YEARS_01 = 30 * TTL_DAYS_01;

	/** The default cookie TTL, currently 30 days */
	public static final int TTL_DEFAULT = TTL_DAYS_30;

	private static final int TTL_DELETE_COOKIE = 0;
	private static final int TTL_SESSION_ONLY = -1;

	private static final String CONTEXT_ROOT = "/";

	
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	
	/**
	 * Constructs a Cookie for a given name, value, TTL (in seconds) and valid
	 * context path.
	 * 
	 * @see HttpServletResponse#addCookie(Cookie)
	 * @param name
	 *            Name of the cookie conforming to RFC 2109
	 * @param value
	 *            Value for this cookie
	 * @param ttl
	 *            TTL for this cookie in seconds
	 * @param path
	 *            The context path which this cookie will be valid for
	 * @return The constructed Cookie
	 */
	private static Cookie createCookie(String name, String value, int ttl, String path) {
		Cookie cookie = new Cookie(name, value);

		cookie.setMaxAge(ttl);
		cookie.setPath(path);

		return cookie;
	}

	/**
	 * Constructs a Cookie for a given name, value, and TTL with a context path
	 * valid for the entire server (&quot/&quot).
	 * 
	 * @see #createCookie(String, String, int, String)
	 * @see HttpServletResponse#addCookie(Cookie)
	 * @param name
	 *            Name of the cookie conforming to RFC 2109
	 * @param value
	 *            Value for this cookie
	 * @param ttl
	 *            TTL for this cookie in seconds
	 * @return The constructed Cookie
	 */
	public static Cookie createCookie(String name, String value, int ttl) {
		return createCookie(name, value, ttl, CONTEXT_ROOT);
	}

	/**
	 * Constructs a Cookie which will survive only for the life of the browser
	 * (TTL set to -1), for a given name, and value with a context path valid
	 * for the entire server (&quot/&quot).
	 * 
	 * @see #createCookie(String, String, int, String)
	 * @see HttpServletResponse#addCookie(Cookie)
	 * @param name
	 *            Name of the cookie conforming to RFC 2109
	 * @param value
	 *            Value for this cookie
	 * @return The constructed Cookie
	 */
	public static Cookie createSessionCookie(String name, String value) {
		return createCookie(name, value, TTL_SESSION_ONLY, CONTEXT_ROOT);
	}

	/**
	 * Constructs a Cookie with a TTL set to {@link #TTL_DEFAULT}, for a given
	 * name, and value with a context path valid for the entire server
	 * (&quot/&quot).
	 * 
	 * @see #createCookie(String, String, int, String)
	 * @see HttpServletResponse#addCookie(Cookie)
	 * @param name
	 *            Name of the cookie conforming to RFC 2109
	 * @param value
	 *            Value for this cookie
	 * @return The constructed Cookie
	 */
	public static Cookie createDefaultCookie(String name, String value) {
		return createCookie(name, value, TTL_DAYS_30, CONTEXT_ROOT);
	}

	/**
	 * Constructs a Cookie for a given name which will instruct the browser to
	 * remove the cookie (Cookie TTL is set to 0).
	 * 
	 * @see #createCookie(String, String, int, String)
	 * @see HttpServletResponse#addCookie(Cookie)
	 * @param name
	 *            Name of the cookie conforming to RFC 2109
	 * @return The constructed Cookie
	 */
	public static Cookie createDeleteCookie(String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(TTL_DELETE_COOKIE);
		cookie.setPath(CONTEXT_ROOT);
		return cookie;
	}

	
	
	public CookieFactory(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}

	
	
	/**
	 * Locates a cookie value of the given name returns null if it is not found.
	 * @param name
	 * @param request
	 * @return
	 */
	public String findCookieValue(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public void addCookie(Cookie cookie) {
		response.addCookie(cookie);
	}

	public void removeCookie(String cookieName) {
		response.addCookie(CookieFactory.createDeleteCookie("loginTypeSecurityCard"));
	}
}
