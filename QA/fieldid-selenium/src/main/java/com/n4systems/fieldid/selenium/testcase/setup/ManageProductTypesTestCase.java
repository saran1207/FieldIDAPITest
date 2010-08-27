package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertFalse;

import org.junit.Before;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageProductTypesPage;

public abstract class ManageProductTypesTestCase extends PageNavigatingTestCase<ManageProductTypesPage> {
	
	protected static final String TEST_PRODUCT_TYPE_NAME = "Test";
	protected static final String TEST_PRODUCT_TYPE_WARNINGS = "Test Warnings";
	protected static final String TEST_PRODUCT_TYPE_INSTRUCTIONS = "Test Instructions";
	protected static final String TEST_PRODUCT_TYPE_CAUTIONS_URL = "http://cautions.url.com/";
	protected static final String TEST_PRODUCT_TYPE_MANUFACTURER_CERTIFICATE_TEXT = "Test Manufacturer Certificate Text";
	protected static final String TEST_PRODUCT_TYPE_PRODUCT_DESCRIPTION_TEMPLATE = "Test Product Description Template";

	@Override
	protected ManageProductTypesPage navigateToPage() {
		return startAsCompany("aacm").login().clickSetupLink().clickManageProductTypes();
	}
	
	@Before
	public void cleanUp() {
		// TODO: Remove when data setup is done.
		removeTestProductTypeIfNecessary(TEST_PRODUCT_TYPE_NAME);
	}
	
	protected void addTestProductType() {
		page.clickAddTab();
		page.enterName(TEST_PRODUCT_TYPE_NAME);
		page.enterWarnings(TEST_PRODUCT_TYPE_WARNINGS);
		page.enterInstructions(TEST_PRODUCT_TYPE_INSTRUCTIONS);
		page.enterCautionsUrl(TEST_PRODUCT_TYPE_CAUTIONS_URL);
		page.checkHasManufacturerCertificate();
		page.enterManufacturerCertificateText(TEST_PRODUCT_TYPE_MANUFACTURER_CERTIFICATE_TEXT);
		page.enterProductDescriptionTemplate(TEST_PRODUCT_TYPE_PRODUCT_DESCRIPTION_TEMPLATE);
		page.saveProductType();
		page.clickViewAllTab();
	}
	
	private void removeTestProductTypeIfNecessary(String typeName) {
		System.out.println("Testing remove product type if necessary: " + page.getProductTypes());
		if(page.getProductTypes().contains(typeName)) {
			page.clickEditProductType(typeName);
			page.deleteProductType();
		}
		assertFalse(page.getProductTypes().contains(typeName));
	}

}
