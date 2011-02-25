package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.builders.CriteriaSectionBuilder;
import com.n4systems.model.builders.SelectCriteriaBuilder;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EventFormEditorTest_Select extends PageNavigatingTestCase<ManageEventTypesPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        SelectCriteriaBuilder critBuilder = scenario.aSelectCriteria();

        SelectCriteria crit1 = critBuilder.withDisplayText("select1").build();
        SelectCriteria crit2 = critBuilder.withDisplayText("select2").build();
        SelectCriteria crit3 = critBuilder.withDisplayText("select3").build();

        CriteriaSectionBuilder builder = scenario.aCriteriaSection();

        CriteriaSection sect1 = builder.withTitle("Main Section").withCriteria(crit1, crit2, crit3).build();

        EventForm eventForm = scenario.anEventForm().withSections(sect1).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Override
    protected ManageEventTypesPage navigateToPage() {
        ManageEventTypesPage typesPage = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes();
        typesPage.clickEditEventType("Test type");
        typesPage.clickEventFormTab();
        return typesPage;
    }

    @Test
    public void view_existing_select_criteria_in_editor() {
        page.clickCriteriaSection("Main Section");
        assertEquals(Arrays.asList("select1", "select2", "select3"), page.getCriteriaNames());

        assertEquals("Select Box", page.getTypeForCriteria("select1"));
        assertEquals("Select Box", page.getTypeForCriteria("select2"));
        assertEquals("Select Box", page.getTypeForCriteria("select3"));
    }

    @Test
    public void add_select_criteria_and_save_event_form() {
        page.clickCriteriaSection("Main Section");
        page.addCriteriaNamed("New Select Criteria", "Select Box");
        page.clickCriteria("New Select Criteria");

        assertEquals("Should be no options in new select criteria", 0, page.getDropDownOptions().size());

        page.addDropDownOption("New option 1");
        assertEquals("Should find new option", 1, page.getDropDownOptions().size());
        assertEquals("Should find new option", "New option 1", page.getDropDownOptions().get(0));

        page.clickSaveAndFinishEventForm();

        assertEquals("Should see save success message", 1, page.getActionMessages().size());
        assertEquals("Event Form saved.", page.getActionMessages().get(0));

        page.clickEventFormTab();
        page.clickCriteriaSection("Main Section");
        page.clickCriteria("New Select Criteria");
        assertEquals("Should find new option", 1, page.getDropDownOptions().size());
        assertEquals("Should find new option", "New option 1", page.getDropDownOptions().get(0));
    }

}
