package com.n4systems.fieldid.selenium.pages;

import java.util.List;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsSearchPage extends FieldIDPage {

    public AssetsSearchPage(Selenium selenium) {
        super(selenium);
    }

    public void enterSerialNumber(String serialNumber) {
        selenium.type("//input[@id='reportForm_criteria_serialNumber']", serialNumber);
    }

    public AssetsSearchResultsPage clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return new AssetsSearchResultsPage(selenium);
    }

    public boolean hasSearchResults() {
    	return selenium.isElementPresent("//table[@class='list']");
    }
    
    public void expandSelectDisplayColumns() {
    	selenium.click("//a[@id='open_selectColumnForm']");
    	waitForAjax();
    }
	public void expandSearchCriteria() {
    	selenium.click("//a[@id='expandSection_reportForm']");
    	waitForAjax();
	}

	
	public void setSearchCriteria(AssetSearchCriteria criteria) {
		if (criteria.getRFIDNumber() != null) {
			selenium.type("//input[@id='reportForm_criteria_rfidNumber']", criteria.getRFIDNumber());
		}
		if (criteria.getSerialNumber() != null) {
			selenium.type("//input[@id='reportForm_criteria_serialNumber']", criteria.getSerialNumber());
		}
		if (criteria.getOrderNumber() != null) {
			selenium.type("//input[@id='reportForm_criteria_orderNumber']", criteria.getOrderNumber());
		}
		if (criteria.getPurchaseOrder() != null) {
			selenium.type("//input[@id='reportForm_criteria_purchaseOrder']", criteria.getPurchaseOrder());
		}
		if (criteria.getAssignedTo() != null) {
			selenium.select("//select[@id='reportForm_criteria_assignedUser']", criteria.getAssignedTo());
		}
		if (criteria.getReferenceNumber() != null) {
			selenium.type("//input[@id='reportForm_criteria_referenceNumber']", criteria.getReferenceNumber());
		}
		if (criteria.getProductStatus() != null) {
			selenium.select("//select[@id='reportForm_criteria_productStatus']", criteria.getProductStatus());
		}
		if (criteria.getProductTypeGroup() != null) {
			selenium.select("//select[@id='productTypeGroup']", criteria.getProductTypeGroup());
		}
		if (criteria.getProductType() != null) {
			selenium.select("//select[@id='productType']", criteria.getProductType());
		}
		if (criteria.getLocation() != null) {
			LocationPicker locPicker = getLocationPicker();
			locPicker.clickChooseLocation();
			locPicker.setFreeFormLocation(criteria.getLocation());
			locPicker.clickSetLocation();			
		}
		if (criteria.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(criteria.getOwner());
			orgPicker.clickSelectOwner();
		}
		if (criteria.getFromDate() != null) {
			selenium.type("//input[@id='reportForm_fromDate']", criteria.getFromDate());
		}
		if (criteria.getToDate() != null) {
			selenium.type("//input[@id='reportForm_toDate']", criteria.getToDate());
		}
	}

	public void setDisplayColumns(AssetSearchDisplayColumns displayColumns) {
		setCheckBoxValue("//input[@id='chk_product_search_serialnumber']", displayColumns.isSerialNumber());
		setCheckBoxValue("//input[@id='chk_product_search_referencenumber']", displayColumns.isReferenceNumber());
		setCheckBoxValue("//input[@id='chk_product_search_rfidnumber']", displayColumns.isRfidNumber());
		setCheckBoxValue("//input[@id='chk_product_search_customer']", displayColumns.isJobSiteName());
		setCheckBoxValue("//input[@id='chk_product_search_division']", displayColumns.isDivision());
		setCheckBoxValue("//input[@id='chk_product_search_location']", displayColumns.isLocation());
		setCheckBoxValue("//input[@id='chk_product_search_organization']", displayColumns.isOrganization());
		setCheckBoxValue("//input[@id='chk_product_search_producttypegroup']", displayColumns.isProductTypeGroup());
		setCheckBoxValue("//input[@id='chk_product_search_producttype']", displayColumns.isProductType());
		setCheckBoxValue("//input[@id='chk_product_search_productstatus']", displayColumns.isProductStatus());
		setCheckBoxValue("//input[@id='chk_product_search_identified']", displayColumns.isDateIdentified());
		setCheckBoxValue("//input[@id='chk_product_search_lastinspdate']", displayColumns.isLastInspectionDate());
		setCheckBoxValue("//input[@id='chk_product_search_network_lastinspdate']", displayColumns.isNetworkLastInspectionDate());
		setCheckBoxValue("//input[@id='chk_product_search_assignedto']", displayColumns.isAssignedTo());
		setCheckBoxValue("//input[@id='chk_product_search_identifiedby']", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//input[@id='chk_product_search_modifiedby']", displayColumns.isModifiedBy());
		setCheckBoxValue("//input[@id='chk_product_search_comments']", displayColumns.isComments());
		setCheckBoxValue("//input[@id='chk_product_search_description']", displayColumns.isDescription());
		setCheckBoxValue("//input[@id='chk_product_search_published']", displayColumns.isSafetyNetwork());
		setCheckBoxValue("//input[@id='chk_product_search_order_description']", displayColumns.isOrderDescription());
		setCheckBoxValue("//input[@id='chk_product_search_order_number']", displayColumns.isOrderNumber());
		setCheckBoxValue("//input[@id='chk_product_search_purchaseorder']", displayColumns.isPurchaseOrder());
	}

	public List<String> getResultColumnHeaders() {
		return collectTableHeaders();
	}
}
