package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.misc.DateUtil;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesSearchResultsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage.EventFrequencyOverride;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;

public class AssetTypeAutoSchedulesTest extends FieldIDTestCase {

	private static String COMPANY = "test1";
    private static final String TEST_CUSTOMER_ORG = "TestCustomerOrg";
	private static final String TEST_EVENT_TYPE = "Test Event Type";
	private static final String SERIAL_NUMBER = "11111111";
	
	@Override
	public void setupScenario(Scenario scenario) {

		EventTypeGroup group = scenario.anEventTypeGroup()
									   .forTenant(scenario.tenant(COMPANY))
								       .withName("Test Event Type Group")
								 	   .build();

        EventType eventType = scenario.anEventType()
                                      .named(TEST_EVENT_TYPE)
                                      .withGroup(group)
                                      .build();
        
        AssetType assetType = scenario.assetType(COMPANY, TEST_ASSET_TYPE_1);
        
        AssociatedEventType associatedEventType = new AssociatedEventType(eventType, assetType);
        
        scenario.save(associatedEventType);    
        
        scenario.aCustomerOrg()
        		.withParent(scenario.primaryOrgFor(COMPANY))
        		.withName(TEST_CUSTOMER_ORG)
        		.build();
     }
	
	@Test
	public void verify_auto_schedule_created() throws Exception {
		ManageAssetTypesPage assetTypesPage = startAsCompany(COMPANY).login().clickSetupLink().clickAssetTypes();
		assetTypesPage.clickAssetType(TEST_ASSET_TYPE_1);
		assetTypesPage.clickEventFrequenciesTab();
		assetTypesPage.scheduleEventFrequencyForType(TEST_EVENT_TYPE, 14);
		assertEquals(14, assetTypesPage.getScheduledFrequencyForEventType(TEST_EVENT_TYPE));
		
		IdentifyPage identifyPage = assetTypesPage.clickIdentifyLink();
		
		identifyPage.enterSerialNumber(SERIAL_NUMBER);
		identifyPage.saveNewAsset();
		
		SchedulesSearchResultsPage searchResultPage = identifyPage.clickSchedulesLink().clickRunSearchButton();
		
		assertEquals(1, searchResultPage.getTotalResultsCount());
		assertEquals(getExpectedScheduleDate(14), searchResultPage.getScheduledDateForResult(1));
	}
	
	@Test
	public void verify_auto_schdedule_override_created() throws Exception {
		ManageAssetTypesPage assetTypesPage = startAsCompany(COMPANY).login().clickSetupLink().clickAssetTypes();
		assetTypesPage.clickAssetType(TEST_ASSET_TYPE_1);
		assetTypesPage.clickEventFrequenciesTab();
		assetTypesPage.scheduleEventFrequencyForType(TEST_EVENT_TYPE, 14);
		assertEquals(14, assetTypesPage.getScheduledFrequencyForEventType(TEST_EVENT_TYPE));

		assetTypesPage.addOverrideForOwner(TEST_EVENT_TYPE, new Owner(COMPANY,TEST_CUSTOMER_ORG), 7);
		
		List<EventFrequencyOverride> overrides = assetTypesPage.getEventFrequencyOverrides(TEST_EVENT_TYPE);
		assertEquals(1, overrides.size());
		assertEquals(7, overrides.get(0).frequency);
		assertEquals(TEST_CUSTOMER_ORG, overrides.get(0).customer);
		
		IdentifyPage identifyPage = assetTypesPage.clickIdentifyLink();
		
		identifyPage.enterSerialNumber(SERIAL_NUMBER);
		identifyPage.setOwner(new Owner(COMPANY,TEST_CUSTOMER_ORG));
		identifyPage.saveNewAsset();

		SchedulesSearchResultsPage searchResultPage = identifyPage.clickSchedulesLink().clickRunSearchButton();
		
		assertEquals(1, searchResultPage.getTotalResultsCount());
		assertEquals(getExpectedScheduleDate(7), searchResultPage.getScheduledDateForResult(1));
	}

	private String getExpectedScheduleDate(int interval) {
		return DateUtil.formatDate(DateUtil.addDays(new Date(), interval));
	}
}
