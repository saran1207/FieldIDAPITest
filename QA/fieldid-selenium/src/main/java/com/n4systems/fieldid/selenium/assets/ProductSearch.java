package com.n4systems.fieldid.selenium.assets;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.ProductSelectDisplayColumns;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.n4systems.fieldid.selenium.misc.Search;
import com.thoughtworks.selenium.Selenium;
import org.junit.Assert;

public class ProductSearch extends Assert {
	Selenium selenium;
	Misc misc;
	Search search;
	
	// Locators
	private String productSearchPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Product Search')]";
	private String rfidNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_rfidNumber']";
	private String serialNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_serialNumber']";
	private String orderNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_orderNumber']";
	private String purchaseOrderTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_purchaseOrder']";
	private String referenceNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_referenceNumber']";
	private String productStatusSelectListLocator = "xpath=//SELECT[@id='reportForm_criteria_productStatus']";
	private String productTypeGroupSelectListLocator = "xpath=//SELECT[@id='productTypeGroup']";
	private String productTypeSelectListLocator = "xpath=//SELECT[@id='productType']";
	private String locationTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_location']";
	private String ownerTextFieldLocator = "xpath=//INPUT[@id='reportForm_owner_orgName']";
	private String chooseOwnerLinkLocator = "xpath=//A[contains(text(),'Choose')]";
	private String fromDateTextFieldLocator = "xpath=//INPUT[@id='reportForm_fromDate']";
	private String toDateTextFieldLocator = "xpath=//INPUT[@id='reportForm_toDate']";
	private String serialNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_serialnumber']";
	private String rfidNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_rfidnumber']";
	private String customerNameDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_customer']";
	private String divisionDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_division']";
	private String organizationDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_organization']";
	private String referenceNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_referencenumber']";
	private String productTypeGroupDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_producttypegroup']";
	private String productTypeDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_producttype']";
	private String productStatusDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_productstatus']";
	private String lastInspectionDateDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_lastinspdate']";
	private String networkLastInspectionDateDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_network_lastinspdate']";
	private String dateIdentifiedDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_identified']";
	private String identifiedByDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_identifiedby']";
	private String modifiedByDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_modifiedby']";
	private String commentsDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_comments']";
	private String descriptionDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_description']";
	private String locationDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_location']";
	private String safetyNetworkDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_published']";
	private String orderDescriptionDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_order_description']";
	private String orderNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_order_number']";
	private String purchaseOrderDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_product_search_purchaseorder']";
	private String productAttributesDynamicColumnLocator = "xpath=//DIV[@id='product_search_product_info_options']";
	private String productAttributesDynamicCheckBoxXpath = "//INPUT[@type='checkbox' and contains(@id,'chk_product_search_infooption_')]";
	private String runButtonLocator = "xpath=//INPUT[@id='reportForm_label_Run']";

	public ProductSearch(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
		search = misc.getSearch();
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header "Product Search" on the page.
	 */
	public void verifyAssetsPage() {
		misc.info("Verify going to Assets page went okay.");
		misc.checkForErrorMessages("verifyAssetsPage");
		if(!selenium.isElementPresent(productSearchPageHeaderLocator)) {
			fail("Could not find the header for 'Product Search'.");
		}
	}
	
	public void verifySearchCriteriaForm() {
		misc.info("Verify all the correct fields are present in the Search Criteria form");
		assertTrue(selenium.isElementPresent(rfidNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(serialNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(orderNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(purchaseOrderTextFieldLocator));
		assertTrue(selenium.isElementPresent(referenceNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(productStatusSelectListLocator));
		assertTrue(selenium.isElementPresent(productTypeGroupSelectListLocator));
		assertTrue(selenium.isElementPresent(productTypeSelectListLocator));
		assertTrue(selenium.isElementPresent(locationTextFieldLocator));
		assertTrue(selenium.isElementPresent(ownerTextFieldLocator));
		assertTrue(selenium.isElementPresent(chooseOwnerLinkLocator));
		assertTrue(selenium.isElementPresent(fromDateTextFieldLocator));
		assertTrue(selenium.isElementPresent(toDateTextFieldLocator));
	}
	
	public ProductSelectDisplayColumns getDisplayColumns() {
		ProductSelectDisplayColumns result = new ProductSelectDisplayColumns();

		result.setSerialNumber(misc.getCheckBoxValue(serialNumberDisplayColumnCheckBoxLocator));
		result.setrfidNumber(misc.getCheckBoxValue(rfidNumberDisplayColumnCheckBoxLocator));
		result.setCustomerName(misc.getCheckBoxValue(customerNameDisplayColumnCheckBoxLocator));
		result.setDivision(misc.getCheckBoxValue(divisionDisplayColumnCheckBoxLocator));
		result.setOrganization(misc.getCheckBoxValue(organizationDisplayColumnCheckBoxLocator));
		result.setReferenceNumber(misc.getCheckBoxValue(referenceNumberDisplayColumnCheckBoxLocator));
		result.setProductTypeGroup(misc.getCheckBoxValue(productTypeGroupDisplayColumnCheckBoxLocator));
		result.setProductType(misc.getCheckBoxValue(productTypeDisplayColumnCheckBoxLocator));
		result.setProductStatus(misc.getCheckBoxValue(productStatusDisplayColumnCheckBoxLocator));
		result.setLastInspectionDate(misc.getCheckBoxValue(lastInspectionDateDisplayColumnCheckBoxLocator));
		result.setNetworkLastInspectionDate(misc.getCheckBoxValue(networkLastInspectionDateDisplayColumnCheckBoxLocator));
		result.setDateIdentified(misc.getCheckBoxValue(dateIdentifiedDisplayColumnCheckBoxLocator));
		result.setIdentifiedBy(misc.getCheckBoxValue(identifiedByDisplayColumnCheckBoxLocator));
		result.setModifiedBy(misc.getCheckBoxValue(modifiedByDisplayColumnCheckBoxLocator));
		result.setComments(misc.getCheckBoxValue(commentsDisplayColumnCheckBoxLocator));
		result.setDescription(misc.getCheckBoxValue(descriptionDisplayColumnCheckBoxLocator));
		result.setLocation(misc.getCheckBoxValue(locationDisplayColumnCheckBoxLocator));
		result.setSafetyNetwork(misc.getCheckBoxValue(safetyNetworkDisplayColumnCheckBoxLocator));
		result.setOrderDescription(misc.getCheckBoxValue(orderDescriptionDisplayColumnCheckBoxLocator));
		result.setOrderNumber(misc.getCheckBoxValue(orderNumberDisplayColumnCheckBoxLocator));
		result.setPurchaseOrder(misc.getCheckBoxValue(purchaseOrderDisplayColumnCheckBoxLocator));
		
		return result;
	}
	
	public void setDisplayColumns(ProductSelectDisplayColumns sdc) {
		misc.setCheckBoxValue(serialNumberDisplayColumnCheckBoxLocator, sdc.getSerialNumber());
		misc.setCheckBoxValue(rfidNumberDisplayColumnCheckBoxLocator, sdc.getrfidNumber());
		misc.setCheckBoxValue(customerNameDisplayColumnCheckBoxLocator, sdc.getCustomerName());
		misc.setCheckBoxValue(divisionDisplayColumnCheckBoxLocator, sdc.getDivision());
		misc.setCheckBoxValue(organizationDisplayColumnCheckBoxLocator, sdc.getOrganization());
		misc.setCheckBoxValue(referenceNumberDisplayColumnCheckBoxLocator, sdc.getReferenceNumber());
		misc.setCheckBoxValue(productTypeGroupDisplayColumnCheckBoxLocator, sdc.getProductTypeGroup());
		misc.setCheckBoxValue(productTypeDisplayColumnCheckBoxLocator, sdc.getProductType());
		misc.setCheckBoxValue(productStatusDisplayColumnCheckBoxLocator, sdc.getProductStatus());
		misc.setCheckBoxValue(lastInspectionDateDisplayColumnCheckBoxLocator, sdc.getLastInspectionDate());
		misc.setCheckBoxValue(networkLastInspectionDateDisplayColumnCheckBoxLocator, sdc.getNetworkLastInspectionDate());
		misc.setCheckBoxValue(dateIdentifiedDisplayColumnCheckBoxLocator, sdc.getDateIdentified());
		misc.setCheckBoxValue(identifiedByDisplayColumnCheckBoxLocator, sdc.getIdentifiedBy());
		misc.setCheckBoxValue(modifiedByDisplayColumnCheckBoxLocator, sdc.getModifiedBy());
		misc.setCheckBoxValue(commentsDisplayColumnCheckBoxLocator, sdc.getComments());
		misc.setCheckBoxValue(descriptionDisplayColumnCheckBoxLocator, sdc.getDescription());
		misc.setCheckBoxValue(locationDisplayColumnCheckBoxLocator, sdc.getLocation());
		misc.setCheckBoxValue(safetyNetworkDisplayColumnCheckBoxLocator, sdc.getSafetyNetwork());
		misc.setCheckBoxValue(orderDescriptionDisplayColumnCheckBoxLocator, sdc.getOrderDescription());
		misc.setCheckBoxValue(orderNumberDisplayColumnCheckBoxLocator, sdc.getOrderNumber());
		misc.setCheckBoxValue(purchaseOrderDisplayColumnCheckBoxLocator, sdc.getPurchaseOrder());
	}
	
	public void setSearchCriteria(AssetSearchCriteria asc) {
		verifySearchCriteriaForm();
		if(asc.getRFIDNumber() != null) {
			selenium.type(rfidNumberTextFieldLocator, asc.getRFIDNumber());
		}
		
		if(asc.getSerialNumber() != null) {
			selenium.type(serialNumberTextFieldLocator, asc.getSerialNumber());
		}
		
		if(asc.getOrderNumber() != null) {
			selenium.type(orderNumberTextFieldLocator, asc.getOrderNumber());
		}
		
		if(asc.getPurchaseOrder() != null) {
			selenium.type(purchaseOrderTextFieldLocator, asc.getPurchaseOrder());
		}
		
		if(asc.getReferenceNumber() != null) {
			selenium.type(referenceNumberTextFieldLocator, asc.getReferenceNumber());
		}
		
		if(asc.getProductStatus() != null) {
			if(misc.isOptionPresent(productStatusSelectListLocator, asc.getProductStatus())) {
				selenium.select(productStatusSelectListLocator, asc.getProductStatus());
			} else {
				fail("Could not find the product status '" + asc.getProductStatus() + "'");
			}
		}
		
		if(asc.getProductTypeGroup() != null) {
			if(misc.isOptionPresent(productTypeGroupSelectListLocator, asc.getProductTypeGroup())) {
				selenium.select(productTypeGroupSelectListLocator, asc.getProductTypeGroup());
				search.waitForDisplayColumnsToUpdate(Misc.defaultTimeout);
			} else {
				fail("Could not find the product type group '" + asc.getProductTypeGroup() + "'");
			}
		}
		
		if(asc.getProductType() != null) {
			if(misc.isOptionPresent(productTypeSelectListLocator, asc.getProductType())) {
				selenium.select(productTypeSelectListLocator, asc.getProductType());
				search.waitForDisplayColumnsToUpdate(Misc.defaultTimeout);
			} else {
				fail("Could not find the product type  '" + asc.getProductType() + "'");
			}
		}
		
		if(asc.getLocation() != null) {
			selenium.type(locationTextFieldLocator, asc.getLocation());
		}
		
		if(asc.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(asc.getOwner());
			misc.gotoSelectOwner();
		}
		
		if(asc.getFromDate() != null) {
			selenium.type(fromDateTextFieldLocator, asc.getFromDate());
		}
		
		if(asc.getToDate() != null) {
			selenium.type(toDateTextFieldLocator, asc.getToDate());
		}
	}
	
	public List<String> getProductStatuses() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(productStatusSelectListLocator)) {
			String tmp[] = selenium.getSelectOptions(productStatusSelectListLocator);
			for(int i = 0; i < tmp.length; i++) {
				result.add(tmp[i]);
			}
		}
		return result;
	}
	
	public List<String> getProductTypeGroups() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(productTypeGroupSelectListLocator)) {
			String tmp[] = selenium.getSelectOptions(productTypeGroupSelectListLocator);
			for(int i = 0; i < tmp.length; i++) {
				result.add(tmp[i]);
			}
		}
		return result;
	}
	
	public List<String> getProductTypes() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(productTypeSelectListLocator)) {
			String tmp[] = selenium.getSelectOptions(productTypeSelectListLocator);
			for(int i = 0; i < tmp.length; i++) {
				result.add(tmp[i]);
			}
		}
		return result;
	}

	public List<String> getDynamicProductAttributeDisplayColumns() {
		List<String> result = search.getAttributesDisplayColumns(productAttributesDynamicColumnLocator, productAttributesDynamicCheckBoxXpath);
		return result;
	}

	public void gotoRunProductSearch() {
		misc.info("Click Run buttom");
		if(selenium.isElementPresent(runButtonLocator)) {
			selenium.click(runButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Run button for Product Search");
		}
	}
}

