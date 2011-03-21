package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeFormPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeViewPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.builders.ComboBoxCriteriaBuilder;
import com.n4systems.model.builders.CriteriaSectionBuilder;

public class EventFormEditorTest_ComboBox extends PageNavigatingTestCase<EventTypeFormPage> {

    @Override
    public void setupScenario(Scenario scenario) {
    	ComboBoxCriteriaBuilder critBuilder = scenario.aComboBoxCriteria();
    	
        ComboBoxCriteria crit1 = critBuilder.withDisplayText("text1").build();
        ComboBoxCriteria crit2 = critBuilder.withDisplayText("text2").build();
        ComboBoxCriteria crit3 = critBuilder.withDisplayText("text3").build();

        CriteriaSectionBuilder builder = scenario.aCriteriaSection();

        CriteriaSection sect1 = builder.withTitle("Main Section").withCriteria(crit1, crit2, crit3).build();

        EventForm eventForm = scenario.anEventForm().withSections(sect1).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Override
    protected EventTypeFormPage navigateToPage() {
        EventTypeFormPage typesPage = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes().clickEventTypeName("Test type").clickEventFormTab();
        return typesPage;
    }

    @Test
    public void view_existing_select_criteria_in_editor() {
        page.clickCriteriaSection("Main Section");
        assertEquals(Arrays.asList("text1", "text2", "text3"), page.getCriteriaNames());

        assertEquals("Combo Box", page.getTypeForCriteria("text1"));
        assertEquals("Combo Box", page.getTypeForCriteria("text2"));
        assertEquals("Combo Box", page.getTypeForCriteria("text3"));
    }

    @Test
    public void add_combo_box_criteria_and_save_event_form() {
        page.clickCriteriaSection("Main Section");
        page.addCriteriaNamed("New Combo Box Criteria", "Combo Box");
        page.clickCriteria("New Combo Box Criteria");

        assertEquals("Should be no options in new combo box criteria", 0, page.getDropDownOptions().size());

        page.addComboBoxOption("New option 1");
        assertEquals("Should find new option", 1, page.getDropDownOptions().size());
        assertEquals("Should find new option", "New option 1", page.getDropDownOptions().get(0));

        EventTypeViewPage viewPage = page.clickSaveAndFinishEventForm();

        assertEquals("Should see save success message", 1, viewPage.getActionMessages().size());
        assertEquals("Event Form saved.", viewPage.getActionMessages().get(0));

        EventTypeFormPage newFormPage = viewPage.clickEventFormTab();
        newFormPage.clickCriteriaSection("Main Section");
        newFormPage.clickCriteria("New Combo Box Criteria");
        assertEquals("Should find new option", 1, newFormPage.getDropDownOptions().size());
        assertEquals("Should find new option", "New option 1", newFormPage.getDropDownOptions().get(0));
    }

}
