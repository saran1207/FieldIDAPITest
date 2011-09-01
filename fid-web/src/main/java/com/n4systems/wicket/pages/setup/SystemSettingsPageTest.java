package com.n4systems.wicket.pages.setup;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import com.n4systems.fieldid.wicket.FieldIdPageTestCase;
import com.n4systems.fieldid.wicket.IFixtureFactory;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.pages.setup.SystemSettingsPage;
import com.n4systems.model.tenant.SystemSettings;
import com.n4systems.wicket.pages.setup.SystemSettingsPageTest.SystemsSettingsPageHarness;

public class SystemSettingsPageTest extends FieldIdPageTestCase<SystemsSettingsPageHarness> implements IFixtureFactory<SystemSettingsPage> {	
	private SystemSettingsService systemSettingsService;

		
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		expectingConfigurationProvider();
		systemSettingsService = wire(SystemSettingsService.class, "systemSettingsService");
	}

	@Test
	public void testRender() {
		SystemSettings settings = new SystemSettings();
		String dateFormat = "mm/dd/yy";
		settings.setDateFormat(dateFormat);
		settings.setGpsCapture(true);
		
		expect(systemSettingsService.getSystemSettings()).andReturn(settings);
		replay(systemSettingsService);
		
		renderFixture(this);
		
		assertRenderedPage(SystemSettingsPage.class);
		
		assertChecked(false, getHarness().getAssetAssignmentCheckbox());
		assertChecked(false, getHarness().getManufacturerCertificatesCheckbox());
		assertChecked(false, getHarness().getOrderDetailsCheckbox());
		assertChecked(true, getHarness().getGpsCaptureCheckbox());
		
		assertEquals(dateFormat, getHarness().getDateFormat().getValue());
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
			return (CheckBox) getComponent("systemSettingsForm:assignedTo");
		}
		
		public CheckBox getProofTestIntegrationCheckbox() { 
			return (CheckBox) getComponent("systemSettingsForm:proofTestIntegration");
		}
		
		public CheckBox getManufacturerCertificatesCheckbox() { 
			return (CheckBox) getComponent("systemSettingsForm:manufacturerCertificate");
		}
		
		public CheckBox getOrderDetailsCheckbox() { 
			return (CheckBox) getComponent("systemSettingsForm:orderDetails");
		}
		
		public CheckBox getGpsCaptureCheckbox() { 
			return (CheckBox) getComponent("systemSettingsForm:gpsCapture");
		}
		
		@SuppressWarnings("unchecked")
		public DropDownChoice<String> getDateFormat() { 
			return (DropDownChoice<String>) getComponent("systemSettingsForm:dateFormat");
		}		
		
	}


}
