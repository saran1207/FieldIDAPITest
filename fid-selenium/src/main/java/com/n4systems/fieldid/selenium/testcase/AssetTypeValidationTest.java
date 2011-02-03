package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import org.junit.Test;

public class AssetTypeValidationTest extends PageNavigatingTestCase<HomePage> {

    @Override
    protected HomePage navigateToPage() {
        return start().login();
    }

	@Test
	public void should_not_validate_an_info_option_after_the_field_is_changed_to_one_that_does_not_have_static_options() throws Exception {
		selenium.open("/fieldid/assetTypeEdit.action");
		selenium.waitForPageToLoad();
		selenium.click("addInfoField");
		selenium.waitForElementToBePresent("css=#field_0", WebEntity.JS_TIMEOUT);
		selenium.type("css=#field_0 .name", "field 1");
		selenium.select("css=#field_0 .fieldType", "label=Select Box");
		selenium.click("css=#field_0 .editInfoOptions");
		selenium.click("css=#field_0 .addOption");
		selenium.waitForElementToBePresent("css=#field_0 .fieldType", WebEntity.JS_TIMEOUT);
		selenium.select("css=#field_0 .fieldType", "label=Text Field");
		selenium.click("save");
		selenium.waitForPageToLoad();

		assertFalse(selenium.isTextPresent("Drop Down Option Name can not be blank."));
		assertFalse(selenium.isElementPresent("css=#infoFieldEditing .errorMessage"));
	}
	
	@Test
	public void should_find_an_info_option_with_a_blank_name_added_to_an_select_box_to_be_invalid() throws Exception {
		selenium.open("/fieldid/assetTypeEdit.action");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		selenium.click("addInfoField");
		selenium.waitForElementToBePresent("css=#field_0", WebEntity.JS_TIMEOUT);
		selenium.type("css=#field_0 .name", "field 1");
		selenium.select("css=#field_0 .fieldType", "label=Select Box");
		selenium.click("css=#field_0 .editInfoOptions");
		selenium.click("css=#field_0 .addOption");
		selenium.click("save");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);

		assertTrue(selenium.isTextPresent("Drop Down Option Name can not be blank."));
		assertTrue(selenium.isElementPresent("css=#infoFieldEditing .errorMessage"));
	}

}
