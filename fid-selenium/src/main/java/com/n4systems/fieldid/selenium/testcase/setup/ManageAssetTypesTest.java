package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ManageAssetTypesTest extends ManageAssetTypesTestCase {
	
	@Test
	public void test_add_asset_type_and_view_details() throws Exception {
		assertTrue(page.getAssetTypes().contains(TEST_ASSET_TYPE_NAME));
		
		page.clickAssetType(TEST_ASSET_TYPE_NAME);
		
		assertEquals(TEST_ASSET_TYPE_NAME, page.getViewName());
		assertEquals(TEST_ASSET_TYPE_WARNINGS, page.getViewWarnings());
		assertEquals(TEST_ASSET_TYPE_INSTRUCTIONS, page.getViewInstructions());
		assertEquals("Yes", page.getViewHasManufacturerCertificate());
		assertEquals(TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT, page.getViewManufacturerCertificateText());
		assertEquals(TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE, page.getViewAssetDescriptionTemplate());
	}

	@Test
	public void test_add_asset_type_missing_name_validation_error() throws Exception {
		page.clickAddTab();
		page.clickSaveAssetType();
		List<String> errors = page.getFormErrorMessages();
		assertEquals(1, errors.size());
		assertEquals("Asset Type name is required.", errors.get(0));
	}
	
	@Test
	public void test_copy_asset_type() throws Exception {
		page.clickCopyAssetType(TEST_ASSET_TYPE_NAME);
		
		assertEquals("Copied asset name should be blank in form", "", page.getEditName());
		assertEquals(TEST_ASSET_TYPE_WARNINGS, page.getEditWarnings());
		assertEquals(TEST_ASSET_TYPE_INSTRUCTIONS, page.getEditInstructions());
		assertEquals(TEST_ASSET_TYPE_CAUTIONS_URL, page.getEditCautionsUrl());
		assertTrue(page.getEditHasManufacturerCertificate());
		assertEquals(TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT, page.getEditManufacturerCertificateText());
		assertEquals(TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE, page.getEditAssetDescriptionTemplate());
	}

}
