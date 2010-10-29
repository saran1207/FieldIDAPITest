package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.InspectionForm;
import com.n4systems.fieldid.selenium.datatypes.InspectionFormCriteria;
import com.n4systems.fieldid.selenium.datatypes.InspectionFormObservations;
import com.n4systems.fieldid.selenium.datatypes.InspectionFormSection;
import com.n4systems.fieldid.selenium.datatypes.InspectionType;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypesPage;

public class ManageInspectionTypesTest extends FieldIDTestCase {
	
	private static final String TEST_INSPECTION_NAME = "Selenium Test";
	
	ManageEventTypesPage manageEventTypesPage;
	
	@Before
	public void setUp() {
		manageEventTypesPage = start().systemLogin().clickSetupLink().clickManageEventTypes();
	}
	
	@Test
	public void test_view_all_inspection_types() throws Exception {
		assertEquals("View All", manageEventTypesPage.getCurrentTab());
	}
	
	@Test
	public void test_view_inspection_type() throws Exception {
		String inspectionName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(inspectionName));
	}
	
	@Test
	public void test_edit_inspection_type_from_list() throws Exception {
		String inspectionName = manageEventTypesPage.clickFirstListItemEdit();
		assertEquals("Edit", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(inspectionName));
	}

	@Test
	public void test_edit_inspection_type_from_tab() throws Exception {
		String inspectionName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickEditTab();
		assertEquals("Edit", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(inspectionName));
	}
	
	@Test
	public void test_view_inspection_form() throws Exception {
		String inspectionName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickEventFormTab();
		assertEquals("Inspection Form", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(inspectionName));
	}

	@Test
	public void test_import_inspection_type() throws Exception {
		String inspectionName = manageEventTypesPage.clickFirstListItem();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickImportTab();
		assertEquals("Import", manageEventTypesPage.getCurrentTab());
		assertTrue(manageEventTypesPage.checkPageHeaderText(inspectionName));
	}
	
	@Test
	public void test_add_inspection_type_save_with_error() throws Exception {
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickSave();
		assertEquals(1, manageEventTypesPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_inspection_type_save_and_add_with_error() throws Exception {
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickSaveAndAdd();
		assertEquals(1, manageEventTypesPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_inspection_type_cancel() throws Exception {
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.clickCancel();
		assertEquals("View All", manageEventTypesPage.getCurrentTab());
	}

	@Test
	public void test_add_and_delete_inspection_type() throws Exception {
		deleteTestInspection(TEST_INSPECTION_NAME);
		
		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		InspectionType inspectionType = getInspectionType();
		manageEventTypesPage.setFormFields(inspectionType);
		manageEventTypesPage.clickSave();
		manageEventTypesPage.verifyInspectionTypeSaved();
		assertEquals("View", manageEventTypesPage.getCurrentTab());
		
		deleteTestInspection(TEST_INSPECTION_NAME);
	}

	@Test
	public void test_add_inspection_type_and_inspection_form() throws Exception {
		deleteTestInspection(TEST_INSPECTION_NAME);

		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		InspectionType inspectionType = getInspectionType();
		manageEventTypesPage.setFormFields(inspectionType);
		manageEventTypesPage.clickSaveAndAdd();
		manageEventTypesPage.verifyInspectionTypeSaved();

		assertEquals("Inspection Form", manageEventTypesPage.getCurrentTab());
		manageEventTypesPage.setInpectionFormFields(getInspectionForm());
		manageEventTypesPage.clickSaveInspectionForm();
		manageEventTypesPage.verifyInspectionFormSaved();
		
		deleteTestInspection(TEST_INSPECTION_NAME);
	}
	
	@Test
	public void test_add_inspection_form_with_errors() throws Exception {
		deleteTestInspection(TEST_INSPECTION_NAME);

		manageEventTypesPage.clickAddTab();
		assertEquals("Add", manageEventTypesPage.getCurrentTab());
		InspectionType inspectionType = getInspectionType();
		manageEventTypesPage.setFormFields(inspectionType);
		manageEventTypesPage.clickSaveAndAdd();
		manageEventTypesPage.verifyInspectionTypeSaved();

		assertEquals("Inspection Form", manageEventTypesPage.getCurrentTab());
		InspectionForm badForm = new InspectionForm();
		badForm.addSection(0, new InspectionFormSection(""));
		manageEventTypesPage.setInpectionFormFields(badForm);
		manageEventTypesPage.clickSaveInspectionForm();
		assertEquals(3, manageEventTypesPage.getFormErrorMessages().size());
		
		deleteTestInspection(TEST_INSPECTION_NAME);
	}
	
	@Test
	public void test_copy_existing_inpection_type() throws Exception {
		String inspectionName = manageEventTypesPage.clickFirstListItemCopy() + " - 1";
		manageEventTypesPage.validateCopiedInspection(inspectionName);
		
		deleteTestInspection(inspectionName);
	}
	
	private void deleteTestInspection(String name) {
		if(manageEventTypesPage.listItemExists(name)) {
			manageEventTypesPage.clickListItem(name);
			manageEventTypesPage.clickEditTab();
			assertEquals("Edit", manageEventTypesPage.getCurrentTab());
			manageEventTypesPage.clickDelete();
			manageEventTypesPage.clickConfirmDelete();
			assertEquals("View All", manageEventTypesPage.getCurrentTab());
			manageEventTypesPage.verifyInspectionFormDeleted();
		}

	}

	private InspectionType getInspectionType() {
		InspectionType inspectionType = new InspectionType(TEST_INSPECTION_NAME);
		inspectionType.setGroup("Maintenance");
		inspectionType.setPrintable(true);
		inspectionType.setMasterInspection(false);
		inspectionType.setAssignedToAvailable(true);
		inspectionType.setSupportedProofTestTypes(Arrays.asList(new String[]{"ROBERTS", "CHANT"}));
		inspectionType.setInspectionAttributes(Arrays.asList(new String[]{"Attribute1", "Attribute2"}));
		return inspectionType;
	}

	private InspectionForm getInspectionForm() {
		InspectionForm form = new InspectionForm();
		
		InspectionFormSection section1 = new InspectionFormSection("Section1");
		InspectionFormCriteria criteria1 = new InspectionFormCriteria("Criteria1", "Pass,Fail");
		criteria1.setSetsResult(true);
		
		InspectionFormObservations observation1 = new InspectionFormObservations();
		observation1.setRecommendations(Arrays.asList(new String [] {"recommendation1", "recommendation2"}));
		observation1.setDeficiencies(Arrays.asList(new String [] {"deficiency1", "deficiency2"}));					
		criteria1.setObservations(observation1);

		InspectionFormCriteria criteria2 = new InspectionFormCriteria("Criteria2", "Acceptable");
		InspectionFormObservations observation2 = new InspectionFormObservations();
		observation2.setRecommendations(Arrays.asList(new String [] {"recommendation3"}));
		observation2.setDeficiencies(Arrays.asList(new String [] {"deficiency3"}));
		criteria2.setObservations(observation2);		

		section1.setCriteria(Arrays.asList(new InspectionFormCriteria [] {criteria1, criteria2}));
		
		InspectionFormSection section2 = new InspectionFormSection("Section2");

		section2.setCriteria(Arrays.asList(new InspectionFormCriteria [] {criteria2}));		
		
		form.setSections(Arrays.asList(new InspectionFormSection [] {section1, section2}));
		
		return form;
	}

}
