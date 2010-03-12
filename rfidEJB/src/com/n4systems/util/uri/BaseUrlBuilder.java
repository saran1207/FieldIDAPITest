package com.n4systems.util.uri;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.HostNameParser;

public abstract class BaseUrlBuilder implements UrlBuilder {

	private static final String URL_ENCODING_CHARACTER_SET = "UTF-8";
	private final URI baseUri;
	private final ConfigContext configContext;
	private Tenant tenant;
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	

	public BaseUrlBuilder(URI baseUri, ConfigContext configContext) {
		super();
		
		this.baseUri = baseUri;
		this.configContext = configContext;
		
	}

	protected abstract String path();
	
	
	private URI getURI() {
		return getBaseUri().resolve(path() + getQueryString());
	}
	
	private String getQueryString()  {
		if (parameters.isEmpty()) {
			return "";
		}
		try {
			return createQueryString();
		} catch (UnsupportedEncodingException e) {
			throw new InvalidArgumentException("UTF-8 encoding is not supported ", e);
		}
	}

	private String createQueryString() throws UnsupportedEncodingException {
		String queryString = "";
		for (Entry<String, Object> entry : parameters.entrySet()) {
			queryString += "&" + encodeParameter(entry.getKey()) + "=" + encodeParameter(entry.getValue().toString());
		}
		
		return queryString.replaceFirst("&", "?");
	}

	private String encodeParameter(String urlString) throws UnsupportedEncodingException {
		return URLEncoder.encode(urlString, URL_ENCODING_CHARACTER_SET);
	}

	public String build() {
		try {
			URL url = getURL();
			
			url = setProtocol(url);
			
			url = adjustDomain(url);
			return url.toString();
		} catch (MalformedURLException e) {
			throw new InvalidArgumentException("the url could not be construtcted", e);
		}
	}

	private URL adjustDomain(URL result) throws MalformedURLException {
		if (tenant != null) {
			HostNameParser hostParser = HostNameParser.create(result);
			String newHostname = hostParser.replaceFirstSubDomain(tenant.getName());
			return new URL(result.getProtocol(), newHostname, result.getFile());
			
		}
		return result;
	}

	private URL setProtocol(URL url) throws MalformedURLException {
		if (url.getProtocol().equalsIgnoreCase(configContext.getString(ConfigEntry.SYSTEM_PROTOCOL))) {
			return url;
		}
		
		return new URL(configContext.getString(ConfigEntry.SYSTEM_PROTOCOL), url.getHost(), url.getFile());
	}

	private URL getURL() {
		try {
			return getURI().toURL();
		} catch (MalformedURLException e) {
			throw new InvalidArgumentException("the url could not be construtcted", e);
		}
	}
	
	@Override
	public String toString() {
		return build();
	}
	
	
	private URI getBaseUri() {
		return baseUri;
	}

	public ConfigContext getConfigContext() {
		return configContext;
	}

	public BaseUrlBuilder setCompany(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}
	
	
	public BaseUrlBuilder addParameter(String paramName, Object paramValue) {
		parameters.put(paramName, paramValue);
		return this;
	}
}