package com.n4systems.ws.utils;

import javax.ws.rs.core.UriInfo;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.handlers.TenantLoadHandler;

public class ResourceContext {
	private final UriInfo uriInfo;
	private final Tenant tenant;
	private final TenantOnlySecurityFilter securityFilter;
	private final LoaderFactory loaderFactory;
	
	public ResourceContext(UriInfo uriInfo) {
		this(new TenantLoadHandler(), uriInfo);
	}
	
	public ResourceContext(TenantLoadHandler tenantHandler, UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		tenant = tenantHandler.getTenant(uriInfo.getRequestUri());
		
		securityFilter = new TenantOnlySecurityFilter(tenant);
		securityFilter.enableShowArchived();
		
		loaderFactory = new LoaderFactory(securityFilter);
	}

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public TenantOnlySecurityFilter getSecurityFilter() {
		return securityFilter;
	}

	public LoaderFactory getLoaderFactory() {
		return loaderFactory;
	}
	
}
