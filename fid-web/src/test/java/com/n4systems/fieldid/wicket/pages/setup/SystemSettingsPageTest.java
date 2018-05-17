package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.*;
import com.n4systems.fieldid.wicket.pages.setup.SystemSettingsPageTest.SystemsSettingsPageHarness;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.SystemSettings;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

@RunWith(FieldIdWicketTestRunner.class)
public class SystemSettingsPageTest extends FieldIdPageTest<SystemsSettingsPageHarness, SystemSettingsPage> implements IFixtureFactory<SystemSettingsPage> {

	private SystemSettingsService systemSettingsService;
	private UserLimitService userLimitService;
	private S3Service s3Service;
	private TenantSettingsService tenantSettingsService;
	private OrgService orgService;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		expectingConfig();
		expectingTenantSettingsService();
		systemSettingsService = wire(SystemSettingsService.class, "systemSettingsService");
		userLimitService = wire(UserLimitService.class);
		s3Service = wire(S3Service.class);
		orgService = wire(OrgService.class);
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
		PrimaryOrg primaryOrg = wire(PrimaryOrg.class);
		expect(primaryOrg.hasExtendedFeature(ExtendedFeature.GoogleTranslate)).andReturn(false);
		replay(primaryOrg);
		expect(orgService.getPrimaryOrgForTenant(anyLong())).andReturn(primaryOrg);
		replay(orgService);

		renderFixture(this);

		assertRenderedPage(SystemSettingsPage.class);

		assertChecked(false, getHarness().getAssetAssignmentCheckbox());
		assertChecked(false, getHarness().getManufacturerCertificatesCheckbox());
		assertChecked(true, getHarness().getGpsCaptureCheckbox());

		assertEquals(dateFormat, getHarness().getDateFormat().getModelObject());
	}

	@Override
	public SystemSettingsPage createFixture(String id) {
		return new SystemSettingsPage();
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
