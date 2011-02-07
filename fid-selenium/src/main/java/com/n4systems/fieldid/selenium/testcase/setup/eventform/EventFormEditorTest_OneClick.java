package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.StateSet;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EventFormEditorTest_OneClick extends PageNavigatingTestCase<ManageEventTypesPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        StateSet passFail = scenario.buttonGroup(scenario.defaultTenant(), "Pass, Fail");

        Criteria oneClick1 = scenario.aOneClickCriteria().withText("oneclick1").withStateSet(passFail).build();
        Criteria oneClick2 = scenario.aOneClickCriteria().withText("oneclick2").withStateSet(passFail).build();

        CriteriaSection mainSection = scenario.aCriteriaSection()
                .withTitle("Main section")
                .withCriteria(oneClick1, oneClick2)
                .build();

        EventForm eventForm = scenario.anEventForm().withSections(mainSection).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Override
    protected ManageEventTypesPage navigateToPage() {
        ManageEventTypesPage eventTypesPage = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes();
        eventTypesPage.clickEditEventType("Test type");
        eventTypesPage.clickEventFormTab();
        return eventTypesPage;
    }

    @Test
    public void view_one_click_criteria() {
        page.clickCriteriaSection("Main section");
        assertEquals(Arrays.asList("oneclick1", "oneclick2"), page.getCriteriaNames());

        assertEquals("One-Click Button", page.getTypeForCriteria("oneclick1"));
        assertEquals("One-Click Button", page.getTypeForCriteria("oneclick2"));
    }

}
