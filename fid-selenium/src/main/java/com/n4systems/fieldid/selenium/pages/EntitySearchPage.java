package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public abstract class EntitySearchPage extends FieldIDPage {

    public EntitySearchPage(Selenium selenium) {
        super(selenium);
    }

    public abstract void setDisplayColumns(SearchDisplayColumns displayColumns);
    
    public void enterIdentifier(String identifier) {
        selenium.type("//input[@id='reportForm_criteria_identifier']", identifier);
    }

    public void clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        waitForPageToLoad();
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

    public void selectAllItemsOnPage() {
        checkAndFireClick("//table[@class='list']//tr[1]//th[1]//input");
        waitForAjax();
    }

    public void selectItemOnRow(int rowNumber) {
        checkAndFireClick("//table[@class='list']//tr[" + rowNumber + "]//td[1]//input");
        waitForAjax();
    }

	public void setSearchCriteria(AssetSearchCriteria criteria) {
		if (criteria.getRFIDNumber() != null) {
			selenium.type("//input[@id='reportForm_criteria_rfidNumber']", criteria.getRFIDNumber());
		}
		if (criteria.getIdentifier() != null) {
			selenium.type("//input[@id='reportForm_criteria_identifier']", criteria.getIdentifier());
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
		if (criteria.getAssetStatus() != null) {
			selenium.select("//select[@id='reportForm_criteria_assetStatus']", criteria.getAssetStatus());
		}
		if (criteria.getAssetTypeGroup() != null) {
			selenium.select("//select[@id='assetTypeGroup']", criteria.getAssetTypeGroup());
            waitForAjax();
		}
		if (criteria.getAssetType() != null) {
			selenium.select("//select[@id='assetType']", criteria.getAssetType());
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

	public List<String> getResultColumnHeaders() {
		return collectTableHeaders();
	}
	
	public abstract List<String> getResultIdentifiers();

	public AssetPage clickResultIdentifier(String identifier) {
		selenium.click("//a[text()='" +identifier+"']");
		return new AssetPage(selenium);
	}

}
