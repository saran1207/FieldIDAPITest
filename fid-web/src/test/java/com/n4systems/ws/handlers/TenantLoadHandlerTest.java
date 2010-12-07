package com.n4systems.ws.handlers;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.ws.exceptions.WsSecurityException;
import com.n4systems.ws.utils.TenantNameUriParser;

public class TenantLoadHandlerTest {
	
	@Test
	public void parses_tenant_name_from_uri_and_loads_tenant() {
		Tenant tenant = new Tenant(10L, "n4");
		URI requestUri = URI.create("http://n4.fieldid.com");
		
		TenantNameUriParser uriParser = EasyMock.createMock(TenantNameUriParser.class);
		EasyMock.expect(uriParser.parseTenantName(requestUri)).andReturn(tenant.getName());
		EasyMock.replay(uriParser);
		
		TenantByNameLoader loader = EasyMock.createMock(TenantByNameLoader.class);
		EasyMock.expect(loader.setTenantName(tenant.getName())).andReturn(loader);
		EasyMock.expect(loader.load()).andReturn(tenant);
		EasyMock.replay(loader);

		Tenant result = new TenantLoadHandler(uriParser, loader).getTenant(requestUri);
		
		EasyMock.verify(uriParser);
		EasyMock.verify(loader);
		
		assertEquals(tenant, result);
	}
	
	@Test(expected=WsSecurityException.class)
	public void throws_security_exception_on_null_tenant() throws URISyntaxException {
		TenantNameUriParser uriParser = new TenantNameUriParser() {
			@Override
			public String parseTenantName(URI requestUri) {
				return "O Hai";
			}
		};
		
		TenantByNameLoader loader = new TenantByNameLoader() {
			@Override
			public Tenant load() {
				return null;
			}
		};
		
		new TenantLoadHandler(uriParser, loader).getTenant(null);
	}
	
	@Test(expected=WsSecurityException.class)
	public void throws_security_exception_on_load_exception() throws URISyntaxException {
		TenantNameUriParser uriParser = new TenantNameUriParser() {
			@Override
			public String parseTenantName(URI requestUri) {
				return "O Hai";
			}
		};
		
		TenantByNameLoader loader = new TenantByNameLoader() {
			@Override
			public Tenant load() {
				throw new RuntimeException();
			}
		};
		
		new TenantLoadHandler(uriParser, loader).getTenant(null);
	}
	
}
