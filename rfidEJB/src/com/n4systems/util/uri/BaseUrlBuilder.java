package com.n4systems.util.uri;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.n4systems.exceptions.InvalidArgumentException;

public abstract class BaseUrlBuilder implements UrlBuilder {
	protected static final String URL_ENCODING_CHARACTER_SET = "UTF-8";
	
	private final URI baseUri;
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	
	public BaseUrlBuilder(String baseUri) {
		this(URI.create(baseUri));
	}
	
	public BaseUrlBuilder(URI baseUri) {
		this.baseUri = baseUri;
	}
	
	/**
	 * Returns the absolute or relative path for this URL.  Note that
	 * absolute paths (paths starting with '/') will override any path
	 * supplied in the baseUri.  Relative paths will be appended to the baseUri.  
	 * @return
	 */
	protected abstract String path();
	
	private URI buildFullURI() {
		return baseUri.resolve(path() + getQueryString());
	}
	
	@Override
	public String build() {
		URI fullUri = buildFullURI();
		
		return fullUri.toString();
	}

	private String encodeParameter(String urlString) throws UnsupportedEncodingException {
		return URLEncoder.encode(urlString, URL_ENCODING_CHARACTER_SET);
	}

	private String createQueryString() throws UnsupportedEncodingException {
		String queryString = "";
		for (Entry<String, Object> entry : parameters.entrySet()) {
			queryString += "&" + encodeParameter(entry.getKey()) + "=" + encodeParameter(entry.getValue().toString());
		}
		
		return queryString.replaceFirst("&", "?");
	}
	
	private String getQueryString() {
		if (parameters.isEmpty()) {
			return "";
		}
		try {
			return createQueryString();
		} catch (UnsupportedEncodingException e) {
			throw new InvalidArgumentException("UTF-8 encoding is not supported ", e);
		}
	}
	
	public BaseUrlBuilder addParameter(String paramName, Object paramValue) {
		parameters.put(paramName, paramValue);
		return this;
	}
	
	@Override
	public String toString() {
		return build();
	}
}
