package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.misc.DateUtil;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.security.Permissions;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MassUpdateOpenEventsTest extends PageNavigatingTestCase<ReportingPage> {

	private static String COMPANY = "test1";
	private static final String TEST_EVENT_TYPE1 = "Event Type 1";
	private static final String TEST_EVENT_TYPE2 = "Event Type 2";
	private static final String IDENTIFIER = "11111111";

	@Override
	public void setupScenario(Scenario scenario) {

        User defaultUser = scenario.defaultUser();
        defaultUser.setPermissions(Permissions.ALL);
        scenario.save(defaultUser);

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

        scenario.anEventStatus().named("Status").forTenant(scenario.defaultTenant()).build();

        Asset asset = scenario.anAsset()
                              .withOwner(scenario.primaryOrgFor(COMPANY))
                              .withIdentifier(IDENTIFIER)
                              .ofType(assetType)
                              .build();

        Event openEvent1 = scenario.anOpenEvent()
                                   .ofType(eventType1)
                                   .on(asset)
                                   .performedOn(null)
                                   .withPerformedBy(null)
                                   .withOwner(scenario.defaultPrimaryOrg())
                                   .withTenant(scenario.defaultTenant())
                                   .scheduledFor(new PlainDate())
                                   .build();
        Event openEvent2 = scenario.anOpenEvent()
                                   .ofType(eventType2)
                                   .on(asset)
                                   .performedOn(null)
                                   .withPerformedBy(null)
                                   .withOwner(scenario.defaultPrimaryOrg())
                                   .withTenant(scenario.defaultTenant())
                                   .scheduledFor(new PlainDate())
                                   .build();
     }
	
    @Override
    protected ReportingPage navigateToPage() {
        return startAsCompany(COMPANY).login().clickReportingLink();
    }

    @Test
    public void test_mass_update_open_event_next_date() throws Exception {
        page.selectOpenEvents();
        page.setDisplayColumns(new SearchDisplayColumns().selectOpenEventColumns());
        page.enterIdentifier(IDENTIFIER);
        page.clickRunSearchButton();

        assertEquals(2, page.getTotalResultsCount());

        page.selectItemOnRow(1);
        page.selectItemOnRow(2);

        EventMassUpdatePage massUpdatePage = page.clickEventMassUpdate();
        final String nextDate = DateUtil.formatDateTime(DateUtil.theDayAfter(new Date()));

        massUpdatePage.selectEdit();
        massUpdatePage.enterNextEventDate(nextDate);
        massUpdatePage.saveEditDetails();
        page = massUpdatePage.clickPerformMassUpdate();

        assertFalse(page.getActionMessages().isEmpty());
        assertEquals("Events are currently being updated for you. This may take some time for the process to complete", page.getActionMessages().get(0));
    }

    @Test
    public void test_mass_update_open_event_assign() throws Exception {
        page.selectOpenEvents();
        page.setDisplayColumns(new SearchDisplayColumns().selectOpenEventColumns());
        page.enterIdentifier(IDENTIFIER);
        page.clickRunSearchButton();

        assertEquals(2, page.getTotalResultsCount());

        page.selectItemOnRow(1);
        page.selectItemOnRow(2);

        EventMassUpdatePage massUpdatePage = page.clickEventMassUpdate();

        massUpdatePage.selectAssign();
        massUpdatePage.selectAssignee("some first last name");
        massUpdatePage.clickAssign();
        page = massUpdatePage.clickPerformMassUpdate();

        assertFalse(page.getActionMessages().isEmpty());
        assertEquals("Events are currently being updated for you. This may take some time for the process to complete", page.getActionMessages().get(0));
    }

    @Test
    public void test_mass_update_open_event_close() throws Exception {
        page.selectOpenEvents();
        page.setDisplayColumns(new SearchDisplayColumns().selectOpenEventColumns());
        page.enterIdentifier(IDENTIFIER);
        page.clickRunSearchButton();

        assertEquals(2, page.getTotalResultsCount());

        page.selectItemOnRow(1);
        page.selectItemOnRow(2);

        EventMassUpdatePage massUpdatePage = page.clickEventMassUpdate();

        massUpdatePage.selectClose();
        massUpdatePage.selectClosedBy("some first last name");
        massUpdatePage.clickCloseEvent();
        page = massUpdatePage.clickPerformMassUpdate();

        assertFalse(page.getActionMessages().isEmpty());
        assertEquals("open events are currently being updated for you. This may take some time for the process to complete", page.getActionMessages().get(0));
    }

}
