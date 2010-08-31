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
import com.n4systems.fieldid.selenium.pages.setup.ManageInspectionTypesPage;

public class ManageInspectionTypesTest extends FieldIDTestCase {
	
	private static final String TEST_INSPECTION_NAME = "Selenium Test";
	
	ManageInspectionTypesPage manageInspectionTypesPage;
	
	@Before
	public void setUp() {
		manageInspectionTypesPage = start().systemLogin().clickSetupLink().clickManageInspectionTypes();
	}
	
	@Test
	public void test_view_all_inspection_types() throws Exception {
		assertEquals("View All", manageInspectionTypesPage.getCurrentTab());
	}
	
	@Test
	public void test_view_inspection_type() throws Exception {
		String inspectionName = manageInspectionTypesPage.clickFirstListItem();
		assertEquals("View", manageInspectionTypesPage.getCurrentTab());
		assertTrue(manageInspectionTypesPage.checkPageHeaderText(inspectionName));
	}
	
	@Test
	public void test_edit_inspection_type_from_list() throws Exception {
		String inspectionName = manageInspectionTypesPage.clickFirstListItemEdit();
		assertEquals("Edit", manageInspectionTypesPage.getCurrentTab());
		assertTrue(manageInspectionTypesPage.checkPageHeaderText(inspectionName));		
	}

	@Test
	public void test_edit_inspection_type_from_tab() throws Exception {
		String inspectionName = manageInspectionTypesPage.clickFirstListItem();
		assertEquals("View", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickEditTab();
		assertEquals("Edit", manageInspectionTypesPage.getCurrentTab());
		assertTrue(manageInspectionTypesPage.checkPageHeaderText(inspectionName));		
	}
	
	@Test
	public void test_view_inspection_form() throws Exception {
		String inspectionName = manageInspectionTypesPage.clickFirstListItem();
		assertEquals("View", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickInspectionFormTab();
		assertEquals("Inspection Form", manageInspectionTypesPage.getCurrentTab());
		assertTrue(manageInspectionTypesPage.checkPageHeaderText(inspectionName));				
	}

	@Test
	public void test_import_inspection_type() throws Exception {
		String inspectionName = manageInspectionTypesPage.clickFirstListItem();
		assertEquals("View", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickImportTab();
		assertEquals("Import", manageInspectionTypesPage.getCurrentTab());
		assertTrue(manageInspectionTypesPage.checkPageHeaderText(inspectionName));				
	}
	
	@Test
	public void test_add_inspection_type_save_with_error() throws Exception {
		manageInspectionTypesPage.clickAddTab();
		assertEquals("Add", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickSave();
		assertEquals(1, manageInspectionTypesPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_inspection_type_save_and_add_with_error() throws Exception {
		manageInspectionTypesPage.clickAddTab();
		assertEquals("Add", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickSaveAndAdd();
		assertEquals(1, manageInspectionTypesPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_inspection_type_cancel() throws Exception {
		manageInspectionTypesPage.clickAddTab();
		assertEquals("Add", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickCancel();
		assertEquals("View All", manageInspectionTypesPage.getCurrentTab());
	}

	@Test
	public void test_add_and_delete_inspection_type() throws Exception {
		if(manageInspectionTypesPage.listItemExists(TEST_INSPECTION_NAME)) {
			manageInspectionTypesPage.clickListItem(TEST_INSPECTION_NAME);
			deleteTestInspection();
		}
		
		manageInspectionTypesPage.clickAddTab();
		assertEquals("Add", manageInspectionTypesPage.getCurrentTab());
		InspectionType inspectionType = getInspectionType();
		manageInspectionTypesPage.setFormFields(inspectionType);
		manageInspectionTypesPage.clickSave();
		manageInspectionTypesPage.verifyInspectionTypeSaved();		
		assertEquals("View", manageInspectionTypesPage.getCurrentTab());
		
		deleteTestInspection();
	}

	@Test
	public void test_add_inspection_type_and_inspection_form() throws Exception {
		if(manageInspectionTypesPage.listItemExists(TEST_INSPECTION_NAME)) {
			manageInspectionTypesPage.clickListItem(TEST_INSPECTION_NAME);
			deleteTestInspection();
		}

		manageInspectionTypesPage.clickAddTab();
		assertEquals("Add", manageInspectionTypesPage.getCurrentTab());
		InspectionType inspectionType = getInspectionType();
		manageInspectionTypesPage.setFormFields(inspectionType);
		manageInspectionTypesPage.clickSaveAndAdd();
		manageInspectionTypesPage.verifyInspectionTypeSaved();

		assertEquals("Inspection Form", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.setInpectionFormFields(getInspectionForm());
		manageInspectionTypesPage.clickSaveInspectionForm();
		manageInspectionTypesPage.verifyInspectionFormSaved();
		
		deleteTestInspection();
	}
	
	@Test
	public void test_add_inspection_form_with_errors() throws Exception {
		if(manageInspectionTypesPage.listItemExists(TEST_INSPECTION_NAME)) {
			manageInspectionTypesPage.clickListItem(TEST_INSPECTION_NAME);
			deleteTestInspection();
		}

		manageInspectionTypesPage.clickAddTab();
		assertEquals("Add", manageInspectionTypesPage.getCurrentTab());
		InspectionType inspectionType = getInspectionType();
		manageInspectionTypesPage.setFormFields(inspectionType);
		manageInspectionTypesPage.clickSaveAndAdd();
		manageInspectionTypesPage.verifyInspectionTypeSaved();

		assertEquals("Inspection Form", manageInspectionTypesPage.getCurrentTab());
		InspectionForm badForm = new InspectionForm();
		badForm.addSection(0, new InspectionFormSection(""));
		manageInspectionTypesPage.setInpectionFormFields(badForm);
		manageInspectionTypesPage.clickSaveInspectionForm();
		assertEquals(3, manageInspectionTypesPage.getFormErrorMessages().size());
		
		deleteTestInspection();
	}
	
	private void deleteTestInspection() {
		manageInspectionTypesPage.clickEditTab();
		assertEquals("Edit", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.clickDelete();
		manageInspectionTypesPage.clickConfirmDelete();		
		assertEquals("View All", manageInspectionTypesPage.getCurrentTab());
		manageInspectionTypesPage.verifyInspectionFormDeleted();		
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

	private InspectionForm getInspectionForm(){
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
