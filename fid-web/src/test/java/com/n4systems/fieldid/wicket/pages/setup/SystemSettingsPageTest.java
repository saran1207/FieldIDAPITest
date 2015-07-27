package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.FieldIdWicketTestRunner;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(FieldIdWicketTestRunner.class)
@Ignore("Fix me")
public class SystemSettingsPageTest {

}

/*
public class SystemSettingsPageTest extends FieldIdPageTest<SystemsSettingsPageHarness, SystemSettingsPage> implements IFixtureFactory<SystemSettingsPage> {	

	private SystemSettingsService systemSettingsService;
	private UserLimitService userLimitService;
    private S3Service s3Service;
    private TenantSettingsService tenantSettingsService;
		
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		expectingConfig();
        expectingTenantSettingsService();
		systemSettingsService = wire(SystemSettingsService.class, "systemSettingsService");
		userLimitService = wire(UserLimitService.class);
        s3Service = wire(S3Service.class);
	}

	@Test
	public void testRender() throws MalformedURLException {
		SystemSettings settings = new SystemSettings();
		String dateFormat = "mm/dd/yy";
		settings.setDateFormat(dateFormat);
		settings.setGpsCapture(true);
		
		expect(systemSettingsService.getSystemSettings()).andReturn(settings);
		replay(systemSettingsService);
		expect(userLimitService.isReadOnlyUsersEnabled()).andReturn(true);
		replay(userLimitService);
        expect(s3Service.getBrandingLogoURL()).andReturn(new URL("http://www.fieldid.com"));
        replay(s3Service);

		renderFixture(this);
		
		assertRenderedPage(SystemSettingsPage.class);
		
		assertChecked(false, getHarness().getAssetAssignmentCheckbox());
		assertChecked(false, getHarness().getManufacturerCertificatesCheckbox());
		assertChecked(true, getHarness().getGpsCaptureCheckbox());

        assertEquals(dateFormat, getHarness().getDateFormat().getModelObject());
	}
	
	@Override
	public SystemSettingsPage createFixture(String id) {
		return new SystemSettingsPage(getConfigurationProvider());
	}

	@Override
	protected SystemsSettingsPageHarness createHarness(String pathContext, IWicketTester wicketTester) {
		return new SystemsSettingsPageHarness(pathContext, wicketTester);
	}
	
	
	class SystemsSettingsPageHarness extends WicketHarness {

		public SystemsSettingsPageHarness(String pathContext, IWicketTester tester) {
			super(pathContext, tester);			
		}
		
		public CheckBox getAssetAssignmentCheckbox() { 
			return (CheckBox) get("systemSettingsForm:assignedTo");
		}
		
		public CheckBox getProofTestIntegrationCheckbox() { 
			return (CheckBox) get("systemSettingsForm:proofTestIntegration");
		}
		
		public CheckBox getManufacturerCertificatesCheckbox() { 
			return (CheckBox) get("systemSettingsForm:manufacturerCertificate");
		}
		
		public CheckBox getOrderDetailsCheckbox() { 
			return (CheckBox) get("systemSettingsForm:orderDetails");
		}
		
		public CheckBox getGpsCaptureCheckbox() { 
			return (CheckBox) get("systemSettingsForm:gpsCapture");
		}
		
		@SuppressWarnings("unchecked")
		public DropDownChoice<String> getDateFormat() { 
			return (DropDownChoice<String>) get("systemSettingsForm:dateFormat");
		}		
		
	}


}
*/