package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.StateSet;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventFormEditorTest_RetiredItems extends FieldIDTestCase {

    @Override
    public void setupScenario(Scenario scenario) {
        CriteriaSection retiredSection = scenario.aCriteriaSection()
                .withTitle("Retired section")
                .withRetired(true)
                .build();

        StateSet buttonGroup = scenario.buttonGroup(scenario.tenant("test1"), "Pass, Fail");

        OneClickCriteria retiredCriteria = scenario.aCriteria().withText("Retired criteria").withRetired(true).withStateSet(buttonGroup).build();
        OneClickCriteria activeCriteria = scenario.aCriteria().withText("Active criteria").withRetired(false).withStateSet(buttonGroup).build();

        CriteriaSection nonRetiredSection = scenario.aCriteriaSection()
                .withTitle("Active section")
                .withCriteria(activeCriteria, retiredCriteria)
                .withRetired(false)
                .build();

        EventForm eventForm = scenario.anEventForm().withSections(retiredSection, nonRetiredSection).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Test
    public void event_form_editor_should_not_display_retired_sections_or_criteria() {
        ManageEventTypesPage eventTypesPage = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes();
        eventTypesPage.clickEditEventType("Test type");
        eventTypesPage.clickEventFormTab();
        List<String> sectionNames = eventTypesPage.getCriteriaSectionNames();

        assertTrue(sectionNames.contains("Active section"));
        assertFalse(sectionNames.contains("Retired section"));

        eventTypesPage.clickCriteriaSection("Active section");
        List<String> criteriaNames = eventTypesPage.getCriteriaNames();

        assertTrue(criteriaNames.contains("Active criteria"));
        assertFalse(criteriaNames.contains("Retired criteria"));
    }

}
