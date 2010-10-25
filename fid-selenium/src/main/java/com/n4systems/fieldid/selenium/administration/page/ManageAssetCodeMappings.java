package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.AssetCodeMapping;
import com.n4systems.fieldid.selenium.datatypes.SelectBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.TextFieldAttribute;
import com.n4systems.fieldid.selenium.datatypes.UnitOfMeasureAttribute;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageAssetCodeMappings {
	FieldIdSelenium selenium;
	MiscDriver misc;
	IdentifyPageDriver identify;
	
	private String manageAssetCodeMappingsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Asset Code Mappings')]";
	private String addAssetCodeMappingLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Add')]";
	private String assetCodeTextFieldLocator = "xpath=//INPUT[@id='assetCodeMappingEdit_assetCode']";
	private String referenceNumberTextFieldLocator = "xpath=//INPUT[@id='assetCodeMappingEdit_customerRefNumber']";
	private String assetTypeSelectListLocator = "xpath=//SELECT[@id='assetType']";
	private String saveButtonLocator = "xpath=//INPUT[@id='assetCodeMappingEdit_hbutton_save']";
	private String cancelButtonLocator = "xpath=//A[contains(text(),'Cancel')]";
	
	public ManageAssetCodeMappings(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
		identify = new IdentifyPageDriver(selenium, misc);
	}

	public void verifyManageAssetCodeMappingsPage() {
		misc.checkForErrorMessages("verifyManageAssetCodeMappingsPage");
		if(!selenium.isElementPresent(manageAssetCodeMappingsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Asset Code Mappings'.");
		}
	}

	public void gotoAddAssetCodeMapping() {
		if(selenium.isElementPresent(addAssetCodeMappingLinkLocator)) {
			selenium.click(addAssetCodeMappingLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
			verifyAddAssetCodeMapping();
		} else {
			fail("Could not find the Add link to add an asset code mapping");
		}
	}

	public void verifyAddAssetCodeMapping() {
		assertTrue(selenium.isElementPresent(assetCodeTextFieldLocator));
		assertTrue(selenium.isElementPresent(referenceNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(assetTypeSelectListLocator));
		assertTrue(selenium.isElementPresent(saveButtonLocator));
		assertTrue(selenium.isElementPresent(cancelButtonLocator));
	}

	public void setAssetCodeMapping(AssetCodeMapping pcm) {
		assertNotNull(pcm);
		verifyAddAssetCodeMapping();
		if(pcm.getAssetCode() != null) {
			selenium.type(assetCodeTextFieldLocator, pcm.getAssetCode());
		}
		if(pcm.getReferenceNumber() != null) {
			selenium.type(referenceNumberTextFieldLocator, pcm.getReferenceNumber());
		}
		if(pcm.getAssetType() != null) {
			setAssetTypeInAssetCodeMapping(pcm.getAssetType());
			Map<Attribute,String> pta = pcm.getAssetAttributes();
			if(pta != null) {
				Set<Attribute> attributes = pta.keySet();
				Iterator<Attribute> i = attributes.iterator();
				while(i.hasNext()) {
					Attribute attribute = i.next();
					String value = pta.get(attribute);
					String attributeName = attribute.getName();
					if(attribute instanceof UnitOfMeasureAttribute) {
						String id = getInputIDForAssetTypeUnitOfMeasureAttribute(attributeName);
						identify.setUnitOfMeasure(id, value);
					} else if(attribute instanceof TextFieldAttribute) {
						String id = getInputIDForAssetTypeTextFieldAttribute(attributeName);
						String locator = "xpath=//INPUT[@id='" + id + "']";
						if(selenium.isElementPresent(locator)) {
							selenium.type(locator, value);
						} else {
							fail("Could not locate the Text Field for '" + attributeName + "'");
						}
					} else if(attribute instanceof SelectBoxAttribute || attribute instanceof ComboBoxAttribute) {
						String id = getInputIDForAssetTypeSelectComboBoxAttributes(attributeName);
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

	public String getInputIDForAssetTypeUnitOfMeasureAttribute(String name) {
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

	public String getInputIDForAssetTypeTextFieldAttribute(String name) {
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

	public String getInputIDForAssetTypeSelectComboBoxAttributes(String name) {
		String id = null;
		String locator = "xpath=//LABEL[contains(@class,'label') and contains(text(),'" + name + "')]/../SPAN[contains(@class,'fieldHolder')]/SELECT[contains(@class,'attribute')]";
		String attributeLocator = locator + "@id";
		selenium.waitForElementToBePresent(locator);
		if(selenium.isElementPresent(locator)) {
			id = selenium.getAttribute(attributeLocator);
		} else {
			fail("Could not find an input for the attribute '" + name +"'");
		}
		return id;
	}

	public void setAssetTypeInAssetCodeMapping(String name) {
		if(selenium.isElementPresent(assetTypeSelectListLocator)) {
			if(misc.isOptionPresent(assetTypeSelectListLocator, name)) {
				selenium.select(assetTypeSelectListLocator, name);
				selenium.waitForAjax();
			} else {
				fail("Could not find '" + name + "' in the list of asset types");
			}
		} else {
			fail("Could not find the Asset Type select list");
		}
	}

	public void gotoSaveAssetCodeMapping() {
		if(selenium.isElementPresent(saveButtonLocator)) {
			selenium.click(saveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button");
		}
	}
}
