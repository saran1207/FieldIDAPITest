package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageProductTypeGroupsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageProductTypesPage;

public class ManageProductTypeGroupsTest extends FieldIDTestCase {
	
	private static final String TEST_GROUP_NAME = "TestGroup";
	private static final String TEST_GROUP_NAME_2 = "TestGroup2";

	ManageProductTypeGroupsPage typeGroupsPage;
	
	@Before
	public void navigate() {
		typeGroupsPage = start().login().clickSetupLink().clickManageProductTypeGroups();
		deleteProductTypeIfExists(TEST_GROUP_NAME);
		deleteProductTypeIfExists(TEST_GROUP_NAME_2);
	}

	@Test
	public void adding_group_with_valid_name_should_succeed() throws Exception {
		addTestGroup(TEST_GROUP_NAME);
		
		assertEquals("Save should have succeeded", 0, typeGroupsPage.getFormErrorMessages().size());
		assertTrue(typeGroupsPage.getProductTypeGroups().contains(TEST_GROUP_NAME));
	}
	
	@Test
	public void adding_group_with_blank_name_should_give_validation_error() throws Exception {
		typeGroupsPage.clickAddTab();
		typeGroupsPage.clickSave();
		
		List<String> formErrorMessages = typeGroupsPage.getFormErrorMessages();
		assertEquals("Should be a validation error", 1, formErrorMessages.size());
		assertEquals("Name is a required field.", formErrorMessages.get(0));
	}
	
	@Test
	public void editing_group_to_valid_name_should_succeed() throws Exception {
		addTestGroup(TEST_GROUP_NAME);
		
		typeGroupsPage.clickEditGroup(TEST_GROUP_NAME);
		typeGroupsPage.enterName(TEST_GROUP_NAME_2);
		typeGroupsPage.clickSave();
		typeGroupsPage.clickViewAllTab();
		
		assertFalse("Old name should not be present in list", typeGroupsPage.getProductTypeGroups().contains(TEST_GROUP_NAME));
		assertTrue("New name should be present in list", typeGroupsPage.getProductTypeGroups().contains(TEST_GROUP_NAME_2));
	}

	private void addTestGroup(String groupName) {
		typeGroupsPage.clickAddTab();
		typeGroupsPage.enterName(TEST_GROUP_NAME);
		typeGroupsPage.clickSave();
		typeGroupsPage.clickViewAllTab();
	}
	
	@Test
	public void adding_product_to_type_should_warn_when_deleting() throws Exception {
		addTestGroup(TEST_GROUP_NAME);
		ManageProductTypesPage productTypesPage = typeGroupsPage.clickSetupLink().clickManageProductTypes();
		
		String firstProductType = productTypesPage.getProductTypes().get(0);
		productTypesPage.clickEditProductType(firstProductType);
		productTypesPage.selectProductTypeGroup(TEST_GROUP_NAME);
		productTypesPage.saveProductType();
		
		typeGroupsPage = productTypesPage.clickSetupLink().clickManageProductTypeGroups();
		typeGroupsPage.clickDeleteGroup(TEST_GROUP_NAME);
		assertEquals(1, typeGroupsPage.getWarningNumberOfAttachedProductTypes());
		typeGroupsPage.confirmDeleteGroup();
	}
	
	private void deleteProductTypeIfExists(String typeName) {
		for (String productType : typeGroupsPage.getProductTypeGroups()) {
			if (productType.equals(typeName)) {
				typeGroupsPage.deleteProductTypeGroup(productType);
			}
		}
	}

}
