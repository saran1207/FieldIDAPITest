package com.n4systems.fieldid.wicket;

import static org.easymock.EasyMock.*;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.junit.Before;

import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;



public abstract class FieldIdPageTestCase<T extends WicketHarness> extends WicketTestCase<T> {

	private ConfigurationProvider configurationProvider = createMock(ConfigurationProvider.class);
	
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		setPathContext(PAGE_CONTEXT);
	}
	
	@Override
	protected IApplicationFactory getApplicationFactory() {
		return new IApplicationFactory() {			
			@Override
			public WebApplication createTestApplication(ComponentTestInjector injector) {
				return new FieldIdTestableApp(injector);
			}
		};
	}
	
	@Override
	public void renderFixture(IFixtureFactory<?> factory) {
		Page page = (Page)factory.createFixture("id");
		if (page instanceof FieldIDLoggedInPage) { 
			FieldIDLoggedInPage fieldIdPage = (FieldIDLoggedInPage) page;
			fieldIdPage.setConfigurationProvider(configurationProvider);
		}
		getWicketTester().startPage(page);
	}
	
	protected void expectingConfigurationProvider() {
		expect(configurationProvider.getString(ConfigEntry.CLICKTALE_START)).andReturn("");
		expect(configurationProvider.getString(ConfigEntry.CLICKTALE_END)).andReturn("");
		expect(configurationProvider.getString(ConfigEntry.SYSTEM_DOMAIN)).andReturn("localhost");
		expect(configurationProvider.getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT)).andReturn(new Integer(20));
		replay(configurationProvider);		
	}

	protected ConfigurationProvider getConfigurationProvider() { 
		return configurationProvider;
	}

}

