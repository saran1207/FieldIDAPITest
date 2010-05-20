package com.n4systems.fieldid.selenium.assets.page;

import static org.junit.Assert.*;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class AssetPage {
	FieldIdSelenium selenium;
	Misc misc;
	
	// Locators
	private String assetHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Asset - ')]";
	private String assetEditTabLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI[not(contains(@class,'selected'))]/A[contains(text(),'Edit')]";
	private String assetTraceablityLinkLocator = "xpath=//A[contains(text(),'Traceability')]";
	private String productSummaryHeaderLocator = "xpath=//H2[contains(text(),'Product Summary')]";
	private String serialNumberLabelLocator = "xpath=//LABEL[contains(text(),'Serial Number')]";
	private String rfidNumberLabelLocator = "xpath=//LABEL[contains(text(),'RFID Number')]";
	private String publishedLabelLocator = "xpath=//LABEL[contains(text(),'Published Over Safety Network')]";
	private String productTypeLabelLocator = "xpath=//LABEL[contains(text(),'Product Type')]";
	private String productStatusLabelLocator = "xpath=//LABEL[contains(text(),'Product Status')]";
	private String identifiedLabelLocator = "xpath=//LABEL[contains(text(),'Identified')]";
	private String identifiedByLabelLocator = "xpath=//LABEL[contains(text(),'Identified By')]";
	private String modifiedByLabelLocator = "xpath=//LABEL[contains(text(),'Modified By')]";
	private String customerInformationHeaderLocator = "xpath=//H2[contains(text(),'Customer Information') or contains(text(),'Site Information')]";
	private String organizationLabelLocator = "xpath=//LABEL[contains(text(),'Organization')]";
	private String customerNameLabelLocator = "xpath=//LABEL[contains(text(),'Customer Name') or contains(text(),'Job Site Name')]";
	private String divisionLabelLocator = "xpath=//LABEL[contains(text(),'Division')]";
	private String locationLabelLocator = "xpath=//LABEL[contains(text(),'Location')]";
	private String referenceNumberLabelLocator = "xpath=//LABEL[contains(text(),'Reference Number')]";
	private String purchaseOrderLabelLocator = "xpath=//LABEL[contains(text(),'Purchase Order')]";
	private String lastInspectionHeaderLocator = "xpath=//H2[contains(text(),'Last Inspection')]";
	private String manageInspectionsLinkLocator = "xpath=//A[@id='manageInspections']";
	private String editAssetSerialNumberTextFieldLocator = "xpath=//INPUT[@id='serialNumberText']";
	private String editAssetGenerateLinkLocator = "xpath=//A[contains(text(),'generate')]";
	private String editAssetRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='rfidNumber']";
	private String editAssetReferenceNumberTextFieldLocator = "xpath=//INPUT[@id='customerRefNumber']";
	private String editAssetLocationTextFieldLocator = "xpath=//INPUT[@id='location']";
	private String editAssetProductStatusSelectListLocator = "xpath=//SELECT[@id='productUpdate_productStatus']";
	private String editAssetPurchaseOrderTextFieldLocator = "xpath=//INPUT[@id='productUpdate_purchaseOrder']";
	private String editAssetIdentifiedTextFieldLocator = "xpath=//INPUT[@id='identified']";
	private String editAssetProductTypeTextFieldLocator = "xpath=//SELECT[@id='productType']";
	private String editAssetOwnerTextFieldLocator = "xpath=//INPUT[@id='productUpdate_owner_orgName']";
	private String editAssetChooseLinkLocator = "xpath=//A[contains(text(),'Choose')]";
	private String editAssetAttachAFileButtonLocator = "xpath=//BUTTON[contains(text(),'Attach A File')]";
	private String editAssetSaveButtonLocator = "xpath=//INPUT[@id='saveButton']";
	private String editAssetSaveAndInspectButtonLocator = "xpath=//INPUT[@id='saveAndInspButton']";
	private String editAssetCancelLinkLocator = "xpath=//A[text()='Cancel']";
	private String editAssetMergeLinkLocator = "xpath=//A[text()='Merge']";
	private String editAssetDeleteLinkLocator = "xpath=//A[text()='Delete']";
	private String editAssetPublishOverSafetyNetworkSelectListLocator = "xpath=//SELECT[@id='productUpdate_publishedState']";
	private String editAssetCommentTextFieldLocator = "xpath=//TEXTAREA[@id='comments']";

	public AssetPage(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header containing "Asset - ${serialNumber}" on the page. Does
	 * not check the dynamic contents of the Asset View page. For checking
	 * the dynamic portion is verifyAssetViewPageDynamicContents().
	 * 
	 * @param serialNumber 
	 */
	public void verifyAssetViewPage(String serialNumber) {
		misc.checkForErrorMessages("verifyAssetViewPage");
		verifyAssetPageHeader(serialNumber);
		verifyAssetViewPageStaticContents();
		String serialNumberSpanLocator = "xpath=//SPAN[contains(text(),'" + serialNumber + "')]";
		if(!selenium.isElementPresent(serialNumberSpanLocator)) {
			fail("Could not find the serial number '" + serialNumber + "' in the Product Summary");
		}
	}
	
	/**
	 * Checks to see if the Asset - ${serialNumber}" header exists.
	 * 
	 * @param serialNumber
	 */
	private void verifyAssetPageHeader(String serialNumber) {
		if(!(selenium.isElementPresent(assetHeaderLocator) && selenium.getText(assetHeaderLocator).contains(serialNumber))) {
			fail("Could not find the header for 'Asset - " + serialNumber + "'.");
		}
	}
	
	/**
	 * Compares the given Product information to the text on the Asset View page.
	 * If any field in the Product object is null or the string is empty it will
	 * not be checked.
	 *  
	 * @param p
	 */
	public void verifyAssetViewPageDynamicContents(Product p) {
		if(p.getSerialNumber() != null && !p.getSerialNumber().equals("")) {
			String locator = serialNumberLabelLocator + "/../SPAN[contains(text(),'" + p.getSerialNumber() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getRFIDNumber() != null && !p.getRFIDNumber().equals("")) {
			String locator = rfidNumberLabelLocator + "/../SPAN[contains(text(),'" + p.getRFIDNumber() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getPublished()) {
			String locator = publishedLabelLocator + "/../SPAN[contains(text(),'Published') and not(contains(text(),'Not'))]";
			assertTrue(selenium.isElementPresent(locator));
		} else {
			String locator = publishedLabelLocator + "/../SPAN[contains(text(),'Not Published')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getProductType() != null && !p.getProductType().equals("")) {
			String locator = productTypeLabelLocator + "/../SPAN[contains(text(),'" + p.getProductType() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getProductStatus() != null && !p.getProductStatus().equals("")) {
			String locator = productStatusLabelLocator + "/../SPAN[contains(text(),'" + p.getProductStatus() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getIdentified() != null && !p.getIdentified().equals("")) {
			String locator = identifiedLabelLocator + "/../SPAN[contains(text(),'" + p.getIdentified() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getOwner() != null) {
			String organization = p.getOwner().getOrganization();
			if(organization != null && !organization.equals("")) {
				String locator = organizationLabelLocator + "/../SPAN[contains(text(),'" + organization + "')]";
				assertTrue(selenium.isElementPresent(locator));
			}
			String customer = p.getOwner().getCustomer();
			if(customer != null && !customer.equals("")) {
				String locator = customerNameLabelLocator + "/../SPAN[contains(text(),'" + customer + "')]";
				assertTrue(selenium.isElementPresent(locator));
			}
			String division = p.getOwner().getDivision();
			if(division != null && !division.equals("")) {
				String locator = divisionLabelLocator + "/../SPAN[contains(text(),'" + division + "')]";
				assertTrue(selenium.isElementPresent(locator));
			}
		}
		if(p.getLocation() != null && !p.getLocation().equals("")) {
			String locator = locationLabelLocator + "/../SPAN[contains(text(),'" + p.getLocation() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getReferenceNumber() != null && !p.getReferenceNumber().equals("")) {
			String locator = referenceNumberLabelLocator + "/../SPAN[contains(text(),'" + p.getReferenceNumber() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
		if(p.getPurchaseOrder() != null && !p.getPurchaseOrder().equals("")) {
			String locator = purchaseOrderLabelLocator + "/../SPAN[contains(text(),'" + p.getPurchaseOrder() + "')]";
			assertTrue(selenium.isElementPresent(locator));
		}
	}

	private void verifyAssetViewPageStaticContents() {
		assertTrue(selenium.isElementPresent(productSummaryHeaderLocator));
		assertTrue(selenium.isElementPresent(serialNumberLabelLocator));
		assertTrue(selenium.isElementPresent(rfidNumberLabelLocator));
		assertTrue(selenium.isElementPresent(publishedLabelLocator));
		assertTrue(selenium.isElementPresent(productTypeLabelLocator));
		assertTrue(selenium.isElementPresent(productStatusLabelLocator));
		assertTrue(selenium.isElementPresent(identifiedLabelLocator));
		assertTrue(selenium.isElementPresent(identifiedByLabelLocator));
		assertTrue(selenium.isElementPresent(modifiedByLabelLocator));
		assertTrue(selenium.isElementPresent(customerInformationHeaderLocator));
		assertTrue(selenium.isElementPresent(organizationLabelLocator));
		assertTrue(selenium.isElementPresent(customerNameLabelLocator));
		assertTrue(selenium.isElementPresent(divisionLabelLocator));
		assertTrue(selenium.isElementPresent(locationLabelLocator));
		assertTrue(selenium.isElementPresent(referenceNumberLabelLocator));
		assertTrue(selenium.isElementPresent(purchaseOrderLabelLocator));
		assertTrue(selenium.isElementPresent(lastInspectionHeaderLocator));
		assertTrue(selenium.isElementPresent(manageInspectionsLinkLocator));
	}

	public void gotoEdit() {
		if(selenium.isElementPresent(assetEditTabLinkLocator)) {
			selenium.click(assetEditTabLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Edit tab for the current asset");
		}
	}
	
	public void gotoTraceability() {
		selenium.click(assetTraceablityLinkLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
		
	}

	public void verifyAssetEditPage(String serialNumber) {
		misc.checkForErrorMessages("verifyAssetEditPage");
		verifyAssetPageHeader(serialNumber);
		verifyAssetEditPageContents();
	}

	private void verifyAssetEditPageContents() {
		verifyAssetEditPageDynamicContents();
	}

	private void verifyAssetEditPageDynamicContents() {
		assertTrue(selenium.isElementPresent(editAssetSerialNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetGenerateLinkLocator));
		assertTrue(selenium.isElementPresent(editAssetRFIDNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetReferenceNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetLocationTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetProductStatusSelectListLocator));
		assertTrue(selenium.isElementPresent(editAssetPurchaseOrderTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetIdentifiedTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetProductTypeTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetOwnerTextFieldLocator));
		assertTrue(selenium.isElementPresent(editAssetChooseLinkLocator));
		assertTrue(selenium.isElementPresent(editAssetAttachAFileButtonLocator));
		assertTrue(selenium.isElementPresent(editAssetSaveButtonLocator));
		assertTrue(selenium.isElementPresent(editAssetSaveAndInspectButtonLocator));
		assertTrue(selenium.isElementPresent(editAssetCancelLinkLocator));
		assertTrue(selenium.isElementPresent(editAssetMergeLinkLocator));
		assertTrue(selenium.isElementPresent(editAssetDeleteLinkLocator));
	}

	public void gotoSaveAndInspect() {
		if(selenium.isElementPresent(editAssetSaveAndInspectButtonLocator)) {
			selenium.click(editAssetSaveAndInspectButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not locate the Save And Inspect button");
		}
	}

	public void setAssetForm(Product p) {
		this.verifyAssetEditPageContents();
		if(p.getSafetyNetworkRegistration() != null) {
			; // TODO handle setting the safety network
		}
		if(p.getPublished() == true) {
			selenium.select(editAssetPublishOverSafetyNetworkSelectListLocator, "Publish");
		} else {
			selenium.select(editAssetPublishOverSafetyNetworkSelectListLocator, "Do Not Publish");
		}
		if(p.getSerialNumber() != null) {
			selenium.type(editAssetSerialNumberTextFieldLocator, p.getSerialNumber());
		}
		if(p.getRFIDNumber() != null) {
			selenium.type(editAssetRFIDNumberTextFieldLocator, p.getRFIDNumber());
		}
		if(p.getReferenceNumber() != null) {
			selenium.type(editAssetReferenceNumberTextFieldLocator, p.getReferenceNumber());
		}
		if(p.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(p.getOwner());
			misc.gotoSelectOwner();
		}
		if(p.getLocation() != null) {
			selenium.type(this.editAssetLocationTextFieldLocator, p.getLocation());
		}
		if(p.getProductStatus() != null) {
			if(misc.isOptionPresent(editAssetProductStatusSelectListLocator, p.getProductStatus())) {
				selenium.select(this.editAssetProductStatusSelectListLocator, p.getProductStatus());
			} else {
				fail("Could not find the product status '" + p.getProductStatus() + "'");
			}
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type(this.editAssetPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type(this.editAssetIdentifiedTextFieldLocator, p.getIdentified());
		}
		if(p.getProductType() != null) {
			selenium.type(this.editAssetProductTypeTextFieldLocator, p.getProductType());
		}
		if(p.getComments() != null) {
			selenium.type(editAssetCommentTextFieldLocator, p.getComments());
		}
	}

	public void verifyAttribute(String fieldName, String value) {
		assertEquals(value, selenium.getText("css=#attributes .fieldValue[infoFieldName='"+ fieldName.replace("'", "/'") + "']"));
	}
}
