package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class AssetsSearchPage extends EntitySearchPage<AssetsSearchResultsPage> {
	
	public AssetsSearchPage(Selenium selenium) {
		super(selenium, AssetsSearchResultsPage.class);
	}

	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
		setCheckBoxValue("//input[@id='chk_asset_search_serialnumber']", displayColumns.isSerialNumber());
		setCheckBoxValue("//input[@id='chk_asset_search_referencenumber']", displayColumns.isReferenceNumber());
		setCheckBoxValue("//input[@id='chk_asset_search_rfidnumber']", displayColumns.isRfidNumber());
		setCheckBoxValue("//input[@id='chk_asset_search_customer']", displayColumns.isJobSiteName());
		setCheckBoxValue("//input[@id='chk_asset_search_division']", displayColumns.isDivision());
		setCheckBoxValue("//input[@id='chk_asset_search_location']", displayColumns.isLocation());
		setCheckBoxValue("//input[@id='chk_asset_search_organization']", displayColumns.isOrganization());
		setCheckBoxValue("//input[@id='chk_asset_search_assettypegroup']", displayColumns.isAssetTypeGroup());
		setCheckBoxValue("//input[@id='chk_asset_search_assettype']", displayColumns.isAssetType());
		setCheckBoxValue("//input[@id='chk_asset_search_assetstatus']", displayColumns.isAssetStatus());
		setCheckBoxValue("//input[@id='chk_asset_search_identified']", displayColumns.isDateIdentified());
		setCheckBoxValue("//input[@id='chk_asset_search_lasteventdate']", displayColumns.isLastEventDate());
		setCheckBoxValue("//input[@id='chk_asset_search_network_lasteventdate']", displayColumns.isNetworkLastEventDate());
		setCheckBoxValue("//input[@id='chk_asset_search_assignedto']", displayColumns.isAssignedTo());
		setCheckBoxValue("//input[@id='chk_asset_search_identifiedby']", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//input[@id='chk_asset_search_modifiedby']", displayColumns.isModifiedBy());
		setCheckBoxValue("//input[@id='chk_asset_search_comments']", displayColumns.isComments());
		setCheckBoxValue("//input[@id='chk_asset_search_description']", displayColumns.isDescription());
		setCheckBoxValue("//input[@id='chk_asset_search_published']", displayColumns.isSafetyNetwork());
		
		setCheckBoxValue("//input[@id='chk_asset_search_order_description']", displayColumns.isOrderDescription());
		setCheckBoxValue("//input[@id='chk_asset_search_order_number']", displayColumns.isOrderNumber());
		setCheckBoxValue("//input[@id='chk_asset_search_purchaseorder']", displayColumns.isPurchaseOrder());
	}

	public AssetPage clickResultInfo(String serialNumber) {
		selenium.click("//table[@class='list']//a[text()='" +serialNumber+"']/../..//a[contains(text(), 'View Asset')]");		
		return new AssetPage(selenium);
	}

	public QuickEventPage clickResultStartEvent(String serialNumber) {
		selenium.click("//table[@class='list']//a[text()='" +serialNumber+"']/../..//a[contains(text(), 'Start Event')]");
		return new QuickEventPage(selenium);
	}

    @Override
	public List<String> getResultSerialNumbers() {
		return collectTableValuesUnderCellForCurrentPage(2, 2, "a");
	}

}
