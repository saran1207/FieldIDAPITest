package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeFormPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeViewPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;

public class EventFormEditorTest_DateFields extends PageNavigatingTestCase<EventTypeFormPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        Criteria date1 = scenario.aDateFieldCriteria().withDisplayText("date").build();
        Criteria date2 = scenario.aDateFieldCriteria().withDisplayText("datetime").build();

        CriteriaSection mainSection = scenario.aCriteriaSection()
                .withTitle("Main section")
                .withCriteria(date1, date2)
                .build();

        CriteriaSection otherSection = scenario.aCriteriaSection()
                .withTitle("Other section")
                .build();

        EventForm eventForm = scenario.anEventForm().withSections(mainSection, otherSection).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Test
    public void view_existing_text_field_criteria_in_editor() {
        page.clickCriteriaSection("Main section");
        assertEquals(Arrays.asList("date", "datetime"), page.getCriteriaNames());
        assertEquals("Date Field", page.getTypeForCriteria("date"));
        assertEquals("Date Field", page.getTypeForCriteria("datetime"));
    }

    @Test
    public void add_text_criteria() {
        page.clickCriteriaSection("Other section");
        assertEquals("Should start with no criteria in 'other' section", 0, page.getCriteriaNames().size());
        page.addCriteriaNamed("Date field 1", "Date Field");
        assertEquals("Should find new date criteria", 1, page.getCriteriaNames().size());
        assertEquals("Should find new date criteria", "Date field 1", page.getCriteriaNames().get(0));

        EventTypeViewPage viewPage = page.clickSaveAndFinishEventForm();
        assertEquals("Event Form saved.", page.getActionMessages().get(0));

        EventTypeFormPage newFormPage = viewPage.clickEventFormTab();
        newFormPage.clickCriteriaSection("Other section");
        assertEquals("Should find new text criteria", "Date field 1", newFormPage.getCriteriaNames().get(0));
        assertEquals("Date Field", newFormPage.getTypeForCriteria("Date field 1"));
    }

    @Override
    protected EventTypeFormPage navigateToPage() {
        EventTypeFormPage page = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes().clickEventTypeName("Test type").clickEventFormTab();
        return page;
    }

}
