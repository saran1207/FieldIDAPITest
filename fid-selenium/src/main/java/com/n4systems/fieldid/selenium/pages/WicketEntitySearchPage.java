package com.n4systems.fieldid.selenium.pages;

import java.util.List;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.thoughtworks.selenium.Selenium;

public abstract class WicketEntitySearchPage<T extends WebPage> extends WicketFieldIDPage {

    private Class<T> clazz;

    public WicketEntitySearchPage(Selenium selenium, Class<T> clazz) {
        super(selenium);
        this.clazz = clazz;
    }

    public abstract void setDisplayColumns(SearchDisplayColumns displayColumns);
    
    public void enterIdentifier(String identifier) {
        selenium.type("//input[@name='identifiersCriteriaPanel:identifier']", identifier);
    }

    public T clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return createResultsPage();
    }

    private T createResultsPage() {
        return PageFactory.createPage(clazz, selenium);
    }
    
    public boolean hasSearchResults() {
    	return selenium.isElementPresent("//table[@class='list']/thead/tr");
    }
    
    public void expandSelectDisplayColumns() {
    	selenium.click("//div[@class='pageSection']//h2[contains(.,'Select Display Columns')]//a[1]");
    	//waitForAjax();
    }
	public void expandSearchCriteria() {
    	selenium.click("//div[@class='pageSection']//h2[contains(.,'Search Settings')]//a[1]");
    	//waitForAjax();
	}

	public void setSearchCriteria(AssetSearchCriteria criteria) {
		if (criteria.getRFIDNumber() != null) {
			selenium.type("//input[@name='identifiersCriteriaPanel:rfidNumber']", criteria.getRFIDNumber());
		}
		if (criteria.getIdentifier() != null) {
			selenium.type("//input[@name='identifiersCriteriaPanel:identifier']", criteria.getIdentifier());
		}
		if (criteria.getOrderNumber() != null) {
			selenium.type("//input[@name='orderDetailsCriteriaPanel:orderNumber']", criteria.getOrderNumber());
		}
		if (criteria.getPurchaseOrder() != null) {
			selenium.type("//input[@name='orderDetailsCriteriaPanel:purchaseOrder']", criteria.getPurchaseOrder());
		}
		if (criteria.getAssignedTo() != null) {
			selenium.select("//select[@name='ownershipCriteriaPanel:assignedUserContainer:assignedTo']", criteria.getAssignedTo());
		}
		if (criteria.getReferenceNumber() != null) {
			selenium.type("//input[@name='identifiersCriteriaPanel:referenceNumber']", criteria.getReferenceNumber());
		}
		if (criteria.getAssetStatus() != null) {
			selenium.select("//select[@name='assetDetailsCriteriaPanel:assetStatus']", criteria.getAssetStatus());
		}
		if (criteria.getAssetTypeGroup() != null) {
			selenium.select("//select[@name='assetDetailsCriteriaPanel:assetTypeGroup']", criteria.getAssetTypeGroup());
            waitForAjax();
		}
		if (criteria.getAssetType() != null) {
			selenium.select("//select[@name='assetDetailsCriteriaPanel:assetType']", criteria.getAssetType());
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
			selenium.type("//input[@name='fromDate:dateField']", criteria.getFromDate());
		}
		if (criteria.getToDate() != null) {
			selenium.type("//input[@name='toDate:dateField']", criteria.getToDate());
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
