package com.n4systems.fieldid.selenium.testcase.setup;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.administration.page.ManageProductStatusDriver;
import com.n4systems.fieldid.selenium.datatypes.ProductStatus;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;


@SuppressWarnings("unchecked")
public class ProductStatusCrudTest extends LoggedInTestCase {

	private static final String STATUS_NAME_THAT_IS_TOO_SHORT = "";
	private ManageProductStatusDriver driver;

	@Before
	public void setupDrivers() throws Exception {
		driver = systemDriverFactory.createProductStatusDriver();
	}
	
	@Test
	public void should_allow_the_creation_editing_and_removal_of_a_product_status() throws Exception {
		ProductStatus status = ProductStatus.aVaildProductStatus();
		ProductStatus editedStatus = ProductStatus.aVaildProductStatus();
		
		driver.gotoAddStatus();
		
		driver.createStatus(status);
		
		driver.assertStatusWasCreated(status);
		
		
		
		driver.editStatus(status, editedStatus);
	
		driver.assertStatusWasEdited(editedStatus, status);
		
		
		
		driver.removeStatus(editedStatus);
		
		driver.assertStatusWasRemoved(editedStatus);

	}

	
	
	@Test
	public void should_require_a_new_product_status_to_have_name() throws Exception {
		ProductStatus invalidNameInTempalte = new ProductStatus(STATUS_NAME_THAT_IS_TOO_SHORT);
		
		driver.gotoAddStatus();
		
		driver.createStatus(invalidNameInTempalte);
		
		driver.assertVaildationErrorFor(ManageProductStatusDriver.FieldName.NAME_FIELD, containsString("required"));
		
		
		
	}
	
	@Test
	public void should_require_an_edited_product_status_to_have_name() throws Exception {
		ProductStatus invalidNameInTempalte = new ProductStatus(STATUS_NAME_THAT_IS_TOO_SHORT);
		
		ProductStatus status = driver.selectAnExistingStatus();
		
		driver.editStatus(status, invalidNameInTempalte);
		
		driver.assertVaildationErrorFor(ManageProductStatusDriver.FieldName.NAME_FIELD, allOf(containsString("required"), containsString("name")));
	}
	
	
	
	
	
}
