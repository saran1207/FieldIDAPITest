package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.assets.AssetsMassUpdatePage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class AssetsSearchPage extends WicketEntitySearchPage {
	
	public AssetsSearchPage(Selenium selenium) {
		super(selenium);
	}

	@Override
	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
		setDisplayColumns(displayColumns, false, false);
	}

	public void setDisplayColumns(SearchDisplayColumns displayColumns, boolean isIntergationEnabled, boolean isJobSitesEnabled) {
		setCheckBoxValue("//div//label[.='ID Number']/../input", displayColumns.isIdentifier());
		setCheckBoxValue("//div//label[.='Reference Number']/../input", displayColumns.isReferenceNumber());
		setCheckBoxValue("//div//label[.='RFID Number']/../input", displayColumns.isRfidNumber());
		if (isJobSitesEnabled) {
			setCheckBoxValue("//div//label[.='Job Site Name']/../input", displayColumns.isJobSiteName());
		}
		setCheckBoxValue("//div//label[.='Customer Name']/../input", displayColumns.isCustomer());
		setCheckBoxValue("//div//label[.='Division']/../input", displayColumns.isDivision());
		setCheckBoxValue("//div//label[.='Location']/../input", displayColumns.isLocation());
		setCheckBoxValue("//div//label[.='Organization']/../input", displayColumns.isOrganization());
		setCheckBoxValue("//div//label[.='Asset Type Group']/../input", displayColumns.isAssetTypeGroup());
		setCheckBoxValue("//div//label[.='Asset Type']/../input", displayColumns.isAssetType());
		setCheckBoxValue("//div//label[.='Asset Status']/../input", displayColumns.isAssetStatus());
		setCheckBoxValue("//div//label[.='Date Identified']/../input", displayColumns.isDateIdentified());
		setCheckBoxValue("//div//label[.='Last Event Date']/../input", displayColumns.isLastEventDate());
		setCheckBoxValue("//div//label[.='Network Last Event Date']/../input", displayColumns.isNetworkLastEventDate());
		setCheckBoxValue("//div//label[.='Assigned To']/../input", displayColumns.isAssignedTo());
		setCheckBoxValue("//div//label[.='Identified By']/../input", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//div//label[.='Modified By']/../input", displayColumns.isModifiedBy());
		setCheckBoxValue("//div//label[.='Comments']/../input", displayColumns.isComments());
		setCheckBoxValue("//div//label[.='Description']/../input", displayColumns.isDescription());
		setCheckBoxValue("//div//label[.='Safety Network']/../input", displayColumns.isSafetyNetwork());
		setCheckBoxValue("//div//label[.='Next Scheduled Date']/../input", displayColumns.isScheduledDate());
		if (isIntergationEnabled) {
			setCheckBoxValue("//div//label[.='Organization']/../input", displayColumns.isOrderDescription());
			setCheckBoxValue("//div//label[.='Organization']/../input", displayColumns.isOrderNumber());
		} else {
			setCheckBoxValue("//div//label[.='Purchase Order']/../input", displayColumns.isPurchaseOrder());
			setCheckBoxValue("//div//label[.='Order Number']/../input", displayColumns.isOrderNumber());
		}
	}

	public AssetPage clickResultInfo(String identifier) {
		selenium.click("//table[@class='list']//a[text()='" +identifier+"']/../../..//li/a[contains(text(), 'View')]");
		return new AssetPage(selenium);
	}

	public QuickEventPage clickResultStartEvent(String identifier) {
		selenium.click("//table[@class='list']//a[text()='" +identifier+"']/../../..//li/a[contains(text(), 'Start Event')]");
		return new QuickEventPage(selenium);
	}

    @Override
	public List<String> getResultIdentifiers() {
		return collectTableValuesUnderCellForCurrentPage(1, 2, "a");
	}

    public boolean isBlankSlate() {
    	return selenium.isElementPresent("//div[@class='initialMessage']");
    }

    public IdentifyPage clickIdentifyFirstAsset() {
    	selenium.click("//input[contains(@value, 'Identify your first asset now')]");
    	return new IdentifyPage(selenium);
    }
    
    public IdentifyPage clickIdentifyUpTo250Assets() {
    	selenium.click("//a[contains(., 'Identify up to 250 at once')]");
    	return new IdentifyPage(selenium);
    }
    
    public ImportPage clickImportFromExcel() {
    	selenium.click("//a[contains(., 'Import from Excel')]");
    	return new ImportPage(selenium);
    }

    public AssetsMassUpdatePage clickMassUpdate() {
        selenium.click("//a[.='Mass Update']");
        return new AssetsMassUpdatePage(selenium);
    }

    public AssetPage clickAssetLinkForResult(int resultNumber) {
        selenium.click("//table[@class='list']//tbody//tr["+resultNumber+"]/td//a[@class='identifierLink']");
        return new AssetPage(selenium);
    }

}
