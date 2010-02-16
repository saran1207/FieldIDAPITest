package com.n4systems.fieldid.selenium.testcase.admintool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.console.page.ConsoleLogin;
import com.n4systems.fieldid.selenium.misc.Misc;

public class AutoAttributeImporterTest extends FieldIDTestCase {

	
	private ConsoleLogin adminConsole;
	
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		adminConsole = new ConsoleLogin(selenium, misc);
	}
	
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	
	@Test
	public void should_find_error_when_using_importer_on_a_product_type_with_auto_attributes() throws Exception {
		String tenantName = "n4";
		String productNameWithAutoAttributes = "Chain";
		
		String autoAttributeImportFile = getSupportFileLocation() + "aa_upload.csv";

		adminConsole.gotoAdminConsoleAndLogin().gotoPage("importAutoAttributes.action");
		
		uploadImportFileForProductType(autoAttributeImportFile, tenantName, productNameWithAutoAttributes);
		
		
		assertTrue(selenium.isTextPresent("Import Failed"));
		assertTrue(selenium.isTextPresent("Product Type already has auto attributes"));
	}


	private void uploadImportFileForProductType(String autoAttributeImportFile,
			String tenantName, String productNameWithAutoAttributes) {
		selenium.select("importAutoAttributes_tenantId", "label=" + tenantName);
		selenium.select("importAutoAttributes_productTypeId", "label=" + productNameWithAutoAttributes);
		
		selenium.attachFile("importAutoAttributes_autoAttributesCsv", autoAttributeImportFile);
		selenium.submit("importAutoAttributes");
		
		selenium.waitForPageToLoad(Misc.defaultTimeout);
	}
}
