package com.n4systems.fieldid.actions.helpers;

import com.n4systems.fieldid.utils.ActionInvocationWrapper;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.HostNameParser;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class FieldIdURI {

	private final ActionInvocationWrapper invocation;
	private final String tenantName;

	public FieldIdURI(ActionInvocationWrapper invocation, String tenantName) {
		this.invocation = invocation;
		this.tenantName = tenantName;
	}
	
	private HttpServletRequest getRequest() {
		return invocation.getRequest();
	}
	
	public boolean isNonBrandedUrl() {
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		boolean b = !hostParser.hasSubDomain();
		boolean nonbrandedsubdomain = hostParser.getFirstSubDomain().equals(ConfigService.getInstance().getString(ConfigEntry.UNBRANDED_SUBDOMAIN));
		return (b || nonbrandedsubdomain);
	}
	
	public String baseNonBrandedUrl() {
		String unbrandedSubDomain = ConfigService.getInstance().getString(ConfigEntry.UNBRANDED_SUBDOMAIN);
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		String newHostname = hostParser.replaceFirstSubDomain(unbrandedSubDomain);
		
		return getProtocol() + newHostname + "/fieldid";
	}
	
	public String baseBrandedUrl() {
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		String newHostname = hostParser.replaceFirstSubDomain(tenantName);
		
		return getProtocol() + newHostname + "/fieldid";
	}
	
	public URI getBaseURI() {
		// creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
		return URI.create(getRequest().getRequestURL().toString()).resolve(getRequest().getContextPath() + "/");
	}
	
	public URI createActionURI(String action) {
		return getBaseURI().resolve(action);
	}

    private String getProtocol() {
        return ConfigService.getInstance().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://";
    }
	
}
