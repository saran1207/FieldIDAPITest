package com.n4systems.fieldid.selenium.testcase;

import org.junit.Test;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ProductTypeValidationTest extends LoggedInTestCase {

	@Test
	public void should_not_validate_an_info_option_after_the_field_is_changed_to_one_that_does_not_have_static_options() throws Exception {
		selenium.open("/fieldid/productTypeEdit.action");
		selenium.waitForPageToLoad();
		selenium.click("addInfoField");
		selenium.waitForElementToBePresent("css=#field_0", MiscDriver.JS_TIMEOUT);
		selenium.type("css=#field_0 .name", "field 1");
		selenium.select("css=#field_0 .fieldType", "label=Select Box");
		selenium.click("css=#field_0 .editInfoOptions");
		selenium.click("css=#field_0 .addOption");
		selenium.waitForElementToBePresent("css=#field_0 .fieldType", MiscDriver.JS_TIMEOUT);
		selenium.select("css=#field_0 .fieldType", "label=Text Field");
		selenium.click("save");
		selenium.waitForPageToLoad();

		assertFalse(selenium.isTextPresent("Drop Down Option Name can not be blank."));
		assertFalse(selenium.isElementPresent("css=#infoFieldEditing .errorMessage"));
	}
	
	@Test
	public void should_find_an_info_option_with_a_blank_name_added_to_an_select_box_to_be_invalid() throws Exception {
		selenium.open("/fieldid/productTypeEdit.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		selenium.click("addInfoField");
		selenium.waitForElementToBePresent("css=#field_0", MiscDriver.JS_TIMEOUT);
		selenium.type("css=#field_0 .name", "field 1");
		selenium.select("css=#field_0 .fieldType", "label=Select Box");
		selenium.click("css=#field_0 .editInfoOptions");
		selenium.click("css=#field_0 .addOption");
		selenium.click("save");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);

		assertTrue(selenium.isTextPresent("Drop Down Option Name can not be blank."));
		assertTrue(selenium.isElementPresent("css=#infoFieldEditing .errorMessage"));
	}

}
