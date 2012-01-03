package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.user.User;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import org.junit.Before;

import static org.easymock.EasyMock.*;

public abstract class FieldIdPageTest<T extends WicketHarness, F extends FieldIDFrontEndPage> extends WicketPageTest<T,F> {

	protected ConfigurationProvider configurationProvider = createMock(ConfigurationProvider.class);
	private ConfigService configService;
		
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		initializeApplication(new FieldIdTestableApp(injector));
		configService = wire(ConfigService.class);
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
        //TODO: FIXME
//		FieldIDSession session = (FieldIDSession) getWicketTester().getWicketSession();
//		session.setUser(user);
	}		
	
	protected String getFeedUrl() {
		return "http://www.fieldid.com";
	}
	
	protected void expectingConfig() {
		expectingConfig(true, getFeedUrl());
	}

	protected void expectingConfig(boolean googleAnalytics) {
		expectingConfig(googleAnalytics, getFeedUrl());
	}
	
	protected void expectingConfig(boolean googleAnalytics, String rssFeed) {
		expect(configurationProvider.getString(ConfigEntry.CLICKTALE_START)).andReturn("");
		expect(configurationProvider.getString(ConfigEntry.CLICKTALE_END)).andReturn("");
		expect(configurationProvider.getString(ConfigEntry.SYSTEM_DOMAIN)).andReturn("localhost");
		expect(configurationProvider.getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT)).andReturn(new Integer(20));
		replay(configurationProvider);	
		expect(configService.getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED)).andReturn(googleAnalytics);
		expect(configService.getString(ConfigEntry.RSS_FEED)).andReturn(rssFeed);
		replay(configService);
	}
	
	protected ConfigurationProvider getConfigurationProvider() { 
		return configurationProvider;
	}

}

