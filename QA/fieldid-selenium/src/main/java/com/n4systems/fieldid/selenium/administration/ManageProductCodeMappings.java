package com.n4systems.fieldid.selenium.administration;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.ProductCodeMapping;
import com.n4systems.fieldid.selenium.datatypes.SelectBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.TextFieldAttribute;
import com.n4systems.fieldid.selenium.datatypes.UnitOfMeasureAttribute;
import com.n4systems.fieldid.selenium.identify.Identify;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageProductCodeMappings extends Assert {
	Selenium selenium;
	Misc misc;
	Identify identify;
	
	private String manageProductCodeMappingsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Code Mappings')]";
	private String addProductCodeMappingLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Add')]";
	private String productCodeTextFieldLocator = "xpath=//INPUT[@id='productCodeMappingEdit_productCode']";
	private String referenceNumberTextFieldLocator = "xpath=//INPUT[@id='productCodeMappingEdit_customerRefNumber']";
	private String productTypeSelectListLocator = "xpath=//SELECT[@id='productType']";
	private String saveButtonLocator = "xpath=//INPUT[@id='productCodeMappingEdit_hbutton_save']";
	private String cancelButtonLocator = "xpath=//A[contains(text(),'Cancel')]";
	
	public ManageProductCodeMappings(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
		identify = new Identify(selenium, misc);
	}

	public void verifyManageProductCodeMappingsPage() {
		misc.info("Verify going to Manage Product Code Mappings page went okay.");
		misc.checkForErrorMessages("verifyManageProductCodeMappingsPage");
		if(!selenium.isElementPresent(manageProductCodeMappingsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Code Mappings'.");
		}
	}

	public void gotoAddProductCodeMapping() {
		misc.info("Click Add to add a product code mapping");
		if(selenium.isElementPresent(addProductCodeMappingLinkLocator)) {
			selenium.click(addProductCodeMappingLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
			verifyAddProductCodeMapping();
		} else {
			fail("Could not find the Add link to add a product code mapping");
		}
	}

	public void verifyAddProductCodeMapping() {
		assertTrue(selenium.isElementPresent(productCodeTextFieldLocator));
		assertTrue(selenium.isElementPresent(referenceNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(productTypeSelectListLocator));
		assertTrue(selenium.isElementPresent(saveButtonLocator));
		assertTrue(selenium.isElementPresent(cancelButtonLocator));
	}

	public void setProductCodeMapping(ProductCodeMapping pcm) {
		misc.info("Fill in a Product Code Mapping");
		assertNotNull(pcm);
		verifyAddProductCodeMapping();
		if(pcm.getProductCode() != null) {
			selenium.type(productCodeTextFieldLocator, pcm.getProductCode());
		}
		if(pcm.getReferenceNumber() != null) {
			selenium.type(referenceNumberTextFieldLocator, pcm.getReferenceNumber());
		}
		if(pcm.getProductType() != null) {
			setProductTypeInProductCodeMapping(pcm.getProductType());
			Map<Attribute,String> pta = pcm.getProductAttributes();
			if(pta != null) {
				Set<Attribute> attributes = pta.keySet();
				Iterator<Attribute> i = attributes.iterator();
				while(i.hasNext()) {
					Attribute attribute = i.next();
					String value = pta.get(attribute);
					String attributeName = attribute.getName();
					if(attribute instanceof UnitOfMeasureAttribute) {
						String id = getInputIDForProductTypeUnitOfMeasureAttribute(attributeName);
						identify.setUnitOfMeasure(id, value);
					} else if(attribute instanceof TextFieldAttribute) {
						String id = getInputIDForProductTypeTextFieldAttribute(attributeName);
						String locator = "xpath=//INPUT[@id='" + id + "']";
						if(selenium.isElementPresent(locator)) {
							selenium.type(locator, value);
						} else {
							fail("Could not locate the Text Field for '" + attributeName + "'");
						}
					} else if(attribute instanceof SelectBoxAttribute || attribute instanceof ComboBoxAttribute) {
						String id = getInputIDForProductTypeSelectComboBoxAttributes(attributeName);
						String locator = "xpath=//SELECT[@id='" + id + "']";
						if(selenium.isElementPresent(locator)) {
							if(misc.isOptionPresent(locator, value)) {
								selenium.select(locator, value);
							} // fail silently if the value is not there
						} else {
							fail("Could not locate the select list for '" + attributeName + "'");
						}
					} else {
						fail("Unknown attribute type '" + attribute.getType() + "'");
					}
				}
			}
		}
	}

	public String getInputIDForProductTypeUnitOfMeasureAttribute(String name) {
		String id = null;
		String locator = "xpath=//DIV[@id='infoOptions']/DIV[contains(@class,'infoSet')]/LABEL[@class='label' and contains(text(),'" + name + "')]/../DIV[contains(@class,'fieldHolder')]/SPAN/INPUT[contains(@class,'attribute')]";
		String attributeLocator = locator + "@id";
		if(selenium.isElementPresent(locator)) {
			id = selenium.getAttribute(attributeLocator);
		} else {
			fail("Could not find an input for the attribute '" + name +"'");
		}
		return id;
	}

	public String getInputIDForProductTypeTextFieldAttribute(String name) {
		String id = null;
		String locator = "xpath=//DIV[@id='infoOptions']/DIV[contains(@class,'infoSet')]/LABEL[@class='label' and contains(text(),'" + name + "')]/../SPAN[contains(@class,'fieldHolder')]/INPUT[contains(@class,'attribute')]";
		String attributeLocator = locator + "@id";
		if(selenium.isElementPresent(locator)) {
			id = selenium.getAttribute(attributeLocator);
		} else {
			fail("Could not find an input for the attribute '" + name +"'");
		}
		return id;
	}

	public String getInputIDForProductTypeSelectComboBoxAttributes(String name) {
		String id = null;
		String locator = "xpath=//LABEL[contains(@class,'label') and contains(text(),'" + name + "')]/../SPAN[contains(@class,'fieldHolder')]/SELECT[contains(@class,'attribute')]";
		String attributeLocator = locator + "@id";
		if(selenium.isElementPresent(locator)) {
			id = selenium.getAttribute(attributeLocator);
		} else {
			fail("Could not find an input for the attribute '" + name +"'");
		}
		return id;
	}

	public void setProductTypeInProductCodeMapping(String name) {
		misc.info("Set the Product Type to '" + name + "'");
		if(selenium.isElementPresent(productTypeSelectListLocator)) {
			if(misc.isOptionPresent(productTypeSelectListLocator, name)) {
				selenium.select(productTypeSelectListLocator, name);
				misc.sleep(3000);	// TODO figure out how to detect update is finished
			} else {
				fail("Could not find '" + name + "' in the list of product types");
			}
		} else {
			fail("Could not find the Product Type select list");
		}
	}

	public void gotoSaveProductCodeMapping() {
		misc.info("Click Save");
		if(selenium.isElementPresent(saveButtonLocator)) {
			selenium.click(saveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button");
		}
	}
}
