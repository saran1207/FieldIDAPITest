package com.n4systems.util.uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public abstract class BaseUrlBuilder implements UrlBuilder {

	private final URI baseUri;
	private final ConfigContext configContext;

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
			
			if (url.getProtocol().equalsIgnoreCase(configContext.getString(ConfigEntry.SYSTEM_PROTOCOL))) {
				return url.toString();
			}
			
			return new URL(configContext.getString(ConfigEntry.SYSTEM_PROTOCOL), url.getHost(), url.getFile()).toString();
		} catch (MalformedURLException e) {
			throw new InvalidArgumentException("the url could not be construtcted", e);
		}
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
}