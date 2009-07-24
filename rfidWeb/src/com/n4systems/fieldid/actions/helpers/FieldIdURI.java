package com.n4systems.fieldid.actions.helpers;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import com.n4systems.fieldid.utils.ActionInvocationWrapper;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.HostNameParser;

public class FieldIdURI {

	private final ActionInvocationWrapper invocation;
	private final String tenantName;

	public FieldIdURI(ActionInvocationWrapper invocation, String tenantName) {
		super();
		this.invocation = invocation;
		this.tenantName = tenantName;
	}
	
	private HttpServletRequest getRequest() {
		return invocation.getRequest();
	}
	
	
	public boolean isNonBrandedUrl() {
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		boolean b = !hostParser.hasSubDomain();
		boolean nonbrandedsubdomain = hostParser.getFirstSubDomain().equals(ConfigContext.getCurrentContext().getString(ConfigEntry.UNBRANDED_SUBDOMAIN));
		return (b || nonbrandedsubdomain);
	}
	
	
	public String baseNonBrandedUrl() {
		String unbrandedSubDomain = ConfigContext.getCurrentContext().getString(ConfigEntry.UNBRANDED_SUBDOMAIN);
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		String newHostname = hostParser.replaceFirstSubDomain(unbrandedSubDomain);
		
		return "https://" + newHostname + "/fieldid";
	}
	
	public String baseBrandedUrl() {
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		String newHostname = hostParser.replaceFirstSubDomain(tenantName);
		
		return "https://" + newHostname + "/fieldid";
	}
	
	
	public URI getBaseURI() {
		// creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
		return URI.create(getRequest().getRequestURL().toString()).resolve(getRequest().getContextPath() + "/");
	}
	
	public URI createActionURI(String action) {
		return getBaseURI().resolve(action);
	}
	
}
