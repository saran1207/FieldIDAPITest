package com.n4systems.fieldid.selenium.administration.page;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;

import com.n4systems.fieldid.selenium.datatypes.ProductStatus;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.nav.OptionNavgationDriver;

public class ManageProductStatusDriver {
	public enum FieldName {
		NAME_FIELD("productStatusEdit_name");
		
		private String inputId;
		
		private FieldName(String inputId) {
			this.inputId = inputId;
		}
	};
	private static final String SAVE_BUTTON_LOCATOR = "productStatusEdit_hbutton_save";
	

	private final FieldIdSelenium selenium;
	

	public ManageProductStatusDriver(FieldIdSelenium selenium) {
		this.selenium = selenium;
	}
	
	public void gotoProductStatuses() {
		selenium.open("/fieldid/productStatusList.action");
	}

	public void gotoAddStatus() {
		gotoProductStatuses();
		selenium.clickAndWaitForPageLoad(OptionNavgationDriver.ADD_ACTION_LINK_LOCATOR);
	}
	
	
	public void createStatus(ProductStatus status) {
		fillInAndSubmitStatusForm(status);
		
	}

	private void fillInAndSubmitStatusForm(ProductStatus status) {
		selenium.type(FieldName.NAME_FIELD.inputId, status.name);
		selenium.clickAndWaitForPageLoad(SAVE_BUTTON_LOCATOR);
	}
	
	private void selectStatusEditPage(ProductStatus status) {
		assertStatusIsInList(status);
		selenium.clickAndWaitForPageLoad("css=td:contains('" + status.name + "') + td a:contains('Edit')");
	}

	private void assertStatusIsInList(ProductStatus status) {
		assertTrue("asset status " + status.name + " does not appear in the list", selenium.isTextPresent(status.name));
	}

	public void assertStatusWasCreated(ProductStatus status) {
		selectStatusEditPage(status);
		assertEquals(status.name, selenium.getValue(FieldName.NAME_FIELD.inputId));
	}

	public void editStatus(ProductStatus originalStatus, ProductStatus editedStatus) {
		gotoProductStatuses();
		selectStatusEditPage(originalStatus);
		fillInAndSubmitStatusForm(editedStatus);
	}

	public void assertStatusWasEdited(ProductStatus editedStatus, ProductStatus originalStatus) {
		assertNotInList(namedStatus(originalStatus, "original status"));
		assertStatusWasCreated(editedStatus);
	}

	private ProductStatus namedStatus(ProductStatus originalStatus, final String name) {
		return new ProductStatus(originalStatus.name) { public String toString() { return name + " :" + super.toString(); }};
	}

	public void removeStatus(ProductStatus status) {
		gotoProductStatuses();
		
		selenium.chooseOkOnNextConfirmation();
		selenium.click("css=td:contains('" + status.name + "') + td a:contains('Remove')");
		
		assertThat(selenium.getConfirmation(), startsWith("Are you sure you want to delete this?"));
		selenium.waitForPageToLoad();
		
	}

	public void assertStatusWasRemoved(ProductStatus status) {
		assertNotInList(namedStatus(status, "removed status"));
		
	}

	private void assertNotInList(ProductStatus status) {
		assertFalse("status " + status + " still appears in the list", selenium.isTextPresent(status.name));
	}

	public void assertVaildationErrorFor(FieldName field, Matcher<String> messageMatcher) {
		String fieldErrorLocator = "css=*[errorfor='" + field.inputId + "']";
		assertThat("there is no error message for field " + field.name(), selenium.isElementPresent(fieldErrorLocator), is(true));
		
		assertThat(selenium.getAttribute(fieldErrorLocator + "@class"), containsString("errorMessage"));
		assertThat(selenium.getText(fieldErrorLocator), messageMatcher);
		
	}

	public ProductStatus selectAnExistingStatus() {
		gotoProductStatuses();
		selenium.clickAndWaitForPageLoad("css=a:contains('Edit')");
		return new ProductStatus(selenium.getValue(FieldName.NAME_FIELD.inputId));
	}

}
