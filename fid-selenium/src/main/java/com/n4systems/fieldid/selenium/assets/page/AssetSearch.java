package com.n4systems.fieldid.selenium.assets.page;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.AssetSelectDisplayColumns;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.misc.Search;

public class AssetSearch {
	FieldIdSelenium selenium;
	MiscDriver misc;
	Search search;
	
	// Locators
	private String assetSearchPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Asset Search')]";
	private String rfidNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_rfidNumber']";
	private String serialNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_serialNumber']";
	private String orderNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_orderNumber']";
	private String purchaseOrderTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_purchaseOrder']";
	private String referenceNumberTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_referenceNumber']";
	private String assetStatusSelectListLocator = "xpath=//SELECT[@id='reportForm_criteria_assetStatus']";
	private String assetTypeGroupSelectListLocator = "xpath=//SELECT[@id='assetTypeGroup']";
	private String assetTypeSelectListLocator = "xpath=//SELECT[@id='assetType']";
	private String locationTextFieldLocator = "xpath=//INPUT[@id='reportForm_criteria_location']";
	private String ownerTextFieldLocator = "xpath=//INPUT[@id='reportForm_owner_orgName']";
	private String chooseOwnerLinkLocator = "xpath=//A[contains(text(),'Choose')]";
	private String fromDateTextFieldLocator = "xpath=//INPUT[@id='reportForm_fromDate']";
	private String toDateTextFieldLocator = "xpath=//INPUT[@id='reportForm_toDate']";
	private String serialNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_serialnumber']";
	private String rfidNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_rfidnumber']";
	private String customerNameDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_customer']";
	private String divisionDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_division']";
	private String organizationDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_organization']";
	private String referenceNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_referencenumber']";
	private String assetTypeGroupDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_assettypegroup']";
	private String assetTypeDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_assettype']";
	private String assetStatusDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_assetstatus']";
	private String lastEventDateDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_lasteventdate']";
	private String networkLastEventDateDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_network_lasteventdate']";
	private String dateIdentifiedDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_identified']";
	private String identifiedByDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_identifiedby']";
	private String modifiedByDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_modifiedby']";
	private String commentsDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_comments']";
	private String descriptionDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_description']";
	private String locationDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_location']";
	private String safetyNetworkDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_published']";
	private String orderDescriptionDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_order_description']";
	private String orderNumberDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_order_number']";
	private String purchaseOrderDisplayColumnCheckBoxLocator = "xpath=//INPUT[@id='chk_asset_search_purchaseorder']";
	private String assetAttributesDynamicColumnLocator = "xpath=//DIV[@id='asset_search_asset_info_options']";
	private String assetAttributesDynamicCheckBoxXpath = "//INPUT[@type='checkbox' and contains(@id,'chk_asset_search_infooption_')]";
	private String runButtonLocator = "xpath=//INPUT[@id='reportForm_label_Run']";
	private String assetSearchResultTable = "xpath=//TABLE[@id='resultsTable']";

	public AssetSearch(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
		search = misc.getSearch();
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header "Asset Search" on the page.
	 */
	public void assertAssetsPage() {
		misc.checkForErrorMessages("verifyAssetsPage");
		if(!selenium.isElementPresent(assetSearchPageHeaderLocator)) {
			fail("Could not find the header for 'Asset Search'.");
		}
	}
	
	public void verifySearchCriteriaForm() {
		assertTrue(selenium.isElementPresent(rfidNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(serialNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(orderNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(purchaseOrderTextFieldLocator));
		assertTrue(selenium.isElementPresent(referenceNumberTextFieldLocator));
		assertTrue(selenium.isElementPresent(assetStatusSelectListLocator));
		assertTrue(selenium.isElementPresent(assetTypeGroupSelectListLocator));
		assertTrue(selenium.isElementPresent(assetTypeSelectListLocator));
		assertTrue(selenium.isElementPresent(locationTextFieldLocator));
		assertTrue(selenium.isElementPresent(ownerTextFieldLocator));
		assertTrue(selenium.isElementPresent(chooseOwnerLinkLocator));
		assertTrue(selenium.isElementPresent(fromDateTextFieldLocator));
		assertTrue(selenium.isElementPresent(toDateTextFieldLocator));
	}
	
	public AssetSelectDisplayColumns getDisplayColumns() {
		AssetSelectDisplayColumns result = new AssetSelectDisplayColumns();

		result.setSerialNumber(misc.getCheckBoxValue(serialNumberDisplayColumnCheckBoxLocator));
		result.setrfidNumber(misc.getCheckBoxValue(rfidNumberDisplayColumnCheckBoxLocator));
		result.setCustomerName(misc.getCheckBoxValue(customerNameDisplayColumnCheckBoxLocator));
		result.setDivision(misc.getCheckBoxValue(divisionDisplayColumnCheckBoxLocator));
		result.setOrganization(misc.getCheckBoxValue(organizationDisplayColumnCheckBoxLocator));
		result.setReferenceNumber(misc.getCheckBoxValue(referenceNumberDisplayColumnCheckBoxLocator));
		result.setAssetTypeGroup(misc.getCheckBoxValue(assetTypeGroupDisplayColumnCheckBoxLocator));
		result.setAssetType(misc.getCheckBoxValue(assetTypeDisplayColumnCheckBoxLocator));
		result.setAssetStatus(misc.getCheckBoxValue(assetStatusDisplayColumnCheckBoxLocator));
		result.setLastEventDate(misc.getCheckBoxValue(lastEventDateDisplayColumnCheckBoxLocator));
		result.setNetworkLastEventDate(misc.getCheckBoxValue(networkLastEventDateDisplayColumnCheckBoxLocator));
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
	
	public void setDisplayColumns(AssetSelectDisplayColumns sdc) {
		misc.setCheckBoxValue(serialNumberDisplayColumnCheckBoxLocator, sdc.getSerialNumber());
		misc.setCheckBoxValue(rfidNumberDisplayColumnCheckBoxLocator, sdc.getrfidNumber());
		misc.setCheckBoxValue(customerNameDisplayColumnCheckBoxLocator, sdc.getCustomerName());
		misc.setCheckBoxValue(divisionDisplayColumnCheckBoxLocator, sdc.getDivision());
		misc.setCheckBoxValue(organizationDisplayColumnCheckBoxLocator, sdc.getOrganization());
		misc.setCheckBoxValue(referenceNumberDisplayColumnCheckBoxLocator, sdc.getReferenceNumber());
		misc.setCheckBoxValue(assetTypeGroupDisplayColumnCheckBoxLocator, sdc.getAssetTypeGroup());
		misc.setCheckBoxValue(assetTypeDisplayColumnCheckBoxLocator, sdc.getAssetType());
		misc.setCheckBoxValue(assetStatusDisplayColumnCheckBoxLocator, sdc.getAssetStatus());
		misc.setCheckBoxValue(lastEventDateDisplayColumnCheckBoxLocator, sdc.getLastEventDate());
		misc.setCheckBoxValue(networkLastEventDateDisplayColumnCheckBoxLocator, sdc.getNetworkLastEventDate());
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
		
		if(asc.getAssetStatus() != null) {
			if(misc.isOptionPresent(assetStatusSelectListLocator, asc.getAssetStatus())) {
				selenium.select(assetStatusSelectListLocator, asc.getAssetStatus());
			} else {
				fail("Could not find the asset status '" + asc.getAssetStatus() + "'");
			}
		}
		
		if(asc.getAssetTypeGroup() != null) {
			if(misc.isOptionPresent(assetTypeGroupSelectListLocator, asc.getAssetTypeGroup())) {
				selenium.select(assetTypeGroupSelectListLocator, asc.getAssetTypeGroup());
				search.waitForDisplayColumnsToUpdate(MiscDriver.DEFAULT_TIMEOUT);
			} else {
				fail("Could not find the asset type group '" + asc.getAssetTypeGroup() + "'");
			}
		}
		
		if(asc.getAssetType() != null) {
			if(misc.isOptionPresent(assetTypeSelectListLocator, asc.getAssetType())) {
				selenium.select(assetTypeSelectListLocator, asc.getAssetType());
				search.waitForDisplayColumnsToUpdate(MiscDriver.DEFAULT_TIMEOUT);
			} else {
				fail("Could not find the asset type  '" + asc.getAssetType() + "'");
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
	
	public List<String> getAssetStatuses() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(assetStatusSelectListLocator)) {
			String tmp[] = selenium.getSelectOptions(assetStatusSelectListLocator);
			for(int i = 0; i < tmp.length; i++) {
				result.add(tmp[i]);
			}
		}
		return result;
	}
	
	public List<String> getAssetTypeGroups() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(assetTypeGroupSelectListLocator)) {
			String tmp[] = selenium.getSelectOptions(assetTypeGroupSelectListLocator);
			for(int i = 0; i < tmp.length; i++) {
				result.add(tmp[i]);
			}
		}
		return result;
	}
	
	public List<String> getAssetTypes() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(assetTypeSelectListLocator)) {
			String tmp[] = selenium.getSelectOptions(assetTypeSelectListLocator);
			for(int i = 0; i < tmp.length; i++) {
				result.add(tmp[i]);
			}
		}
		return result;
	}

	public List<String> getDynamicAssetAttributeDisplayColumns() {
		List<String> result = search.getAttributesDisplayColumns(assetAttributesDynamicColumnLocator, assetAttributesDynamicCheckBoxXpath);
		return result;
	}

	public void runSearch() {
		if(selenium.isElementPresent(runButtonLocator)) {
			selenium.click(runButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Run button for Asset Search");
		}
	}

	/**
	 * Goes to view the asset via the Info link. The index is the row
	 * the asset is on in the search result table. First asset is zero.
	 * If you want an asset from page 2 or higher, go to the next page
	 * then call this. It is based on the current page only. If you
	 * specify asset 400, it will not go ahead pages to find it. It 
	 * will check the current page and fail to find it.
	 * 
	 * @param index
	 */
	public void gotoAsset(int index) {
		String locator = assetSearchResultTable + "/tbody/tr[" + ((index+1) * 2) + "]/td/a[contains(text(),'Info')]";
		selenium.click(locator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public String getSerialNumberForAsset(int row) {
		String serialNumber = null;
		String serialNumberText = "Serial Number";
		assert(row > -1);
		String locator = assetSearchResultTable + "/tbody/tr[1]/th/A[contains(text(),'" + serialNumberText + "')]";
		if(selenium.isElementPresent(locator)) {
			Number n = selenium.getElementIndex(locator);
			int col = n.intValue();
			String serialNumberLocator = assetSearchResultTable + "." + ((row * 2) + 1) + "." + col;
			serialNumber = selenium.getTable(serialNumberLocator);
		} else {
			fail("Could not locate a column with the header '" + serialNumberText + "'");
		}
		return serialNumber;
	}
}

