package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.builders.CriteriaSectionBuilder;
import com.n4systems.model.builders.OneClickCriteriaBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EventFormEditorTest_Reorder extends FieldIDTestCase {

    @Override
    public void setupScenario(Scenario scenario) {
        OneClickCriteriaBuilder critBuilder = scenario.aOneClickCriteria().withStateSet(scenario.buttonGroup(scenario.defaultTenant(), "Pass, Fail"));

        OneClickCriteria crit1 = critBuilder.withDisplayText("Criteria1").build();
        OneClickCriteria crit2 = critBuilder.withDisplayText("Criteria2").build();
        OneClickCriteria crit3 = critBuilder.withDisplayText("Criteria3").build();

        CriteriaSectionBuilder builder = scenario.aCriteriaSection();

        CriteriaSection sect1 = builder.withTitle("Section1").withCriteria(crit1, crit2, crit3).build();
        CriteriaSection sect2 = builder.withTitle("Section2").build();
        CriteriaSection sect3 = builder.withTitle("Section3").build();

        EventForm eventForm = scenario.anEventForm().withSections(sect1,sect2,sect3).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Test
    public void reorder_criteria_section_to_top() {
        ManageEventTypesPage eventTypesPage = navigateToEventForm();

        eventTypesPage.clickReorderSections();
        eventTypesPage.dragSectionToPosition("Section3", 1);

        List<String> sectionNames = eventTypesPage.getCriteriaSectionNames();

        assertEquals(Arrays.asList("Section3", "Section1", "Section2"), sectionNames);
    }

    @Test
    public void reorder_criteria_section_to_middle() {
        ManageEventTypesPage eventTypesPage = navigateToEventForm();

        eventTypesPage.clickReorderSections();
        eventTypesPage.dragSectionToPosition("Section3", 2);

        List<String> sectionNames = eventTypesPage.getCriteriaSectionNames();

        assertEquals(Arrays.asList("Section1", "Section3", "Section2"), sectionNames);
    }

    @Test
    public void reorder_criteria_to_top() {
        ManageEventTypesPage eventTypesPage = navigateToEventForm();
        eventTypesPage.clickCriteriaSection("Section1");
        eventTypesPage.clickReorderCriteria();

        eventTypesPage.dragCriteriaToPosition("Criteria2", 1);
        List<String> criteriaNames = eventTypesPage.getCriteriaNames();

        assertEquals(Arrays.asList("Criteria2", "Criteria1", "Criteria3"), criteriaNames);
    }

    private ManageEventTypesPage navigateToEventForm() {
        ManageEventTypesPage eventTypesPage = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes();
        eventTypesPage.clickEditEventType("Test type");
        eventTypesPage.clickEventFormTab();
        return eventTypesPage;
    }

}
