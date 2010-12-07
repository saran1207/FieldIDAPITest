package com.n4systems.ws.utils;

import static org.junit.Assert.*;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.ws.handlers.TenantLoadHandler;

public class ResourceContextTest {

	@Test
	public void sets_security_fields() {
		URI reqUri = URI.create("http://www.google.com");
		Tenant tenant = new Tenant(10L, "O Hai");
		
		UriInfo uriInfo = EasyMock.createMock(UriInfo.class);
		EasyMock.expect(uriInfo.getRequestUri()).andReturn(reqUri);
		EasyMock.replay(uriInfo);
		
		TenantLoadHandler handler = EasyMock.createMock(TenantLoadHandler.class);
		EasyMock.expect(handler.getTenant(reqUri)).andReturn(tenant);
		EasyMock.replay(handler);
		
		ResourceContext context = new ResourceContext(handler, uriInfo);
		
		EasyMock.verify(uriInfo);
		EasyMock.verify(handler);
		
		assertSame(uriInfo, context.getUriInfo());
		assertSame(tenant, context.getTenant());
		assertEquals(tenant.getId(), context.getSecurityFilter().getTenantId());
		assertNotNull(context.getLoaderFactory());
	}
	
}
