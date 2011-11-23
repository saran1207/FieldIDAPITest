package com.n4systems.fieldid.wicket;

import static org.easymock.EasyMock.*;

import org.junit.Before;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;



public abstract class FieldIdPageTest<T extends WicketHarness, F extends FieldIDFrontEndPage> extends WicketPageTest<T,F> {

	protected ConfigurationProvider configurationProvider = createMock(ConfigurationProvider.class);
	
	
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		initializeApplication(new FieldIdTestableApp(injector));
	}	
	  
	@Override
	public F createFixture(IFixtureFactory<F> factory) {
		F fixture = factory.createFixture("id");
		if (fixture instanceof FieldIDFrontEndPage) {
			FieldIDFrontEndPage fieldIdPage = fixture;
			fieldIdPage.setConfigurationProvider(configurationProvider);
		}
		return fixture;
	}
	
	protected void setSessionUser(User user) {
		FieldIDSession session = (FieldIDSession) getWicketTester().getWicketSession();
		session.setUser(user);
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

