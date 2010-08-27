package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ManageProductTypesTest extends ManageProductTypesTestCase {
	
	@Test
	public void test_add_product_type_and_view_details() throws Exception {
		addTestProductType();
		
		assertTrue(page.getProductTypes().contains(TEST_PRODUCT_TYPE_NAME));
		
		page.clickProductType(TEST_PRODUCT_TYPE_NAME);
		
		assertEquals(TEST_PRODUCT_TYPE_NAME, page.getViewName());
		assertEquals(TEST_PRODUCT_TYPE_WARNINGS, page.getViewWarnings());
		assertEquals(TEST_PRODUCT_TYPE_INSTRUCTIONS, page.getViewInstructions());
		assertEquals("Yes", page.getViewHasManufacturerCertificate());
		assertEquals(TEST_PRODUCT_TYPE_MANUFACTURER_CERTIFICATE_TEXT, page.getViewManufacturerCertificateText());
		assertEquals(TEST_PRODUCT_TYPE_PRODUCT_DESCRIPTION_TEMPLATE, page.getViewProductDescriptionTemplate());
	}

	@Test
	public void test_add_product_type_missing_name_validation_error() throws Exception {
		page.clickAddTab();
		page.saveProductType();
		List<String> errors = page.getFormErrorMessages();
		assertEquals(1, errors.size());
		assertEquals("Product Type name is required.", errors.get(0));
	}
	
	@Test
	public void test_copy_product_type() throws Exception {
		addTestProductType();
		page.clickCopyProductType(TEST_PRODUCT_TYPE_NAME);
		
		assertEquals("Copied product name should be blank in form", "", page.getEditName());
		assertEquals(TEST_PRODUCT_TYPE_WARNINGS, page.getEditWarnings());
		assertEquals(TEST_PRODUCT_TYPE_INSTRUCTIONS, page.getEditInstructions());
		assertEquals(TEST_PRODUCT_TYPE_CAUTIONS_URL, page.getEditCautionsUrl());
		assertTrue(page.getEditHasManufacturerCertificate());
		assertEquals(TEST_PRODUCT_TYPE_MANUFACTURER_CERTIFICATE_TEXT, page.getEditManufacturerCertificateText());
		assertEquals(TEST_PRODUCT_TYPE_PRODUCT_DESCRIPTION_TEMPLATE, page.getEditProductDescriptionTemplate());
	}

}
