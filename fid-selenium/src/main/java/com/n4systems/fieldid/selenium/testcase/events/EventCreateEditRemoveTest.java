package com.n4systems.fieldid.selenium.testcase.events;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.EventPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;

public class EventCreateEditRemoveTest extends PageNavigatingTestCase<AssetPage> {

    private static final String TEST_IDENTIFIER = "4242QQ";
    private static final String TEST_SUB_IDENTIFIER = TEST_IDENTIFIER + "_SUB";

    @Override
    public void setupScenario(Scenario scenario) {
        EventType eventType = scenario.anEventType()
                .withMaster(true)
                .named("Master Event Type").build();

        AssetType subType = scenario.anAssetType()
                .named("Sub Asset Type")
                .withEventTypes(eventType)
                .build();

        AssetType masterType = scenario.anAssetType()
                .named("Master Asset Type")
                .withSubTypes(subType)
                .withEventTypes(eventType)
                .build();

        scenario.anAsset()
                .purchaseOrder("PO 3")
                .withIdentifier(TEST_IDENTIFIER)
                .ofType(masterType)
                .build();

        scenario.anAsset()
                .purchaseOrder("PO 3")
                .withIdentifier(TEST_SUB_IDENTIFIER)
                .ofType(subType)
                .build();
    }

    @Override
    protected AssetPage navigateToPage() {
        return startAsCompany("test1").login().search(TEST_IDENTIFIER);
    }

	@Test
	public void create_master_event_no_sub_events() {
        EventPage eventPage = page.clickEventHistoryTab().clickViewEventsByDateGroup().clickStartNewEvent("Master Event Type");

        performMandatoryEvent(eventPage);

		eventPage.clickSaveMasterEvent();
		
		assertTrue(eventPage.confirmMasterEventSaved());
	}

    @Test
	public void create_master_with_sub_event() {
		page.clickSubComponentsTab();
		page.addNewSubcomponent(TEST_SUB_IDENTIFIER);
		
		EventPage eventPage = page.clickEventHistoryTab().clickViewEventsByDateGroup().clickStartNewEvent("Master Event Type");

		performMandatoryEvent(eventPage);
		performSubEvent(eventPage);

		eventPage.clickSaveMasterEvent();

		assertTrue(eventPage.confirmMasterEventSaved());
	}
    
    @Test
    public void create_master_with_sub_event_from_existing_sub_component(){
    	page.clickSubComponentsTab();
    	page.attachExistingSubcomponent(TEST_SUB_IDENTIFIER);
    	
    	EventPage eventPage = page.clickEventHistoryTab().clickViewEventsByDateGroup().clickStartNewEvent("Master Event Type");

		performMandatoryEvent(eventPage);
		performSubEvent(eventPage);

		eventPage.clickSaveMasterEvent();

		assertTrue(eventPage.confirmMasterEventSaved());
    }
    
    @Test
    public void create_master_with_sub_event_and_then_remove_the_sub_component(){
    	page.clickSubComponentsTab();
		page.addNewSubcomponent(TEST_SUB_IDENTIFIER);
		
		EventPage eventPage = page.clickEventHistoryTab().clickViewEventsByDateGroup().clickStartNewEvent("Master Event Type");

		performMandatoryEvent(eventPage);
		performSubEvent(eventPage);
		
		page.clickRemoveSubComponent();
		
		eventPage.clickSaveMasterEvent();

		assertTrue(eventPage.confirmMasterEventSaved());
		
    }

	private void performMandatoryEvent(EventPage eventPage) {
		eventPage.clickMandatoryEventToPerformLink();
		eventPage.clickStore();
	}

	private void performSubEvent(EventPage eventPage) {
		eventPage.clickSubStartEventLink("Master Event Type");
		eventPage.clickStore();
	}

}
