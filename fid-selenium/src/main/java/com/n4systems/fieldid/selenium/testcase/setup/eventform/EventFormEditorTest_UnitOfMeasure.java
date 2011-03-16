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

public class EventFormEditorTest_UnitOfMeasure extends PageNavigatingTestCase<EventTypeFormPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        Criteria unit1 = scenario.aUnitOfMeasureCriteria()
                .withDisplayText("unit1")
                .primaryUnit(scenario.unitOfMeasure("Feet")).build();

        Criteria unit2 = scenario.aUnitOfMeasureCriteria()
                .withDisplayText("unit2")
                .primaryUnit(scenario.unitOfMeasure("Feet"))
                .secondaryUnit(scenario.unitOfMeasure("Inches"))
                .build();


        CriteriaSection mainSection = scenario.aCriteriaSection()
                .withTitle("Main section")
                .withCriteria(unit1, unit2)
                .build();

        CriteriaSection otherSection = scenario.aCriteriaSection()
                .withTitle("Other sect" +
                        "ion")
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
    public void view_existing_unit_of_measure_criteria_in_editor() {
        page.clickCriteriaSection("Main section");
        assertEquals(Arrays.asList("unit1", "unit2"), page.getCriteriaNames());
        assertEquals("Unit of Measure", page.getTypeForCriteria("unit1"));
        assertEquals("Unit of Measure", page.getTypeForCriteria("unit2"));

        page.clickCriteria("unit1");
        assertEquals("Feet", page.getSelectedPrimaryUnitOfMeasure());
        assertEquals("", page.getSelectedSecondaryUnitOfMeasure());

        page.clickCriteria("unit2");
        assertEquals("Feet", page.getSelectedPrimaryUnitOfMeasure());
        assertEquals("Inches", page.getSelectedSecondaryUnitOfMeasure());
    }

    @Test
    public void add_new_unit_of_measure_criteria_should_save_and_persist() {
        page.clickCriteriaSection("Other section");
        page.addCriteriaNamed("Height", "Unit of Measure");

        page.selectPrimaryUnitOfMeasure("Feet");
        page.selectSecondaryUnitOfMeasure("Inches");

        EventTypeFormPage newFormPage = page.clickSaveAndFinishEventForm().clickEventFormTab();
        newFormPage.clickCriteriaSection("Other section");
        newFormPage.clickCriteria("Height");

        assertEquals("Feet", newFormPage.getSelectedPrimaryUnitOfMeasure());
        assertEquals("Inches", newFormPage.getSelectedSecondaryUnitOfMeasure());
    }

}
