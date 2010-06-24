package com.n4systems.fieldid.selenium.testcase.setup;


import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.administration.page.ManageProductStatuses;
import com.n4systems.fieldid.selenium.administration.page.ProductStatus;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;

public class ProductStatusCrudTest extends LoggedInTestCase {

	
	
	private ManageProductStatuses manageProductStatusDriver;

	@Before
	public void setupDrivers() throws Exception {
		manageProductStatusDriver = systemDriverFactory.createProductStatusDriver();
	}
	
	@Test
	public void should_allow_the_creation_editing_and_removal_of_a_product_status() throws Exception {
		manageProductStatusDriver.gotoAddProductStatus();
		
		ProductStatus productStatus = new ProductStatus(misc.getRandomString(25), misc.getRandomString(100));
		
		manageProductStatusDriver.createStatus(productStatus);
		
		manageProductStatusDriver.assertStatusWasCreated(productStatus);
		
		manageProductStatusDriver.removeStauts(productStatus);
		
		manageProductStatusDriver.assertStatusWasRemoved(productStatus);
	
		
//		selenium.type("commentTemplateEdit_name", "random name2");
//		selenium.type("commentTemplateEdit_comment", "random comment2");
//		selenium.click("commentTemplateEdit_hbutton_save");
//		selenium.waitForPageToLoad("30000");
//		assertTrue(selenium.isTextPresent("random name2"));
//		selenium.click("//div[@id='pageContent']/table/tbody/tr[7]/td[2]/a[1]");
//		selenium.waitForPageToLoad("30000");
//		assertTrue(selenium.isTextPresent(""));
//		assertTrue(selenium.isTextPresent("random comment2"));
//		selenium.click("link=View All");
//		selenium.waitForPageToLoad("30000");
//		selenium.click("//div[@id='pageContent']/table/tbody/tr[7]/td[2]/a[2]");
//		assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete this[\\s\\S]$"));
//		assertFalse(selenium.isTextPresent("random name2"));
	}
}
