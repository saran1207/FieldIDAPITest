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

        SelectCriteria crit1 = critBuilder.withText("select1").build();
        SelectCriteria crit2 = critBuilder.withText("select2").build();
        SelectCriteria crit3 = critBuilder.withText("select3").build();

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
    public void view_select_criteria() {
        page.clickCriteriaSection("Main Section");
        assertEquals(Arrays.asList("select1", "select2", "select3"), page.getCriteriaNames());

        assertEquals("Select Box", page.getTypeForCriteria("select1"));
        assertEquals("Select Box", page.getTypeForCriteria("select2"));
        assertEquals("Select Box", page.getTypeForCriteria("select3"));
    }

}
