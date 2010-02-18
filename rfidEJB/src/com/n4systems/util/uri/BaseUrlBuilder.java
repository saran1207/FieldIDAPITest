package com.n4systems.util.uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.HostNameParser;

public abstract class BaseUrlBuilder implements UrlBuilder {

	private final URI baseUri;
	private final ConfigContext configContext;
	private Tenant tenant;

	public BaseUrlBuilder(URI baseUri, ConfigContext configContext) {
		super();
		
		this.baseUri = baseUri;
		this.configContext = configContext;
		
	}

	protected abstract String path();
	
	
	private URI getURI() {
		return getBaseUri().resolve(path());
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
}