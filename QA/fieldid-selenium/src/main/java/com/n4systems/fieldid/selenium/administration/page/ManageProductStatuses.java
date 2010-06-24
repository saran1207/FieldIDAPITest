package com.n4systems.fieldid.selenium.administration.page;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;

public class ManageProductStatuses {
	private static final String SAVE_BUTTON_LOCATOR = "commentTemplateEdit_hbutton_save";
	private static final String ADD_ACTION_LINK_LOCATOR = "css=.options .add a";
	private static final String NAME_FIELD_LOCATOR = "commentTemplateEdit_name";
	private static final String COMMENT_FIELD_LOCATOR = "commentTemplateEdit_comment";
	
	private final FieldIdSelenium selenium;
	
	public ManageProductStatuses(FieldIdSelenium selenium) {
		this.selenium = selenium;
	}


	public void gotoProductStatuses() {
		selenium.open("/fieldid/commentTemplateList.action");
	}

	public void gotoAddProductStatus() {
		gotoProductStatuses();
		selenium.clickAndWaitForPageLoad(ADD_ACTION_LINK_LOCATOR);
	}

	public void assertStatusWasCreated(ProductStatus productStatusForm) {
		assertTrue(selenium.isTextPresent(productStatusForm.name));
		selenium.clickAndWaitForPageLoad("css=td:contains('" + productStatusForm.name + "') + td a:contains('Edit')");
		
		assertEquals(productStatusForm.comment, selenium.getValue(COMMENT_FIELD_LOCATOR));
		assertEquals(productStatusForm.name, selenium.getValue(NAME_FIELD_LOCATOR));
	}

	public void createStatus(ProductStatus productStatusForm) {
		selenium.type(NAME_FIELD_LOCATOR, productStatusForm.name);
		selenium.type(COMMENT_FIELD_LOCATOR, productStatusForm.comment);
		selenium.clickAndWaitForPageLoad(SAVE_BUTTON_LOCATOR);
		
	}


	public void removeStauts(ProductStatus productStatus) {
		selenium.setSpeed("2000");
		gotoProductStatuses();
		selenium.chooseOkOnNextConfirmation();
		selenium.click("css=td:contains('" + productStatus.name + "') + td a:contains('Remove')");
		assertThat(selenium.getConfirmation(), startsWith("Are you sure you want to delete this?"));
		selenium.waitForPageToLoad();
	}


	public void assertStatusWasRemoved(ProductStatus productStatus) {
		assertFalse(selenium.isTextPresent(productStatus.name));
	}
}
