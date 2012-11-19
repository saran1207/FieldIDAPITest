package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.misc.DateUtil;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesMassUpdatePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MassUpdateSchedulesTest extends PageNavigatingTestCase<SchedulesSearchPage> {

	private static String COMPANY = "test1";
	private static final String TEST_EVENT_TYPE1 = "Event Type 1";
	private static final String TEST_EVENT_TYPE2 = "Event Type 2";
	private static final String IDENTIFIER = "11111111";

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
                              .withIdentifier(IDENTIFIER)
                              .ofType(assetType)
                              .build();      
        
     }
	
    @Override
    protected SchedulesSearchPage navigateToPage() {
        return startAsCompany(COMPANY).login().clickSchedulesLink();
    }

    @Test
    public void test_mass_update_schedules_next_date() throws Exception {
        page.enterIdentifier(IDENTIFIER);
        page.clickRunSearchButton();

        assertEquals(2, page.getTotalResultsCount());

        page.selectItemOnRow(1);
        page.selectItemOnRow(2);

        Date beginningDate = DateUtil.parseDate(page.getScheduledDateForResult(1));
        SchedulesMassUpdatePage scheduleMassUpdatePage = page.clickMassUpdate();
        final String nextDate = DateUtil.formatDate(DateUtil.theDayAfter(beginningDate));

        scheduleMassUpdatePage.enterNextEventDate(nextDate);
        final SchedulesSearchPage finalResultsPage = scheduleMassUpdatePage.clickSaveButtonAndConfirm();

        new ConditionWaiter(new Predicate() {
            @Override
            public boolean evaluate() {
                finalResultsPage.clickSchedulesLink().clickRunSearchButton();
                return nextDate.equals(finalResultsPage.getScheduledDateForResult(1));
            }
        }).run("Mass schedule update should complete successfully");

        assertEquals(nextDate, finalResultsPage.getScheduledDateForResult(1));
        assertEquals(nextDate, finalResultsPage.getScheduledDateForResult(2));
    }

}
