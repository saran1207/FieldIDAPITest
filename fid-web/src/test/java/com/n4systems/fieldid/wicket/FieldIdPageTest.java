package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.services.config.ConfigService;
import com.n4systems.services.config.MutableRootConfig;
import com.n4systems.services.config.RootConfig;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.protocol.http.WebApplication;
import org.junit.Before;

import java.net.MalformedURLException;
import java.net.URL;

import static org.easymock.EasyMock.*;

public abstract class FieldIdPageTest<T extends WicketHarness, F extends FieldIDTemplatePage> extends WicketPageTest<T,F,FieldIdWicketTestContext> {

//	protected ConfigurationProvider configurationProvider = createMock(ConfigurationProvider.class);
	private ConfigService configService;
    protected UserLimitService userLimitService;
    protected S3Service s3Service;
    private ConfigData configData;
    private TenantSettingsService tenantSettingsService;


    @Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		configService = wire(ConfigService.class);
        userLimitService = wire(UserLimitService.class);
        s3Service = wire(S3Service.class);
        tenantSettingsService = wire(TenantSettingsService.class);
        configData = new ConfigData();
    }
	
	@Override
	public WebApplication createApp(ComponentTestInjector injector) {
		return new FieldIdTestableApp(injector);
	}
	  
	@Override
	public F createFixture(IFixtureFactory<F> factory) {
		F fixture = factory.createFixture("id");
		return fixture;
	}
	
	protected void setSessionUser(User user) {
		FieldIDSession session = (FieldIDSession) getWicketTester().getSession();
		session.setUser(user);
	}		
	
	protected void expectingConfig() {
        expect(configService.getString(ConfigEntry.SYSTEM_DOMAIN)).andReturn("localhost");
        expect(configService.getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT)).andReturn(new Integer(20));
		expect(configService.getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED)).andReturn(configData.googleAnalytics);
		expect(configService.getString(ConfigEntry.RSS_FEED)).andReturn(configData.rssFeed);
        expect(configService.getBoolean(ConfigEntry.APPTEGIC_ENABLED)).andReturn(configData.apptegic);
        expect(configService.getString(ConfigEntry.APPTEGIC_DATASET)).andReturn(configData.apptegicDataset);
        expect(configService.getString(ConfigEntry.CUSTOM_JS)).andReturn("");
        expect(configService.getString(eq(ConfigEntry.FOOTER_SCRIPT), anyLong())).andReturn("");
        expect(configService.getString(eq(ConfigEntry.HEADER_SCRIPT), anyLong())).andReturn("");
		expect(configService.getConfig()).andReturn(new RootConfig(new MutableRootConfig()));


		replay(configService);
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

    protected void expectingTenantSettingsService() {
        expect(tenantSettingsService.getTenantSettings()).andReturn(new TenantSettings());
        replay(tenantSettingsService);
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

