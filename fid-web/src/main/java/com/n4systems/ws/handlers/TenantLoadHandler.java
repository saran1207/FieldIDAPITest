package com.n4systems.ws.handlers;

import java.net.URI;

import org.apache.log4j.Logger;

import com.n4systems.model.Tenant;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.ws.exceptions.WsSecurityException;
import com.n4systems.ws.utils.TenantNameUriParser;

public class TenantLoadHandler {
	private static Logger logger = Logger.getLogger(TenantLoadHandler.class);
	private final TenantNameUriParser uriParser;
	private final TenantByNameLoader tenantLoader;
	
	public TenantLoadHandler() {
		this(new TenantNameUriParser(), new TenantByNameLoader());
	}
	
	public TenantLoadHandler(TenantNameUriParser uriParser, TenantByNameLoader tenantLoader) {
		this.uriParser = uriParser;
		this.tenantLoader = tenantLoader;
	}
	
	public Tenant getTenant(URI requestUri) throws WsSecurityException {
		String name = uriParser.parseTenantName(requestUri);
		Tenant tenant = loadTenant(name);
		
		if (tenant == null) {
			throw new WsSecurityException("Bad tenant [" + name + "]");
		}
		
		return tenant;
	}
	
	private Tenant loadTenant(String name) {
		try {
			return tenantLoader.setTenantName(name).load();
		} catch (Exception e) {
			logger.error("Failed loading Tenant [" + name + "]", e);
			return null;
		}
	}
}
