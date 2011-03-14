package com.n4systems.fieldid.selenium.testcase.events;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.components.EventInfoPopup;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.EventPage;
import com.n4systems.fieldid.selenium.pages.EventsPerformedPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;

public class PerformEventWithUnitOfMeasureCriteriaTest extends PageNavigatingTestCase<EventPage> {

    private static final String TEST_SERIAL_NUMBER = "123";

    @Override
    public void setupScenario(Scenario scenario) {
        Criteria textCrit = scenario.aTextFieldCriteria().withDisplayText("Text Crit").build();
        Criteria unitCrit = scenario.aUnitOfMeasureCriteria().withDisplayText("Unit Crit").primaryUnit(scenario.unitOfMeasure("Feet")).build();

        CriteriaSection section = scenario.aCriteriaSection()
                .withTitle("Section One")
                .withCriteria(textCrit, unitCrit)
                .build();

        EventForm eventForm = scenario.anEventForm()
                .withSections(section).build();

        EventType eventType = scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test Event Type").build();

        AssetType assetType = scenario.anAssetType()
                .named("Test Asset Type")
                .withEventTypes(eventType)
                .build();

        scenario.anAsset()
                .withSerialNumber(TEST_SERIAL_NUMBER)
                .ofType(assetType)
                .build();
    }

    @Override
    protected EventPage navigateToPage() {
        return startAsCompany("test1")
        		.login()
        		.search(TEST_SERIAL_NUMBER)
                .clickEventsTab()
                .clickStartEventWithOnlyOneEventType();
    }

    @Test
    public void perform_event_and_edit_underlying_event_form() {
        page.enterTextCriteria("Text Crit", "My event value");
        page.enterPrimaryUnitOfMeasureValue("Unit Crit", "5");

        page.clickSaveNormalEvent();
        AssetPage assetPage = page.clickAssetInformationTab();

        EventsPerformedPage eventsPerformedPage = assetPage.clickEventsTab();
        EventInfoPopup popup = eventsPerformedPage.clickViewLatestEvent();

        assertEquals("My event value", popup.getTextValueForCriteria("Text Crit"));
        assertEquals("5 ft", popup.getTextValueForCriteria("Unit Crit"));
    }

}
