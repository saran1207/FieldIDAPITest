package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.ProductType;
import com.n4systems.fieldid.selenium.datatypes.SelectBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.TextFieldAttribute;
import com.n4systems.fieldid.selenium.datatypes.UnitOfMeasureAttribute;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Wait;

public class ManageProductTypes {
	FieldIdSelenium selenium;
	Misc misc;
	private String manageProductTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Types')]";
	private String productTypeTableXpath = "//DIV[@id='pageContent']/TABLE[@class='list']";
	private String productTypeTableLocator = "xpath=" + productTypeTableXpath;
	private String productTypesXpath = productTypeTableXpath + "/TBODY/TR/TD[1]/A";
	private String addProductTypeLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Add')]";
	private String productInformationHeaderLocator = "xpath=//FORM[@id='productTypeUpdate']/H2[contains(text(),'Product Information')]";
	private String groupSelectListLocator = "xpath=//SELECT[@id='productTypeUpdate_group']";
	private String nameTextFieldLocator = "xpath=//INPUT[@id='productTypeUpdate_name']";
	private String warningsTextFieldLocator = "xpath=//TEXTAREA[@id='productTypeUpdate_warnings']";
	private String instructionsTextFieldLocator = "xpath=//TEXTAREA[@id='productTypeUpdate_instructions']";
	private String cautionsURLLocator = "xpath=//INPUT[@id='productTypeUpdate_cautionsUrl']";
	private String hasManufacturerCertificateCheckboxLocator = "xpath=//INPUT[@id='productTypeUpdate_hasManufacturerCertificate']";
	private String manufacturerCertificateTextFieldLocator = "xpath=//TEXTAREA[@id='productTypeUpdate_manufacturerCertificateText']";
	private String productDescriptionTemplateTextFieldLocator = "xpath=//INPUT[@id='productTypeUpdate_descriptionTemplate']";
	private String uploadImageTextFieldLocator = "xpath=//INPUT[@id='uploadImage_upload']";
	private String attributesHeaderLocator = "xpath=//FORM[@id='productTypeUpdate']/H2[contains(text(),'Attributes')]";
	private String attributesAddButtonLocator = "xpath=//DIV[@id='infoFieldEditing']/DIV/INPUT[contains(@value,'Add')]";
	private String attributesUndoDeletesButtonLocator = "xpath=//DIV[@id='infoFieldEditing']/DIV/INPUT[contains(@value,'Undo Deletes')]";
	private String attachmentsHeaderLocator = "xpath=//FORM[@id='productTypeUpdate']/H2[contains(text(),'Attachments')]";
	private String attachAFileButtonLocator = "xpath=//FORM[@id='productTypeUpdate']/P[contains(@class,'actions')]/BUTTON[contains(text(),'Attach A File')]";
	private String cancelButtonLocator = "xpath=//FORM[@id='productTypeUpdate']/DIV[@class='formAction']/INPUT[contains(@value,'Cancel')]";
	private String saveButtonLocator = "xpath=//FORM[@id='productTypeUpdate']/DIV[@class='formAction']/INPUT[contains(@value,'Save')]";
	private String viewAllProductTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'View All')]";;
	private String viewProductTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'View') and not(contains(text(),'All'))]";;
	private String editProductTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Edit')]";
	private String inspectionTypesProductTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Inspection Types')]";;
	private String inspectionFrequenciesProductTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Inspection Frequencies')]";;
	private String subComponentsProductTypeTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI/A[contains(text(),'Sub-Components')]";;
	
	public ManageProductTypes(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	/**
	 * Checks for error messages on the page and that the
	 * Manage Product Types header exists.
	 */
	public void verifyManageProductTypesPage() {
		misc.info("Verify going to Manage Product Types page went okay.");
		misc.checkForErrorMessages("verifyManageProductTypesPage");
		if(!selenium.isElementPresent(manageProductTypesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Types'.");
		}
	}
	
	/**
	 * Assumes you are on the Manage Product Types page. Currently
	 * this page is not paginated. So this just grabs everything from
	 * the current page. If this becomes paginated, we will need to
	 * update this accordingly.
	 * 
	 * @return
	 */
	public List<String> getProductTypes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(productTypesXpath);
		int numProductTypes = n.intValue();
		int row = 1;
		String locator = productTypeTableLocator + "." + row + ".0";
		for(int i = 0; i < numProductTypes; i++, row++) {
			String productType = selenium.getTable(locator);
			result.add(productType.trim());
			locator = locator.replaceFirst("\\." + row + "\\.0", "." + (row+1) + ".0");
		}
		return result;
	}
	
	/**
	 * Clicks on the Add link on the Manage Product Types page.
	 * Also verifies the Add Product Type page appeared successfully.
	 */
	public void gotoAddProductType() {
		misc.info("Click the Add link for adding a product type");
		if(selenium.isElementPresent(addProductTypeLinkLocator)) {
			selenium.click(addProductTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
			verifyManageProductTypeAddPage();
		} else {
			fail("Could not find the link to add a product type");
		}
	}

	/**
	 * Check that all the usual fields are present.
	 */
	public void verifyManageProductTypeAddPage() {
		assertTrue(selenium.isElementPresent(productInformationHeaderLocator));
		assertTrue(selenium.isElementPresent(groupSelectListLocator));
		assertTrue(selenium.isElementPresent(nameTextFieldLocator));
		assertTrue(selenium.isElementPresent(warningsTextFieldLocator));
		assertTrue(selenium.isElementPresent(instructionsTextFieldLocator));
		assertTrue(selenium.isElementPresent(cautionsURLLocator));
		assertTrue(selenium.isElementPresent(hasManufacturerCertificateCheckboxLocator));
		assertTrue(selenium.isElementPresent(manufacturerCertificateTextFieldLocator));
		assertTrue(selenium.isElementPresent(productDescriptionTemplateTextFieldLocator));
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
	 * Click the Save button on the Add Product Type page.
	 */
	public void gotoSaveProductType() {
		misc.info("Click Save button");
		if(selenium.isElementPresent(saveButtonLocator)) {
			selenium.click(saveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the save button");
		}
	}

	/**
	 * Click the Cancel button on the Add Product Type page.
	 */
	public void gotoCancelProductType() {
		misc.info("Click Cancel button");
		if(selenium.isElementPresent(cancelButtonLocator)) {
			selenium.click(cancelButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the cancel button");
		}
	}

	/**
	 * This fills on the add product type page. It will check
	 * for null input and that all the usual fields are visible
	 * on the page. If any field in the ProductType object is
	 * set to null, it will skip it and use whatever the default
	 * values are.
	 * 
	 * Upload Image and Attach File do not currently work. Defining
	 * them in the ProductType will cause the test case to fail.
	 *  
	 * @param p
	 */
	public void setProductType(ProductType p) {
		assertNotNull(p);
		verifyManageProductTypeAddPage();
		if(p.getGroup() != null) {
			if(misc.isOptionPresent(groupSelectListLocator, p.getGroup())) {
				selenium.select(groupSelectListLocator, p.getGroup());
			} else {
				fail("Could not find the option '" + p.getGroup() + "' on the Group list");
			}
		}
		if(p.getName() != null) {
			selenium.type(nameTextFieldLocator, p.getName());
		}
		if(p.getWarnings() != null) {
			selenium.type(warningsTextFieldLocator, p.getWarnings());
		}
		if(p.getInstructions() != null) {
			selenium.type(instructionsTextFieldLocator, p.getInstructions());
		}
		if(p.getCautionsURL() != null) {
			selenium.type(cautionsURLLocator, p.getCautionsURL());
		}
		if(p.getHasManufacturerCertificate()) {
			selenium.check(hasManufacturerCertificateCheckboxLocator);
		} else {
			selenium.uncheck(hasManufacturerCertificateCheckboxLocator);
		}
		if(p.getManufacturerCertificateText() != null) {
			selenium.type(manufacturerCertificateTextFieldLocator, p.getManufacturerCertificateText());
		}
		if(p.getProductDescriptionTemplate() != null) {
			selenium.type(productDescriptionTemplateTextFieldLocator, p.getProductDescriptionTemplate());
		}
		if(p.getUploadImageFileName() != null) {
			fail("Not implemented");
		}
		if(p.getAttributes() != null && p.getAttributes().size() > 0) {
			setProductTypeAttributes(p.getAttributes());
		}
		if(p.getAttachments() != null && p.getAttachments().size() > 0) {
			fail("Not implemented");
		}
	}

	/**
	 * Set the product type attributes. Call by setProductType.
	 * 
	 * @param attributes
	 */
	private void setProductTypeAttributes(List<Attribute> attributes) {
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
		misc.info("Add a Unit Of Measure attribute to the current product Type being added.");
		int index = addAttribute(uoma);
		String defaultLocator = "xpath=//SELECT[@id='productTypeUpdate_infoFields_" + index + "__defaultUnitOfMeasure']";
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
		misc.info("Add a Combo Box attribute to the current product Type being added.");
		int attributeIndex = addAttribute(cba);
		addDropDowns(cba.getDropDowns(), attributeIndex);
	}

	/**
	 * Add the Select Box attribute including drop downs.
	 * 
	 * @param sba
	 */
	private void addSelectBoxAtribute(SelectBoxAttribute sba) {
		misc.info("Add a Select Box attribute to the current product Type being added.");
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
		misc.info("Add a text field attribute to the current product type being added");
		addAttribute(tfa);
	}
	
	private String getAttributeNameLocator(int index) {
		String locator = "xpath=//INPUT[@id='productTypeUpdate_infoFields_" + index + "__name']";
		return locator;
	}
	
	private String getAttributeTypeLocator(int index) {
		String locator = "xpath=//SELECT[@id='productTypeUpdate_infoFields_" + index + "__fieldType']";
		return locator;
	}
	
	private String getAttributeRequiredLocator(int index) {
		String locator = "xpath=//INPUT[@id='productTypeUpdate_infoFields_" + index + "__required']";
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
	 * Get the number of attributes added to the product type.
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
	 * Go to the View All tab on Manage Product Type. It assumes you
	 * are not already on the Manage Product Types page but are in
	 * one of the other tabs.
	 */
	public void gotoViewAllProductType() {
		misc.info("Click View All tab for Product Type");
		if(selenium.isElementPresent(viewAllProductTypeTabLinkLocator)) {
			selenium.click(viewAllProductTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the View tab on Manage Product Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoViewProductType() {
		misc.info("Click View tab for Product Type");
		if(selenium.isElementPresent(viewProductTypeTabLinkLocator)) {
			selenium.click(viewProductTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Edit tab on Manage Product Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoEditProductType() {
		misc.info("Click Edit tab for Product Type");
		if(selenium.isElementPresent(editProductTypeTabLinkLocator)) {
			selenium.click(editProductTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Inspection Types tab on Manage Product Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoInspectionTypesProductType() {
		misc.info("Click Inspection Types tab for Product Type");
		if(selenium.isElementPresent(inspectionTypesProductTypeTabLinkLocator)) {
			selenium.click(inspectionTypesProductTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Inspection Frequencies tab on Manage Product Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoInspectionFrequenciesProductType() {
		misc.info("Click Inspection Frequencies tab for Product Type");
		if(selenium.isElementPresent(inspectionFrequenciesProductTypeTabLinkLocator)) {
			selenium.click(inspectionFrequenciesProductTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Go to the Sub-Components tab on Manage Product Type. It assumes you
	 * are not already on this page but are in one of the other tabs.
	 */
	public void gotoSubComponentsProductType() {
		misc.info("Click Sub-Components tab for Product Type");
		if(selenium.isElementPresent(subComponentsProductTypeTabLinkLocator)) {
			selenium.click(subComponentsProductTypeTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Deletes the give list of product type attributes.
	 * Assumes you are  on the edit page for the product type.
	 * Assumes all the attributes exist. The first attribute
	 * which does not exist will cause the method to fail the
	 * test case.
	 * 
	 * @param attributes
	 * @throws InterruptedException 
	 */
	public void deleteProductTypeAttributes(List<Attribute> attributes) throws InterruptedException {
		deleteRetireProductTypeAttributeHelper(attributes, true);
	}
	
	/**
	 * Will delete or retire product type attributes. If the delete
	 * parameter is true, it assumes you want to Delete the attributes.
	 * Otherwise, it will attempt to Retire the attributes.
	 * 
	 * @param attributes
	 * @param delete
	 * @throws InterruptedException 
	 */
	private void deleteRetireProductTypeAttributeHelper(List<Attribute> attributes, boolean delete) throws InterruptedException {
		String linkText = "Retire";
		if(delete) {
			linkText = "Delete";
		}
		Iterator<Attribute> i = attributes.iterator();
		while(i.hasNext()) {
			Attribute a = i.next();
			String locator = "xpath=//INPUT[@value='" + a.getName() + "']";
			String deleteLocator = locator + "/../../DIV[contains(@class,'linkCol')]/A[contains(text(),'" + linkText + "')]";
			if(selenium.isElementPresent(deleteLocator)) {
				selenium.click(deleteLocator);
				if(delete) {
					waitForAttributeToBeDeleted(locator);
					verifyAttributeWasDeleted(a.getName());
				} else {
					waitForAttributeToBeRetired(deleteLocator);
					verifyAttributeWasRetired(a.getName());
				}
			} else {
				fail("Could not find a " + linkText + " link on the attribute '" + a.getName() + "' or could not find the attribute");
			}
		}
	}

	private void waitForAttributeToBeRetired(final String locator) {
		new Wait() {
			public boolean until() {
				return !selenium.isElementPresent(locator);
			}
		}.wait("Element '" + locator + "' is still invisible.");
	}

	/**
	 * When you link on Delete for a product type attribute it takes
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
			}.wait("Element '" + locator + "' was never set to invisible", Long.parseLong(Misc.defaultTimeout));
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
	 * Go to View a product type. Assumes you are on the View All
	 * page of Manage Product Types.
	 * 
	 * @param name
	 */
	public void gotoViewProductType(String name) {
		misc.info("Click the link to view the product type '" + name + "'");
		String productTypeLinkLocator = getProductTypeLinkLocator(name);
		if(selenium.isElementPresent(productTypeLinkLocator)) {
			selenium.click(productTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a link to '" + name + "'");
		}
	}
	
	private String getProductTypeLinkLocator(String name) {
		String locator = productTypeTableLocator + "/TBODY/TR/TD[1]/A[contains(text(),'" + name + "')]";
		return locator;
	}

	/**
	 * Go to Edit product type. This uses the Edit link
	 * on the View All page. This does not use the Edit
	 * tab on the Manage Product Type page.
	 * 
	 * @param name the name of the product type.
	 */
	public void gotoEditProductType(String name) {
		misc.info("Click the link to view the product type '" + name + "'");
		String editProductTypeLinkLocator = getProductTypeLinkLocator(name) + "/../../TD[2]/A[contains(text(),'Edit')]";
		if(selenium.isElementPresent(editProductTypeLinkLocator)) {
			selenium.click(editProductTypeLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find an edit link to '" + name + "'");
		}
	}

	public void retireProductTypeAttributes(List<Attribute> attributes) throws InterruptedException {
		deleteRetireProductTypeAttributeHelper(attributes, false);
	}
}
