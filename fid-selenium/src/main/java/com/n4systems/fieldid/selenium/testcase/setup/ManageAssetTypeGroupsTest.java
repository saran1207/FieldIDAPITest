package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypeGroupsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;

public class ManageAssetTypeGroupsTest extends FieldIDTestCase {
	
	private static final String TEST_GROUP_NAME = "TestGroup";
	private static final String TEST_GROUP_NAME_2 = "TestGroup2";

	ManageAssetTypeGroupsPage typeGroupsPage;
	
	@Before
	public void navigate() {
		typeGroupsPage = startAsCompany("test1").login().clickSetupLink().clickAssetTypeGroups();
	}

	@Test
	public void adding_group_with_valid_name_should_succeed() throws Exception {
		addTestGroup(TEST_GROUP_NAME);
		
		assertEquals("Save should have succeeded", 0, typeGroupsPage.getFormErrorMessages().size());
		assertTrue(typeGroupsPage.getAssetTypeGroups().contains(TEST_GROUP_NAME));
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
		
		assertFalse("Old name should not be present in list", typeGroupsPage.getAssetTypeGroups().contains(TEST_GROUP_NAME));
		assertTrue("New name should be present in list", typeGroupsPage.getAssetTypeGroups().contains(TEST_GROUP_NAME_2));
	}

	@Test
	public void adding_asset_to_type_should_warn_when_deleting() throws Exception {
		addTestGroup(TEST_GROUP_NAME);
		ManageAssetTypesPage assetTypesPage = typeGroupsPage.clickSetupLink().clickAssetTypes();
		
		String firstAssetType = assetTypesPage.getAssetTypes().get(0);
		assetTypesPage.clickEditAssetType(firstAssetType);
		assetTypesPage.selectAssetTypeGroup(TEST_GROUP_NAME);
		assetTypesPage.clickSaveAssetType();
		
		typeGroupsPage = assetTypesPage.clickSetupLink().clickAssetTypeGroups();
		typeGroupsPage.clickDeleteGroup(TEST_GROUP_NAME);
		assertEquals(1, typeGroupsPage.getWarningNumberOfAttachedAssetTypes());
		typeGroupsPage.confirmDeleteGroup();
	}
	
	@Test
	public void adding_and_deleting_group_with_valid_name_should_succeed() throws Exception {
		addTestGroup(TEST_GROUP_NAME);
		
		assertEquals("Save should have succeeded", 0, typeGroupsPage.getFormErrorMessages().size());
		assertTrue(typeGroupsPage.getAssetTypeGroups().contains(TEST_GROUP_NAME));
		
		boolean deleted = deleteAssetTypeGroup(TEST_GROUP_NAME);
        assertTrue("Delete should have succeeded", deleted);
		assertEquals("Delete should have succeeded", 0, typeGroupsPage.getFormErrorMessages().size());
		assertFalse(typeGroupsPage.getAssetTypeGroups().contains(TEST_GROUP_NAME));
	}

	private void addTestGroup(String groupName) {
		typeGroupsPage.clickAddTab();
		typeGroupsPage.enterName(groupName);
		typeGroupsPage.clickSave();
		typeGroupsPage.clickViewAllTab();
	}

	private boolean deleteAssetTypeGroup(String typeName) {
		for (String assetType : typeGroupsPage.getAssetTypeGroups()) {
			if (assetType.equals(typeName)) {
				typeGroupsPage.deleteAssetTypeGroup(assetType);
                return true;
			}
		}
        return false;
    }

}
