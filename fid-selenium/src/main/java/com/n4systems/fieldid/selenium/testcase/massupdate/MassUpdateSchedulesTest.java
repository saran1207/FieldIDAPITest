package com.n4systems.fieldid.selenium.testcase.massupdate;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.misc.DateUtil;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;

public class MassUpdateSchedulesTest extends PageNavigatingTestCase<SchedulesSearchPage> {

	private static String COMPANY = "test1";
	private static final String TEST_EVENT_TYPE1 = "Event Type 1";
	private static final String TEST_EVENT_TYPE2 = "Event Type 2";
	private static final String SERIAL_NUMBER = "11111111";

	@Override
	public void setupScenario(Scenario scenario) {

		EventTypeGroup group = scenario.anEventTypeGroup()
									   .forTenant(scenario.tenant(COMPANY))
								       .withName("Test Event Type Group")
								 	   .build();

        EventType eventType1 = scenario.anEventType()
                                      .named(TEST_EVENT_TYPE1)
                                      .withGroup(group)
                                      .build();
        
        EventType eventType2 = scenario.anEventType()
                                       .named(TEST_EVENT_TYPE2)
                                       .withGroup(group)
                                       .build();
        
        AssetType assetType = scenario.assetType(COMPANY, TEST_ASSET_TYPE_1);
                
        scenario.save(new AssociatedEventType(eventType1, assetType));    
        scenario.save(new AssociatedEventType(eventType2, assetType));    

        
        Asset asset = scenario.anAsset()
                              .withOwner(scenario.primaryOrgFor(COMPANY))
                              .withSerialNumber(SERIAL_NUMBER)
                              .ofType(assetType)
                              .build();      
        
        scenario.save(new EventSchedule(asset, eventType1, new Date()));
        scenario.save(new EventSchedule(asset, eventType2, new Date()));        
     }
	
    @Override
    protected SchedulesSearchPage navigateToPage() {
        return startAsCompany(COMPANY).login().clickSchedulesLink();
    }

    @Test
    public void test_mass_update_schedules_next_date() throws Exception {
        page.enterSerialNumber(SERIAL_NUMBER);
        SchedulesSearchResultsPage resultsPage = page.clickRunSearchButton();

        assertEquals(2, resultsPage.getTotalResultsCount());

        resultsPage.selectItemOnRow(1);
        resultsPage.selectItemOnRow(2);

        Date beginningDate = DateUtil.parseDate(resultsPage.getScheduledDateForResult(1));
        SchedulesMassUpdatePage scheduleMassUpdatePage = resultsPage.clickMassUpdate();
        String nextDate = DateUtil.formatDate(DateUtil.theDayAfter(beginningDate));

        scheduleMassUpdatePage.enterNextEventDate(nextDate);
        resultsPage = scheduleMassUpdatePage.clickSaveButtonAndConfirm();

        assertEquals(nextDate, resultsPage.getScheduledDateForResult(1));
        assertEquals(nextDate, resultsPage.getScheduledDateForResult(2));
    }

}
