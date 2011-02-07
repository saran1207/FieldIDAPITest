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
        Criteria text1 = scenario.aTextFieldCriteria().withText("text1").build();
        Criteria text2 = scenario.aTextFieldCriteria().withText("text2").build();

        CriteriaSection mainSection = scenario.aCriteriaSection()
                .withTitle("Main section")
                .withCriteria(text1, text2)
                .build();

        EventForm eventForm = scenario.anEventForm().withSections(mainSection).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Test
    public void view_text_fields() {
        page.clickCriteriaSection("Main section");
        assertEquals(Arrays.asList("text1", "text2"), page.getCriteriaNames());
        assertEquals("Text Field", page.getTypeForCriteria("text1"));
        assertEquals("Text Field", page.getTypeForCriteria("text2"));
    }

    @Override
    protected ManageEventTypesPage navigateToPage() {
        ManageEventTypesPage page = startAsCompany("test1").login().clickSetupLink().clickManageEventTypes();
        page.clickEditEventType("Test type");
        page.clickEventFormTab();
        return page;
    }

}
