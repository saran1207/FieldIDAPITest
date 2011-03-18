package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.EventForm;
import com.n4systems.fieldid.selenium.datatypes.EventFormObservations;
import com.n4systems.fieldid.selenium.datatypes.EventFormSection;
import com.n4systems.fieldid.selenium.datatypes.OneClickEventFormCriteria;
import com.n4systems.fieldid.selenium.lib.PageErrorException;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeAddEditPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeAssetTypeAssociationsPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeFormPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeViewAllPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeViewPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.persistence.builder.SimpleEventBuilder;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ManageEventTypesTest extends PageNavigatingTestCase<EventTypeViewAllPage> {
	
	protected static final String TEST_ASSET_TYPE_NAME = "TestAssetType";
	protected static final String TEST_EVENT_TYPE_NAME = "Simple Event Type";
	
	@Override
	public void setupScenario(Scenario scenario) {
		PrimaryOrg defaultPrimaryOrg = scenario.primaryOrgFor("test1");
		
		defaultPrimaryOrg.setExtendedFeatures(setOf(ExtendedFeature.AssignedTo, ExtendedFeature.ProofTestIntegration));
		scenario.save(defaultPrimaryOrg);
		
		EventTypeGroup maintenanceGroup = scenario.anEventTypeGroup()
		    .forTenant(scenario.defaultTenant())
		    .withName("Maintenance")
		    .build();
		
		SimpleEventBuilder.aSimpleEvent(scenario).build();
		
		scenario.anEventType()
					.named(TEST_EVENT_TYPE_NAME)
					.withGroup(maintenanceGroup)
					.build();
		
	    scenario.anAssetType()
           .named(TEST_ASSET_TYPE_NAME)
           .build();
	}
	
	@Override
	protected EventTypeViewAllPage navigateToPage() {
		return startAsCompany("test1").systemLogin().clickSetupLink().clickManageEventTypes();
	}
	
	@Test
	public void test_view_event_type_loads() throws Exception {
		page.clickEventTypeName(TEST_EVENT_TYPE_NAME);
	}
	
	@Test
	public void test_edit_event_type_from_list_loads() throws Exception {
		page.clickEventTypeEdit(TEST_EVENT_TYPE_NAME);
	}

	@Test
	public void test_edit_event_type_tab_loads() throws Exception {
		page.clickEventTypeName(TEST_EVENT_TYPE_NAME).clickEditTab();
	}
	
	@Test
	public void test_view_event_form_loads() throws Exception {
		page.clickEventTypeName(TEST_EVENT_TYPE_NAME).clickEventFormTab();
	}

	@Test
	public void test_import_event_type_loads() throws Exception {
		page.clickEventTypeName(TEST_EVENT_TYPE_NAME).clickImportTab();
	}
	
	@Test(expected=PageErrorException.class)
	public void test_add_fails_with_empty_name_field() throws Exception {
		page.clickAddTab().clickSave();
	}
	
	@Test(expected=PageErrorException.class)
	public void test_add_save_and_add_fails_with_empty_name_field() throws Exception {
		page.clickAddTab().clickSaveAndAdd();
	}
	
	@Test
	public void test_add_event_type_cancel() throws Exception {
		page.clickAddTab().clickCancel();
	}

	@Test
	public void test_add_and_delete_event_type() throws Exception {
		EventTypeAddEditPage addPage = page.clickAddTab();
		addPage.setFormFields(getEventType("test_add_and_delete_event_type"));
		
		EventTypeViewPage viewPage = addPage.clickSave();
		viewPage.verifyEventTypeSaved();
		
		EventTypeViewAllPage viewAllPage = viewPage.clickEditTab().clickDelete().clickConfirmDelete();
		viewAllPage.verifyTypeDeleted();
	}

	@Test
	public void test_add_event_type_and_event_form() throws Exception {
		EventTypeAddEditPage addEdit = page.clickAddTab();
		addEdit.setFormFields(getEventType("test_add_event_type_and_event_form"));
		EventTypeFormPage formPage = addEdit.clickSaveAndAdd();
		formPage.verifyEventTypeSaved();
		formPage.setEventFormFields(createTestEventForm());
		formPage.clickSaveEventForm();
		formPage.verifyEventFormSaved();
	}
	
	@Test
	public void test_copy_existing_event_type() throws Exception {
		page.clickEventTypeCopy(TEST_EVENT_TYPE_NAME).validateCopiedEvent("Simple Event Type - 1");
	}

	@Test
	public void test_asset_type_associations() throws Exception {
		EventTypeAssetTypeAssociationsPage associationsPage = page.clickEventTypeName(TEST_EVENT_TYPE_NAME).clickAssetTypeAssociationsTab();
		associationsPage.selectAllAssetTypes();
		associationsPage.clickSave();
		associationsPage.clickSetupLink().clickAssetTypes().clickAssetType(TEST_ASSET_TYPE_NAME).clickEventTypeAssociationsTab();
		assertTrue("Event Type wasn't properly associated to Asset Type", selenium.isElementPresent("//tr[@class='selectedEvent']/td[contains(text(),'" + TEST_EVENT_TYPE_NAME + "')]"));
	}

	private com.n4systems.fieldid.selenium.datatypes.EventType getEventType(String name) {
		com.n4systems.fieldid.selenium.datatypes.EventType eventType = new com.n4systems.fieldid.selenium.datatypes.EventType(name);
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
		OneClickEventFormCriteria criteria1 = new OneClickEventFormCriteria("Criteria1", "Pass, Fail");
		criteria1.setSetsResult(true);
		
		EventFormObservations observation1 = new EventFormObservations();
		observation1.setRecommendations(Arrays.asList("recommendation1", "recommendation2"));
		observation1.setDeficiencies(Arrays.asList("deficiency1", "deficiency2"));					
		criteria1.setObservations(observation1);

		OneClickEventFormCriteria criteria2 = new OneClickEventFormCriteria("Criteria2", "Pass, Fail");
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
