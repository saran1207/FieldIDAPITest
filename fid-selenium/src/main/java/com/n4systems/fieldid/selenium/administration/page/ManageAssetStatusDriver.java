package com.n4systems.fieldid.selenium.administration.page;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.datatypes.AssetStatus;
import org.hamcrest.Matcher;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.nav.OptionNavgationDriver;

import java.util.ArrayList;
import java.util.List;

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
        selenium.clickAndWaitForPageLoad("//table[@class='list']//td[position() = 1 and text() = '"+status.name+"']//parent::tr/td[4]//a[.='Edit']");
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
        selenium.clickAndWaitForPageLoad("//table[@class='list']//td[position() = 1 and text() = '"+status.name+"']//parent::tr/td[4]//a[.='Remove']");
		
		assertThat(selenium.getConfirmation(), startsWith("Are you sure you want to delete this?"));
		selenium.waitForPageToLoad();
		
	}

	public void assertStatusWasRemoved(AssetStatus status) {
		assertNotInList(namedStatus(status, "removed status"));
		
	}

	private void assertNotInList(AssetStatus status) {
		assertFalse("status " + status + " still appears in the list", selenium.isTextPresent(status.name));
	}

    public List<String> getValidationErrors() {
        List<String> validationErrors = new ArrayList<String>();
        int errors = selenium.getXpathCount("//div[@class='formErrors']//span[@class='errorMessage']").intValue();

        for (int i = 1; i <= errors; i++) {
            validationErrors.add(selenium.getText("//div[@class='formErrors']//span[@class='errorMessage']["+i+"]"));
        }
        return validationErrors;
    }

	public AssetStatus selectAnExistingStatus() {
		gotoAssetStatuses();
		selenium.clickAndWaitForPageLoad("css=a:contains('Edit')");
		return new AssetStatus(selenium.getValue(FieldName.NAME_FIELD.inputId));
	}

}
