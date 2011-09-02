package com.n4systems.fieldid.wicket;

import static org.easymock.EasyMock.*;

import org.apache.wicket.protocol.http.WebApplication;
import org.junit.Before;

import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;



public abstract class FieldIdPageTest<T extends WicketHarness, F extends FieldIDLoggedInPage> extends WicketPageTest<T,F> {

	private ConfigurationProvider configurationProvider = createMock(ConfigurationProvider.class);
	
	
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		initializeApplication(new IApplicationFactory() {			
			@Override public WebApplication createTestApplication(ComponentTestInjector injector) {
				return new FieldIdTestableApp(injector);
			}
		});
	}	
	  
	@Override
	public F createFixture(IFixtureFactory<F> factory) {
		F fixture = factory.createFixture("id");
		if (fixture instanceof FieldIDLoggedInPage) { 
			FieldIDLoggedInPage fieldIdPage = fixture;
			// TODO DD : FIX THIS STATIC METHOD WORKAROUND!   
			fieldIdPage.setConfigurationProvider(configurationProvider);
		}
		return fixture;
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

