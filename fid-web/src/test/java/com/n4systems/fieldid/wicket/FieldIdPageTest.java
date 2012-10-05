package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.user.User;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import org.apache.wicket.protocol.http.WebApplication;
import org.junit.Before;

import java.net.MalformedURLException;
import java.net.URL;

import static org.easymock.EasyMock.*;

public abstract class FieldIdPageTest<T extends WicketHarness, F extends FieldIDFrontEndPage> extends WicketPageTest<T,F,FieldIdWicketTestContext> {

	protected ConfigurationProvider configurationProvider = createMock(ConfigurationProvider.class);
	private ConfigService configService;
    protected UserLimitService userLimitService;
    protected S3Service s3Service;
    private ConfigData configData;


    @Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		configService = wire(ConfigService.class);
        userLimitService = wire(UserLimitService.class);
        s3Service = wire(S3Service.class);
        configData = new ConfigData();
    }
	
	@Override
	public WebApplication createApp(ComponentTestInjector injector) {
		return new FieldIdTestableApp(injector);
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
		FieldIDSession session = (FieldIDSession) getWicketTester().getSession();
		session.setUser(user);
	}		
	
	protected void expectingConfig() {
		expect(configurationProvider.getString(ConfigEntry.SYSTEM_DOMAIN)).andReturn("localhost");
		expect(configurationProvider.getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT)).andReturn(new Integer(20));
		replay(configurationProvider);
		expect(configService.getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED)).andReturn(configData.googleAnalytics);
		expect(configService.getString(ConfigEntry.RSS_FEED)).andReturn(configData.rssFeed);
        expect(configService.getBoolean(ConfigEntry.APPTEGIC_ENABLED)).andReturn(configData.apptegic);
        expect(configService.getString(ConfigEntry.APPTEGIC_DATASET)).andReturn(configData.apptegicDataset);
		replay(configService);
	}

    protected ConfigurationProvider getConfigurationProvider() {
		return configurationProvider;
	}

	@Override
	protected TestContextHandler<FieldIdWicketTestContext> getContextHandler() {
		return new FieldIdTestContextHandler();
	}

    protected void expectingS3Service() throws MalformedURLException {
        expect(s3Service.getBrandingLogoURL()).andReturn(new URL("http://www.fieldid.com"));
        replay(s3Service);
    }

    protected void expectingUserService() {
        expect(userLimitService.isReadOnlyUsersEnabled()).andReturn(true);
        replay(userLimitService);
    }

    protected ConfigData withRssFeed(String rssFeed) {
        configData.rssFeed = rssFeed;
        return configData;
    }

    protected ConfigData withGoogleAnalytics(boolean googleAnalytics) {
        configData.googleAnalytics = googleAnalytics;
        return configData;
    }

    protected ConfigData withApptegicDataset(String dataset) {
        configData.apptegicDataset = dataset;
        return configData;
    }

    protected ConfigData withApptegic(boolean apptegic) {
        configData.apptegic = apptegic;
        return configData;
    }


    class ConfigData {
        private String rssFeed = "http://www.fieldid.com";
        private Boolean googleAnalytics = true;
        private Boolean apptegic = true;
        private String apptegicDataset = "apptegicDataset:testData-real data exists in CONFIGURATIONS table";
    }

}

