package com.n4systems.util.uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.HostNameParser;

public abstract class InternalUrlBuilder extends BaseUrlBuilder {
	private final ConfigurationProvider configContext;
	private Tenant tenant;
	
	public InternalUrlBuilder(URI baseUri, ConfigurationProvider configContext) {
		super(baseUri);
		this.configContext = configContext;
	}

	@Override
	public String build() {
		try {
			URL url = new URL(super.build());
			
			url = adjustProtocol(url);
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

	private URL adjustProtocol(URL url) throws MalformedURLException {
		if (url.getProtocol().equalsIgnoreCase(configContext.getString(ConfigEntry.SYSTEM_PROTOCOL))) {
			return url;
		}
		
		return new URL(configContext.getString(ConfigEntry.SYSTEM_PROTOCOL), url.getHost(), url.getFile());
	}

	public ConfigurationProvider getConfigContext() {
		return configContext;
	}

	public BaseUrlBuilder setCompany(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}
}