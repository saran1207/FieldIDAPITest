package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.AssetType;
import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.SelectBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.TextFieldAttribute;
import com.n4systems.fieldid.selenium.datatypes.UnitOfMeasureAttribute;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Wait;

public class ManageAssetTypesDriver {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageAssetTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Asset Types')]";
	private String assetTypeTableXpath = "//DIV[@id='pageContent']/TABLE[@class='list']";
	private String assetTypeTableLocator = "xpath=" + assetTypeTableXpath;
	private String assetTypesXpath = assetTypeTableXpath + "/TBODY/TR/TD[1]/A";
	private String addAssetTypeLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Add')]";
	private String assetInformationHeaderLocator = "xpath=//FORM[@id='assetTypeUpdate']/H2[contains(text(),'Asset Information')]";
	private String groupSelectListLocator = "xpath=//SELECT[@id='assetTypeUpdate_group']";
	private String nameTextFieldLocator = "xpath=//INPUT[@id='assetTypeUpdate_name']";
	private String warningsTextFieldLocator = "xpath=//TEXTAREA[@id='assetTypeUpdate_warnings']";
	private String instructionsTextFieldLocator = "xpath=//TEXTAREA[@id='assetTypeUpdate_instructions']";
	private String cautionsURLLocator = "xpath=//INPUT[@id='assetTypeUpdate_cautionsUrl']";
	private String hasManufacturerCertificateCheckboxLocator = "xpath=//INPUT[@id='assetTypeUpdate_hasManufacturerCertificate']";
	private String manufacturerCertificateTextFieldLocator = "xpath=//TEXTAREA[@id='assetTypeUpdate_manufacturerCertificateText']";
	private String assetDescriptionTemplateTextFieldLocator = "xpath=//INPUT[@id='assetTypeUpdate_descriptionTemplate']";
	private String uploadImageTextFieldLocator = "xpath=//INPUT[@id='uploadImage_upload']";
	private String attributesHeaderLocator = "xpath=//FORM[@id='assetTypeUpdate']/H2[contains(text(),'Attributes')]";
	private String attributesAddButtonLocator = "xpath=//DIV[@id='infoFieldEditing']/DIV/INPUT[contains(@value,'Add')]";
	private String attributesUndoDeletesButtonLocator = "xpath=//DIV[@id='infoFieldEditing']/DIV/INPUT[contains(@value,'Undo Deletes')]";
	private String attachmentsHeaderLocator = "xpath=//FORM[@id='assetTypeUpdate']/H2[contains(text(),'Attachments')]";
	private String attachAFileButtonLocator = "xpath=//FORM[@id='assetTypeUpdate']/P[contains(@class,'actions')]/BUTTON[contains(text(),'Attach A File')]";
	private String cancelButtonLocator = "xpath=//FORM[@id='assetTypeUpdate']/DIV[@class='formAction']/INPUT[contains(@value,'Cancel')]";
	private String saveButtonLocator = "xpath=//FORM[@id='assetTypeUpdate']/DIV[@class='formAction']/INPUT[contains(@value,'Save')]";
	private String viewAllAssetTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'View All')]";;
	private String viewAssetTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'View') and not(contains(text(),'All'))]";;
	private String editAssetTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Edit')]";
	private String inspectionTypesAssetTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Inspection Types')]";;
	private String inspectionFrequenciesAssetTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Inspection Frequencies')]";;
	private String subComponentsAssetTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Sub-Components')]";
	private String inspectionTypeTableXpath = "//form[@id='assetTypeEventTypesSave']/table";
	
	public ManageAssetTypesDriver(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	/**
	 * Checks for error messages on the page and that the
	 * Manage Asset Types header exists.
	 */
	public void verifyManageAssetTypesPage() {
		misc.checkForErrorMessages("verifyManageAssetTypesPage");
		if(!selenium.isElementPresent(manageAssetTypesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Asset Types'.");
		}
	}
	
	/**
	 * Assumes you are on the Manage Asset Types page. Currently
	 * this page is not paginated. So this just grabs everything from
	 * the current page. If this becomes paginated, we will need to
	 * update this accordingly.
	 * 
	 * @return
	 */
	public List<String> getAssetTypes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(assetTypesXpath);
		int numAssetTypes = n.intValue();
		int row = 1;
		String locator = assetTypeTableLocator + "." + row + ".0";
		for(int i = 0; i < numAssetTypes; i++, row++) {
			String assetType = selenium.getTable(locator);
			result.add(assetType.trim());
			locator = locator.replaceFirst("\\." + row + "\\.0", "." + (row+1) + ".0");
		}
		return result;
	}
	
	/**
	 * Clicks on the Add link on the Manage Asset Types page.
	 * Also verifies the Add Asset Type page appeared successfully.
	 */
	public void gotoAddAssetType() {
		if(selenium.isElementPresent(addAssetTypeLinkLocator)) {
			selenium.click(addAssetTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
			verifyManageAssetTypeAddPage();
		} else {
			fail("Could not find the link to add an asset type");
		}
	}

	/**
	 * Check that all the usual fields are present.
	 */
	public void verifyManageAssetTypeAddPage() {
		assertTrue(selenium.isElementPresent(assetInformationHeaderLocator));
		assertTrue(selenium.isElementPresent(groupSelectListLocator));
		assertTrue(selenium.isElementPresent(nameTextFieldLocator));
		assertTrue(selenium.isElementPresent(warningsTextFieldLocator));
		assertTrue(selenium.isElementPresent(instructionsTextFieldLocator));
		assertTrue(selenium.isElementPresent(cautionsURLLocator));
		assertTrue(selenium.isElementPresent(hasManufacturerCertificateCheckboxLocator));
		assertTrue(selenium.isElementPresent(manufacturerCertificateTextFieldLocator));
		assertTrue(selenium.isElementPresent(assetDescriptionTemplateTextFieldLocator));
		assertTrue(selenium.isElementPresent(uploadImageTextFieldLocator));
		assertTrue(selenium.isElementPresent(attributesHeaderLocator));
		assertTrue(selenium.isElementPresent(attributesAddButtonLocator));
		assertTrue(selenium.isElementPresent(attributesUndoDeletesButtonLocator));
		assertTrue(selenium.isElementPresent(attachmentsHeaderLocator));
		assertTrue(selenium.isElementPresent(attachAFileButtonLocator));
		assertTrue(selenium.isElementPresent(cancelButtonLocator));
		assertTrue(selenium.isElementPresent(saveButtonLocator));
	}

	/**
	 * Click the Save button on the Add Asset Type page.
	 */
	public void gotoSaveAssetType() {
		if(selenium.isElementPresent(saveButtonLocator)) {
			selenium.click(saveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the save button");
		}
	}

	/**
	 * Click the Cancel button on the Add Asset Type page.
	 */
	public void gotoCancelAssetType() {
		if(selenium.isElementPresent(cancelButtonLocator)) {
			selenium.click(cancelButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the cancel button");
		}
	}

	/**
	 * This fills on the add asset type page. It will check
	 * for null input and that all the usual fields are visible
	 * on the page. If any field in the AssetType object is
	 * set to null, it will skip it and use whatever the default
	 * values are.
	 * 
	 * Upload Image and Attach File do not currently work. Defining
	 * them in the AssetType will cause the test case to fail.
	 *  
	 * @param assetType
	 */
	public void setAssetType(AssetType assetType) {
		assertNotNull(assetType);
		verifyManageAssetTypeAddPage();
		if(assetType.getGroup() != null) {
			if(misc.isOptionPresent(groupSelectListLocator, assetType.getGroup())) {
				selenium.select(groupSelectListLocator, assetType.getGroup());
			} else {
				fail("Could not find the option '" + assetType.getGroup() + "' on the Group list");
			}
		}
		if(assetType.getName() != null) {
			selenium.type(nameTextFieldLocator, assetType.getName());
		}
		if(assetType.getWarnings() != null) {
			selenium.type(warningsTextFieldLocator, assetType.getWarnings());
		}
		if(assetType.getInstructions() != null) {
			selenium.type(instructionsTextFieldLocator, assetType.getInstructions());
		}
		if(assetType.getCautionsURL() != null) {
			selenium.type(cautionsURLLocator, assetType.getCautionsURL());
		}
		if(assetType.getHasManufacturerCertificate()) {
			selenium.check(hasManufacturerCertificateCheckboxLocator);
		} else {
			selenium.uncheck(hasManufacturerCertificateCheckboxLocator);
		}
		if(assetType.getManufacturerCertificateText() != null) {
			selenium.type(manufacturerCertificateTextFieldLocator, assetType.getManufacturerCertificateText());
		}
		if(assetType.getAssetDescriptionTemplate() != null) {
			selenium.type(assetDescriptionTemplateTextFieldLocator, assetType.getAssetDescriptionTemplate());
		}
		if(assetType.getUploadImageFileName() != null) {
			fail("Not implemented");
		}
		if(assetType.getAttributes() != null && assetType.getAttributes().size() > 0) {
			setAssertTypeAttributes(assetType.getAttributes());
		}
		if(assetType.getAttachments() != null && assetType.getAttachments().size() > 0) {
			fail("Not implemented");
		}
	}

	/**
	 * Set the asset type attributes. Call by setAssetType.
	 * 
	 * @param attributes
	 */
	private void setAssertTypeAttributes(List<Attribute> attributes) {
		Iterator<Attribute> i = attributes.iterator();
		while(i.hasNext()) {
			Attribute a = i.next();
			if(a instanceof TextFieldAttribute) {
				TextFieldAttribute tfa = (TextFieldAttribute)a;
				addTextFieldAttribute(tfa);
			} else if(a instanceof SelectBoxAttribute) {
				SelectBoxAttribute sba = (SelectBoxAttribute)a;
				addSelectBoxAtribute(sba);
			} else if(a instanceof ComboBoxAttribute) {
				ComboBoxAttribute cba = (ComboBoxAttribute)a;
				addComboBoxAttribute(cba);
			} else if(a instanceof UnitOfMeasureAttribute) {
				UnitOfMeasureAttribute uoma = (UnitOfMeasureAttribute)a;
				addUnitOfMeasureAttribute(uoma);
			} else {
				fail("Tried to add an unknown attribute type");
			}
		}
	}

	private void addUnitOfMeasureAttribute(UnitOfMeasureAttribute uoma) {
		int index = addAttribute(uoma);
		String defaultLocator = "xpath=//SELECT[@id='assetTypeUpdate_infoFields_" + index + "__defaultUnitOfMeasure']";
		if(uoma.getDefault() != null) {
			if(selenium.isElementPresent(defaultLocator)) {
				if(misc.isOptionPresent(defaultLocator, uoma.getDefault())) {
					selenium.select(defaultLocator, uoma.getDefault());
				} else {
					fail("Could not find the option '" + uoma.getDefault() + "' on the list of default unit of measures");
				}
			} else {
				fail("Could not find the Default select list for the new Unit Of Measure");
			}
		}
	}

	/**
	 * The Attribute class is a parent to all the other classes.
	 * This method will take a child class and populate all the
	 * fields which are actually defined in the parent class.
	 * 
	 * It returns the index of the attribute which was added.
	 * 
	 * You can find the newest attribute by looking at the last
	 * element of a given xpath/css.
	 * 
	 * @param a
	 * @return
	 */
	private int addAttribute(Attribute a) {
		int index = gotoAddAttribute() - 1;
		String nameLocator = getAttributeNameLocator(index);
		String typeLocator = getAttributeTypeLocator(index);
		String requiredLocator = getAttributeRequiredLocator(index);
		if(a.getName() != null) {
			if(selenium.isElementPresent(nameLocator)) {
				selenium.type(nameLocator, a.getName());
			} else {
				fail("Could not find the Name text field for the newly added Attribute");
			}
		}
		if(a.getType() != null) {
			if(selenium.isElementPresent(typeLocator)) {
				if(misc.isOptionPresent(typeLocator, a.getType())) {
					selenium.select(typeLocator, a.getType());
				} else {
					fail("Could not find the attribute type '" + a.getType() + "' on the select list");
				}
			} else {
				fail("Could not find the attribute type select list");
			}
		} else {
			fail("The attribute type was not set. It must be set.");
		}
		if(selenium.isElementPresent(requiredLocator)) {
			if(a.getRequired()) {
				selenium.check(requiredLocator);
			} else {
				selenium.uncheck(requiredLocator);
			}
		} else {
			fail("Could not find the required checkbox");
		}
		return index;
	}

	/**
	 * Add the Combo Box attribute including drop downs.
	 * 
	 * @param cba
	 */
	private void addComboBoxAttribute(ComboBoxAttribute cba) {
		int attributeIndex = addAttribute(cba);
		addDropDowns(cba.getDropDowns(), attributeIndex);
	}

	/**
	 * Add the Select Box attribute including drop downs.
	 * 
	 * @param sba
	 */
	private void addSelectBoxAtribute(SelectBoxAttribute sba) {
		int attributeIndex = addAttribute(sba);
		addDropDowns(sba.getDropDowns(), attributeIndex);
	}
	
	/**
	 * The way drop downs for combo box and select box are handled
	 * is the same. So adding either can use this method to add the
	 * drop downs.
	 * 
	 * @param dropDowns
	 * @param attributeIndex
	 */
	private void addDropDowns(List<String> dropDowns, int attributeIndex) {
		String editDropDownLinkLocator = getEditDropDownLinkLocator(attributeIndex);
		if(selenium.isElementPresent(editDropDownLinkLocator)) {
			selenium.click(editDropDownLinkLocator);
		} else {
			fail("Could not find the link to Edit Drop Down");
		}
		
		Iterator<String> i = dropDowns.iterator();
		while(i.hasNext()) {
			String dropDown = i.next();
			gotoAddDropDown(attributeIndex);
			String locator = "xpath=//DIV[@id='infoOptions_" + attributeIndex + "']/DIV[last()]/INPUT[contains(@id,'__name')]";
			if(selenium.isElementPresent(locator)) {
				selenium.type(locator, dropDown);
			} else {
				fail("Could not find the Drop Down Options text field");
			}
		}
	}

	/**
	 * Click the Add button for adding drop downs.
	 * 
	 * @param attributeIndex
	 */
	private void gotoAddDropDown(int attributeIndex) {
		int result = -1;
		String addDropDownOptionButtonLocator = getAddDropDownOptionButtonLocator(attributeIndex);
		if(selenium.isElementPresent(addDropDownOptionButtonLocator)) {
			int index = getDropDownCount(attributeIndex);
			selenium.click(addDropDownOptionButtonLocator);
			do {
				misc.sleep(250);
				result = getDropDownCount(attributeIndex);
			} while(result == index);
		} else {
			fail("Could not find the Add button for adding a Drop Down");
		}
	}

	private int getDropDownCount(int attributeIndex) {
		int result = -1;
		String infoFieldsXpath = "//DIV[@id='infoOptions_" + attributeIndex + "']/DIV[contains(@class,'infoOptionHandle')]";
		Number n = selenium.getXpathCount(infoFieldsXpath);
		result = n.intValue();
		return result;
	}

	private String getAddDropDownOptionButtonLocator(int index) {
		String locator = "xpath=//DIV[@id='infoOptionContainer_" + index + "']/INPUT[@value='Add']";
		return locator;
	}

	private String getEditDropDownLinkLocator(int index) {
		String locator = "xpath=//DIV[@id='field_" + index + "']/DIV[contains(@class,'linkCol')]/A[contains(text(),'Edit Drop Down')]";
		return locator;
	}

	/**
	 * A TextFieldAttribute and an Attribute are identical. So this
	 * method really does nothing more than add a basic Attribute.
	 * 
	 * @param tfa
	 */
	public void addTextFieldAttribute(TextFieldAttribute tfa) {
		addAttribute(tfa);
	}
	
	private String getAttributeNameLocator(int index) {
		String locator = "xpath=//INPUT[@id='assetTypeUpdate_infoFields_" + index + "__name']";
		return locator;
	}
	
	private String getAttributeTypeLocator(int index) {
		String locator = "xpath=//SELECT[@id='assetTypeUpdate_infoFields_" + index + "__fieldType']";
		return locator;
	}
	
	private String getAttributeRequiredLocator(int index) {
		String locator = "xpath=//INPUT[@id='assetTypeUpdate_infoFields_" + index + "__required']";
		return locator;
	}

	/**
	 * Clicks the Add button in the Attributes section.
	 */
	private int gotoAddAttribute() {
		int result = -1;
		if(selenium.isElementPresent(attributesAddButtonLocator)) {
			int index = getAttributeCount();
			selenium.click(attributesAddButtonLocator);
			do {
				misc.sleep(250);
				result = getAttributeCount();
			} while(result == index);
		} else {
			fail("Could not find the Add button in the Attributes section");
		}
		return result;
	}

	/**
	 * Get the number of attributes added to the asset type.
	 * This number will include the attributes which were added
	 * then deleted.
	 * 
	 * So if this returns 10, there are 10 attributes but some
	 * might be hidden because they were deleted.
	 * 
	 * @return
	 */
	private int getAttributeCount() {
		int result = -1;
		String infoFieldsXpath = "//DIV[@id='infoFields']/DIV[contains(@class,'handle')]";
		Number n = selenium.getXpathCount(infoFieldsXpath);
		result = n.intValue();
		return result;
	}

	/**
	 * Go to the View All tab on Manage Asset Type. It assumes you
	 * are not already on the Manage Asset Types page but are in
	 * one of the other tabs.
	 */
	public void gotoViewAllAssetType() {
		if(selenium.isElementPresent(viewAllAssetTypeTabLinkLocator)) {
			selenium.click(viewAllAssetTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the View tab on Manage Asset Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoViewAssetType() {
		if(selenium.isElementPresent(viewAssetTypeTabLinkLocator)) {
			selenium.click(viewAssetTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Edit tab on Manage Asset Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoEditAssetType() {
		if(selenium.isElementPresent(editAssetTypeTabLinkLocator)) {
			selenium.click(editAssetTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Inspection Types tab on Manage Asset Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoInspectionTypesAssetType() {
		if(selenium.isElementPresent(inspectionTypesAssetTypeTabLinkLocator)) {
			selenium.click(inspectionTypesAssetTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Inspection Frequencies tab on Manage Asset Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoInspectionFrequenciesAssetType() {
		if(selenium.isElementPresent(inspectionFrequenciesAssetTypeTabLinkLocator)) {
			selenium.click(inspectionFrequenciesAssetTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Sub-Components tab on Manage Asset Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoSubComponentsAssetType() {
		if(selenium.isElementPresent(subComponentsAssetTypeTabLinkLocator)) {
			selenium.click(subComponentsAssetTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Deletes the give list of asset type attributes.
	 * Assumes you are  on the edit page for the asset type.
	 * Assumes all the attributes exist. The first attribute
	 * which does not exist will cause the method to fail the
	 * test case.
	 * 
	 * @param attributes
	 * @throws InterruptedException 
	 */
	public void deleteAssetTypeAttributes(List<Attribute> attributes) throws InterruptedException {
		deleteRetireAssetTypeAttributeHelper(attributes, true);
	}
	
	/**
	 * Will delete or retire asset type attributes. If the delete
	 * parameter is true, it assumes you want to Delete the attributes.
	 * Otherwise, it will attempt to Retire the attributes.
	 * 
	 * @param attributes
	 * @param delete
	 * @throws InterruptedException 
	 */
	private void deleteRetireAssetTypeAttributeHelper(List<Attribute> attributes, boolean delete) throws InterruptedException {
		String linkText = "Retire";
		if (delete) {
			linkText = "Delete";
		}
		selenium.isElementPresent("css=h2:contains('Attributes')");
		
		for (Attribute a : attributes) {
			String locator = "xpath=//INPUT[@value='" + a.getName() + "']";
			String deleteLocator = locator + "/../../DIV[contains(@class,'linkCol')]/A[contains(text(),'" + linkText + "')]";
			
			waitForAttributeDeleteLinkToBecomeVisible(linkText, a, deleteLocator);
			
			selenium.click(deleteLocator);
			
			if (delete) {
				waitForAttributeToBeDeleted(locator);
				verifyAttributeWasDeleted(a.getName());
			} else {
				waitForAttributeToBeRetired(deleteLocator);
				verifyAttributeWasRetired(a.getName());
			}
		}
	}

	private void waitForAttributeDeleteLinkToBecomeVisible(String linkText, Attribute a, final String deleteLocator) {
		new Wait() { public boolean until() { return selenium.isElementPresent(deleteLocator); } }
			.wait("Could not find a " + linkText + " link on the attribute '" + a.getName() + "' or could not find the attribute");
	}

	private void waitForAttributeToBeRetired(final String locator) {
		new Wait() {
			public boolean until() {
				return !selenium.isElementPresent(locator);
			}
		}.wait("Element '" + locator + "' is still invisible.");
	}

	/**
	 * When you link on Delete for an asset type attribute it takes
	 * a short period of time for the javascript to delete the element
	 * from the DOM. This method waits for the javascript to finish.
	 * Essentially, the moment the input field is invisible, this 
	 * method will return.
	 */
	private void waitForAttributeToBeDeleted(final String locator) {
			new Wait() {
				public boolean until() {
					return !selenium.isVisible(locator);
				}
			}.wait("Element '" + locator + "' was never set to invisible", Long.parseLong(MiscDriver.DEFAULT_TIMEOUT));
	}

	private void verifyAttributeWasRetired(String name) {
		String locator = "//INPUT[@value='" + name + "' and @disabled]";
		assertTrue("Could not find the read-only INPUT for '" + name + "'", selenium.isElementPresent(locator));
		String hiddenInputLocator = locator + "/../../INPUT[contains(@id,'retired') and @value='true']";
		assertTrue("Hidden input with ", selenium.isElementPresent(hiddenInputLocator));
	}

	private void verifyAttributeWasDeleted(String name) {
		String attributeLocator = "xpath=//INPUT[@value='" + name + "']/../..@style";
		String style = selenium.getAttribute(attributeLocator);
		assertTrue(style.contains("display: none"));
	}

	/**
	 * Go to View an asset type. Assumes you are on the View All
	 * page of Manage Asset Types.
	 * 
	 * @param name
	 */
	public void gotoViewAssetType(String name) {
		String assetTypeLinkLocator = getAssetTypeLinkLocator(name);
		if(selenium.isElementPresent(assetTypeLinkLocator)) {
			selenium.click(assetTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a link to '" + name + "'");
		}
	}
	
	private String getAssetTypeLinkLocator(String name) {
		String locator = assetTypeTableLocator + "/TBODY/TR/TD[1]/A[contains(text(),'" + name + "')]";
		return locator;
	}

	/**
	 * Go to Edit asset type. This uses the Edit link
	 * on the View All page. This does not use the Edit
	 * tab on the Manage Asset Type page.
	 * 
	 * @param name the name of the asset type.
	 */
	public void gotoEditAssetType(String name) {
		String editAssetTypeLinkLocator = getAssetTypeLinkLocator(name) + "/../../TD[2]/A[contains(text(),'Edit')]";
		if (selenium.isElementPresent(editAssetTypeLinkLocator)) {
			selenium.click(editAssetTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find an edit link to '" + name + "'");
		}
	}

	public void retireAssetTypeAttributes(List<Attribute> attributes) throws InterruptedException {
		deleteRetireAssetTypeAttributeHelper(attributes, false);
	}

	public List<String> getInspectionTypes() {
		List<String> result = new ArrayList<String>();
		int row = 2;
		int column = 1;
		String checkboxXpath = inspectionTypeTableXpath + "/tbody/tr/td/input[contains(@id,'selectType_')]";
		Number n = selenium.getXpathCount(checkboxXpath);
		int num = n.intValue();
		String checkboxLocator = "xpath=" + inspectionTypeTableXpath + "/tbody/tr[" + row + "]/td[" + column + "]/input[contains(@id,'selectType_')]";
		for(int i = 0; i < num; i++) {
			if(selenium.isChecked(checkboxLocator)) {
				String inspectionTypeNameLocator = checkboxLocator + "/../../TD[contains(@class,'name')]";
				String s = selenium.getText(inspectionTypeNameLocator);
				result.add(s);
			}
			checkboxLocator = checkboxLocator.replaceFirst("tr\\[" + (row+i), "tr[" + (row+i+1));
		}
		return result;
	}
}
