package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import java.util.Arrays;

import com.n4systems.fieldid.selenium.datatypes.EventForm;
import com.n4systems.fieldid.selenium.datatypes.OneClickEventFormCriteria;
import com.n4systems.fieldid.selenium.datatypes.EventType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.EventFormObservations;
import com.n4systems.fieldid.selenium.datatypes.EventFormSection;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;

public class ManageEventTypesTest extends FieldIDTestCase {
	
	private static final String TEST_EVENT_NAME = "Selenium Test";
	
	ManageEventTypesPage manageEventTypesPage;
	
	@Before
	public void setUp() {
		manageEventTypesPage = start().systemLogin().clickSetupLink().clickManageEventTypes();
	}
	
	@Test
	public void test_view_all_event_types() throws Exception {
		assertEquals("View All", manageEventTypesPage.getCurrentTab());
	}
	
	@Test
	public void test_view_event_type() throws Exception {
		String eventName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(eventName));
	}
	
	@Test
	public void test_edit_event_type_from_list() throws Exception {
		String eventName = manageEventTypesPage.clickFirstListItemEdit();
		assertEquals("Edit", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(eventName));
	}

	@Test
	public void test_edit_event_type_from_tab() throws Exception {
		String eventName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickEditTab();
		assertEquals("Edit", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(eventName));
	}
	
	@Test
	public void test_view_event_form() throws Exception {
		String eventName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickEventFormTab();
		assertEquals("Event Form", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(eventName));
	}

	@Test
	public void test_import_event_type() throws Exception {
		String eventName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickImportTab();
		assertEquals("Import", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(eventName));
	}
	
	@Test
	public void test_add_event_type_save_with_error() throws Exception {
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickSave();
		assertEquals(1, manageEventTypesPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_event_type_save_and_add_with_error() throws Exception {
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickSaveAndAdd();
		assertEquals(1, manageEventTypesPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_event_type_cancel() throws Exception {
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickCancel();
		assertEquals("View All", manageEventTypesPage.getCurrentTab());
	}

	@Test
	public void test_add_and_delete_event_type() throws Exception {
		deleteTestEvent(TEST_EVENT_NAME);
		
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		EventType eventType = getEventType();
		manageEventTypesPage.setFormFields(eventType);
		manageEventTypesPage.clickSave();
		manageEventTypesPage.verifyEventTypeSaved();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		
		deleteTestEvent(TEST_EVENT_NAME);
	}

	@Test
	public void test_add_event_type_and_event_form() throws Exception {
		deleteTestEvent(TEST_EVENT_NAME);

		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		EventType eventType = getEventType();
		manageEventTypesPage.setFormFields(eventType);
		manageEventTypesPage.clickSaveAndAdd();
		manageEventTypesPage.verifyEventTypeSaved();

		assertEquals("Event Form", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.setEventFormFields(createTestEventForm());
		manageEventTypesPage.clickSaveEventForm();
		manageEventTypesPage.verifyEventFormSaved();

		deleteTestEvent(TEST_EVENT_NAME);
	}
	
	@Test
    @Ignore //TODO: Validation workflow for event form to be decided
	public void test_add_event_form_with_errors() throws Exception {
		deleteTestEvent(TEST_EVENT_NAME);

		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		EventType eventType = getEventType();
		manageEventTypesPage.setFormFields(eventType);
		manageEventTypesPage.clickSaveAndAdd();
		manageEventTypesPage.verifyEventTypeSaved();

		assertEquals("Event Form", manageEventTypesPage.getCurrentTab());
		EventForm badForm = new EventForm();
		badForm.addSection(0, new EventFormSection(""));
		manageEventTypesPage.setEventFormFields(badForm);
		manageEventTypesPage.clickSaveEventForm();
		assertEquals(3, manageEventTypesPage.getFormErrorMessages().size());
		
		deleteTestEvent(TEST_EVENT_NAME);
	}
	
	@Test
	public void test_copy_existing_event_type() throws Exception {
		String eventName = manageEventTypesPage.clickFirstListItemCopy() + " - 1";
		manageEventTypesPage.validateCopiedEvent(eventName);
		
		deleteTestEvent(eventName);
	}
	
	private void deleteTestEvent(String name) {
		if(manageEventTypesPage.listItemExists(name)) {
			manageEventTypesPage.clickListItem(name);
			manageEventTypesPage.clickEditTab();
			assertEquals("Edit", manageEventTypesPage.getCurrentTab());
			manageEventTypesPage.clickDelete();
			manageEventTypesPage.clickConfirmDelete();
			assertEquals("View All", manageEventTypesPage.getCurrentTab());
			manageEventTypesPage.verifyEventFormDeleted();
		}

	}

	private EventType getEventType() {
		EventType eventType = new EventType(TEST_EVENT_NAME);
		eventType.setGroup("Maintenance");
		eventType.setPrintable(true);
		eventType.setMasterEvent(false);
		eventType.setAssignedToAvailable(true);
		eventType.setSupportedProofTestTypes(Arrays.asList("ROBERTS", "CHANT"));
		eventType.setEventAttributes(Arrays.asList("Attribute1", "Attribute2"));
		return eventType;
	}

	private EventForm createTestEventForm() {
		EventForm form = new EventForm();
		
		EventFormSection section1 = new EventFormSection("Section1");
		OneClickEventFormCriteria criteria1 = new OneClickEventFormCriteria("Criteria1", "Pass,Fail");
		criteria1.setSetsResult(true);
		
		EventFormObservations observation1 = new EventFormObservations();
		observation1.setRecommendations(Arrays.asList("recommendation1", "recommendation2"));
		observation1.setDeficiencies(Arrays.asList("deficiency1", "deficiency2"));					
		criteria1.setObservations(observation1);

		OneClickEventFormCriteria criteria2 = new OneClickEventFormCriteria("Criteria2", "Acceptable");
		EventFormObservations observation2 = new EventFormObservations();
		observation2.setRecommendations(Arrays.asList("recommendation3"));
		observation2.setDeficiencies(Arrays.asList("deficiency3"));
		criteria2.setObservations(observation2);		

		section1.setCriteria(Arrays.asList(criteria1, criteria2));
		
		EventFormSection section2 = new EventFormSection("Section2");

		section2.setCriteria(Arrays.asList(criteria2));
		
		form.setSections(Arrays.asList(section1, section2));
		
		return form;
	}

}
