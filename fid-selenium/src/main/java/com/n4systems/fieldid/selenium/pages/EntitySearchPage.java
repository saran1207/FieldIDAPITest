package com.n4systems.fieldid.selenium.pages;

import java.util.List;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.thoughtworks.selenium.Selenium;

public abstract class EntitySearchPage<T extends WebPage> extends FieldIDPage {

    private Class<T> clazz;

    public EntitySearchPage(Selenium selenium, Class<T> clazz) {
        super(selenium);
        this.clazz = clazz;
    }

    public abstract void setDisplayColumns(SearchDisplayColumns displayColumns);
    
    public void enterSerialNumber(String serialNumber) {
        selenium.type("//input[@id='reportForm_criteria_serialNumber']", serialNumber);
    }

    public T clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return createResultsPage();
    }

    private T createResultsPage() {
        return PageFactory.createPage(clazz, selenium);
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
		if (criteria.getAssetStatus() != null) {
			selenium.select("//select[@id='reportForm_criteria_assetStatus']", criteria.getAssetStatus());
		}
		if (criteria.getAssetTypeGroup() != null) {
			selenium.select("//select[@id='assetTypeGroup']", criteria.getAssetTypeGroup());
		}
		if (criteria.getAssetType() != null) {
			selenium.select("//select[@id='assettType']", criteria.getAssetType());
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
	
	public abstract List<String> getResultSerialNumbers();

	public AssetPage clickResultSerialNumber(String serialNumber) {
		selenium.click("//table[@class='list']//td[4]//a[text()='" +serialNumber+"']");
		return new AssetPage(selenium);
	}

}
