package com.n4systems.fieldid.selenium.testcase.setup.eventform;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeFormPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.StateSet;

public class EventFormEditorTest_OneClick extends PageNavigatingTestCase<EventTypeFormPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        StateSet passFail = scenario.buttonGroup(scenario.defaultTenant(), "Pass, Fail");
        scenario.aStateSet().named("Button Group 1").states(scenario.failState().build(), scenario.passState().build()).build();
        scenario.aStateSet().named("Button Group 2").states(scenario.naState().build(), scenario.failState().build()).build();

        Criteria oneClick1 = scenario.aOneClickCriteria().withDisplayText("oneclick1").withStateSet(passFail).build();
        Criteria oneClick2 = scenario.aOneClickCriteria().withDisplayText("oneclick2").withStateSet(passFail).build();

        CriteriaSection mainSection = scenario.aCriteriaSection()
                .withTitle("Main section")
                .withCriteria(oneClick1, oneClick2)
                .build();

        CriteriaSection otherSection = scenario.aCriteriaSection()
                .withTitle("Other section")
                .build();

        EventForm eventForm = scenario.anEventForm().withSections(mainSection, otherSection).build();

        scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test type").build();
    }

    @Override
    protected EventTypeFormPage navigateToPage() {
        return startAsCompany("test1").login().clickSetupLink().clickManageEventTypes().clickEventTypeName("Test type").clickEventFormTab();
    }

    @Test
    public void view_one_click_criteria() {
        page.clickCriteriaSection("Main section");
        assertEquals(Arrays.asList("oneclick1", "oneclick2"), page.getCriteriaNames());

        assertEquals("One-Click Button", page.getTypeForCriteria("oneclick1"));
        assertEquals("One-Click Button", page.getTypeForCriteria("oneclick2"));
    }

    @Test
    public void add_one_click_criteria() {
        page.clickCriteriaSection("Other section");
        assertEquals(0, page.getCriteriaNames().size());

        page.addCriteriaNamed("new one click", "One-Click Button");
        assertEquals("One-Click Button", page.getTypeForCriteria("new one click"));

        page.clickCriteria("new one click");
        assertFalse("Default one click criteria should not be result setting", page.isSelectedCriteriaResultSetting());
    }

    @Test
    public void changing_button_group_should_succeed_and_persist() {
        page.clickCriteriaSection("Main section");
        page.clickCriteria("oneclick1");
        page.selectButtonGroup("Button Group 1");
        page.clickCriteria("oneclick2");
        page.selectButtonGroup("Button Group 2");

        EventTypeFormPage newFormPage = page.clickSaveAndFinishEventForm().clickEventFormTab();
        newFormPage.clickCriteriaSection("Main section");
        newFormPage.clickCriteria("oneclick1");
        assertEquals("Button Group 1", newFormPage.getSelectedButtonGroup());
        page.clickCriteria("oneclick2");
        assertEquals("Button Group 2", newFormPage.getSelectedButtonGroup());
    }

    @Test
    public void changing_result_setting_should_succeed_and_persist() throws Exception {
        page.clickCriteriaSection("Main section");
        page.clickCriteria("oneclick1");
        assertFalse(page.isSelectedCriteriaResultSetting());
        page.setSelectedCriteriaResultSetting(true);
        assertTrue(page.isSelectedCriteriaResultSetting());

        // Save and return to event form
        EventTypeFormPage newFormPage = page.clickSaveAndFinishEventForm().clickEventFormTab();
        newFormPage.clickCriteriaSection("Main section");
        newFormPage.clickCriteria("oneclick1");
        assertTrue(newFormPage.isSelectedCriteriaResultSetting());
    }

}
