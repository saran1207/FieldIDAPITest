package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeFormPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;

public class EventFormEditorTest_EditSections extends PageNavigatingTestCase<EventTypeFormPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        CriteriaSection sect1 = scenario.aCriteriaSection().withTitle("Section 1").build();
        CriteriaSection sect2 = scenario.aCriteriaSection().withTitle("Section 2").build();
        CriteriaSection sect3 = scenario.aCriteriaSection().withTitle("Section 3").build();

        EventForm eventForm = scenario.anEventForm().withSections(sect1, sect2, sect3).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Override
    protected EventTypeFormPage navigateToPage() {
    	return startAsCompany("test1").login().clickSetupLink().clickManageEventTypes().clickEventTypeName("Test type").clickEventFormTab();
    }

    @Test
    public void deleting_sections_should_succeed_and_persist() {
        assertEquals(Arrays.asList("Section 1", "Section 2", "Section 3"), page.getCriteriaSectionNames());
        page.deleteCriteriaSectionNamed("Section 2");
        assertEquals(Arrays.asList("Section 1", "Section 3"), page.getCriteriaSectionNames());
        saveAndFinishAndReturnToEventFormTab();
        assertEquals(Arrays.asList("Section 1", "Section 3"), page.getCriteriaSectionNames());
    }

    @Test
    public void renaming_sections_should_succeed_and_persist() {
        assertEquals(Arrays.asList("Section 1", "Section 2", "Section 3"), page.getCriteriaSectionNames());
        page.renameCriteriaSectionFromTo("Section 2", "Section LOL");
        assertEquals(Arrays.asList("Section 1", "Section LOL", "Section 3"), page.getCriteriaSectionNames());
        saveAndFinishAndReturnToEventFormTab();
        assertEquals(Arrays.asList("Section 1", "Section LOL", "Section 3"), page.getCriteriaSectionNames());
    }

    @Test
    public void copying_sections_should_succeed_and_persist() {
        assertEquals(Arrays.asList("Section 1", "Section 2", "Section 3"), page.getCriteriaSectionNames());
        page.copyCriteriaSectionNamed("Section 2");
        assertEquals(Arrays.asList("Section 1", "Section 2", "Section 3", "Section 2 (2)"), page.getCriteriaSectionNames());
        saveAndFinishAndReturnToEventFormTab();
        assertEquals(Arrays.asList("Section 1", "Section 2", "Section 3", "Section 2 (2)"), page.getCriteriaSectionNames());
    }

    private void saveAndFinishAndReturnToEventFormTab() {
        page.clickSaveAndFinishEventForm().clickEventFormTab();
    }

}
