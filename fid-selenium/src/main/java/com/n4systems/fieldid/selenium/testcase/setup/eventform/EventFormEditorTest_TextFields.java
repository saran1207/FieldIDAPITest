package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EventFormEditorTest_TextFields extends PageNavigatingTestCase<ManageEventTypesPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        Criteria text1 = scenario.aTextFieldCriteria().withDisplayText("text1").build();
        Criteria text2 = scenario.aTextFieldCriteria().withDisplayText("text2").build();

        CriteriaSection mainSection = scenario.aCriteriaSection()
                .withTitle("Main section")
                .withCriteria(text1, text2)
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
        assertEquals(Arrays.asList("text1", "text2"), page.getCriteriaNames());
        assertEquals("Text Field", page.getTypeForCriteria("text1"));
        assertEquals("Text Field", page.getTypeForCriteria("text2"));
    }

    @Test
    public void add_text_criteria() {
        page.clickCriteriaSection("Other section");
        assertEquals("Should start with no criteria in 'other' section", 0, page.getCriteriaNames().size());
        page.addCriteriaNamed("Text field 1", "Text Field");
        assertEquals("Should find new text criteria", 1, page.getCriteriaNames().size());
        assertEquals("Should find new text criteria", "Text field 1", page.getCriteriaNames().get(0));

        page.clickSaveAndFinishEventForm();
        assertEquals("Event Form saved.", page.getActionMessages().get(0));

        page.clickEventFormTab();
        page.clickCriteriaSection("Other section");
        assertEquals("Should find new text criteria", "Text field 1", page.getCriteriaNames().get(0));
        assertEquals("Text Field", page.getTypeForCriteria("Text field 1"));
    }

    @Override
    protected ManageEventTypesPage navigateToPage() {
        ManageEventTypesPage page = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes();
        page.clickEditEventType("Test type");
        page.clickEventFormTab();
        return page;
    }

}
