package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsSearchPage extends SearchPage {

    public AssetsSearchPage(Selenium selenium) {
        super(selenium);
    }

    public AssetsSearchResultsPage clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return new AssetsSearchResultsPage(selenium);
    }

	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
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

	public AssetPage clickResultInfo(String serialNumber) {
		selenium.click("//table[@class='list']//a[text()='" +serialNumber+"']/../..//a[contains(text(), 'Info')]");		
		return new AssetPage(selenium);
	}

	public InspectPage clickResultInspection(String serialNumber) {
		selenium.click("//table[@class='list']//a[text()='" +serialNumber+"']/../..//a[contains(text(), 'inspections')]");	
		return new InspectPage(selenium);
	}

}
