package com.n4systems.fieldid.selenium.testcase.setup;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import com.n4systems.fieldid.selenium.administration.page.ManageAssetStatusDriver;
import com.n4systems.fieldid.selenium.datatypes.AssetStatus;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;


@SuppressWarnings("unchecked")
public class AssetStatusCrudTest extends LoggedInTestCase {

	private static final String STATUS_NAME_THAT_IS_TOO_SHORT = "";
	private ManageAssetStatusDriver driver;

	@Before
	public void setupDrivers() throws Exception {
		driver = systemDriverFactory.createAssetStatusDriver();
	}
	
	@Test
	public void should_allow_the_creation_editing_and_removal_of_an_asset_status() throws Exception {
		AssetStatus status = AssetStatus.aVaildAssetStatus();
		AssetStatus editedStatus = AssetStatus.aVaildAssetStatus();
		
		driver.gotoAddStatus();
		
		driver.createStatus(status);
		
		driver.assertStatusWasCreated(status);
		
		driver.editStatus(status, editedStatus);
	
		driver.assertStatusWasEdited(editedStatus, status);
		
		driver.removeStatus(editedStatus);
		
		driver.assertStatusWasRemoved(editedStatus);
	}
	
	@Test
	public void should_require_a_new_asset_status_to_have_name() throws Exception {
		AssetStatus invalidNameInTempalte = new AssetStatus(STATUS_NAME_THAT_IS_TOO_SHORT);
		
		driver.gotoAddStatus();
		
		driver.createStatus(invalidNameInTempalte);

        assertEquals("name is a required field.", driver.getValidationErrors().get(0));
	}
	
	@Test
	public void should_require_an_edited_asset_status_to_have_name() throws Exception {
		AssetStatus invalidNameInTempalte = new AssetStatus(STATUS_NAME_THAT_IS_TOO_SHORT);
		
		AssetStatus status = driver.selectAnExistingStatus();
		
		driver.editStatus(status, invalidNameInTempalte);
		assertEquals("name is a required field.", driver.getValidationErrors().get(0));
	}
	
}
