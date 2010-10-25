package com.n4systems.fieldid.selenium.administration.page;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.datatypes.AssetStatus;
import org.hamcrest.Matcher;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.nav.OptionNavgationDriver;

public class ManageAssetStatusDriver {
	public enum FieldName {
		NAME_FIELD("assetStatusEdit_name");
		
		private String inputId;
		
		private FieldName(String inputId) {
			this.inputId = inputId;
		}
	};
	private static final String SAVE_BUTTON_LOCATOR = "assetStatusEdit_hbutton_save";
	

	private final FieldIdSelenium selenium;
	

	public ManageAssetStatusDriver(FieldIdSelenium selenium) {
		this.selenium = selenium;
	}
	
	public void gotoAssetStatuses() {
		selenium.open("/fieldid/assetStatusList.action");
	}

	public void gotoAddStatus() {
		gotoAssetStatuses();
		selenium.clickAndWaitForPageLoad(OptionNavgationDriver.ADD_ACTION_LINK_LOCATOR);
	}
	
	
	public void createStatus(AssetStatus status) {
		fillInAndSubmitStatusForm(status);
		
	}

	private void fillInAndSubmitStatusForm(AssetStatus status) {
		selenium.type(FieldName.NAME_FIELD.inputId, status.name);
		selenium.clickAndWaitForPageLoad(SAVE_BUTTON_LOCATOR);
	}
	
	private void selectStatusEditPage(AssetStatus status) {
		assertStatusIsInList(status);
		selenium.clickAndWaitForPageLoad("css=td:contains('" + status.name + "') + td a:contains('Edit')");
	}

	private void assertStatusIsInList(AssetStatus status) {
		assertTrue("asset status " + status.name + " does not appear in the list", selenium.isTextPresent(status.name));
	}

	public void assertStatusWasCreated(AssetStatus status) {
		selectStatusEditPage(status);
		assertEquals(status.name, selenium.getValue(FieldName.NAME_FIELD.inputId));
	}

	public void editStatus(AssetStatus originalStatus, AssetStatus editedStatus) {
		gotoAssetStatuses();
		selectStatusEditPage(originalStatus);
		fillInAndSubmitStatusForm(editedStatus);
	}

	public void assertStatusWasEdited(AssetStatus editedStatus, AssetStatus originalStatus) {
		assertNotInList(namedStatus(originalStatus, "original status"));
		assertStatusWasCreated(editedStatus);
	}

	private AssetStatus namedStatus(AssetStatus originalStatus, final String name) {
		return new AssetStatus(originalStatus.name) { public String toString() { return name + " :" + super.toString(); }};
	}

	public void removeStatus(AssetStatus status) {
		gotoAssetStatuses();
		
		selenium.chooseOkOnNextConfirmation();
		selenium.click("css=td:contains('" + status.name + "') + td a:contains('Remove')");
		
		assertThat(selenium.getConfirmation(), startsWith("Are you sure you want to delete this?"));
		selenium.waitForPageToLoad();
		
	}

	public void assertStatusWasRemoved(AssetStatus status) {
		assertNotInList(namedStatus(status, "removed status"));
		
	}

	private void assertNotInList(AssetStatus status) {
		assertFalse("status " + status + " still appears in the list", selenium.isTextPresent(status.name));
	}

	public void assertVaildationErrorFor(FieldName field, Matcher<String> messageMatcher) {
		String fieldErrorLocator = "css=*[errorfor='" + field.inputId + "']";
		assertThat("there is no error message for field " + field.name(), selenium.isElementPresent(fieldErrorLocator), is(true));
		
		assertThat(selenium.getAttribute(fieldErrorLocator + "@class"), containsString("errorMessage"));
		assertThat(selenium.getText(fieldErrorLocator), messageMatcher);
		
	}

	public AssetStatus selectAnExistingStatus() {
		gotoAssetStatuses();
		selenium.clickAndWaitForPageLoad("css=a:contains('Edit')");
		return new AssetStatus(selenium.getValue(FieldName.NAME_FIELD.inputId));
	}

}
