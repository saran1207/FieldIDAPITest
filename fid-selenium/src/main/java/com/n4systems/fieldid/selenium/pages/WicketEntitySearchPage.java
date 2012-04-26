package com.n4systems.fieldid.selenium.pages;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.search.SaveSearchPage;
import com.n4systems.util.persistence.search.SortDirection;
import com.thoughtworks.selenium.Selenium;

public abstract class WicketEntitySearchPage extends WicketFieldIDPage {

    public WicketEntitySearchPage(Selenium selenium) {
        super(selenium);
    }

    public abstract void setDisplayColumns(SearchDisplayColumns displayColumns);
    
    public void enterIdentifier(String identifier) {
        selenium.type("//input[@name='identifiersCriteriaPanel:identifier']", identifier);
    }

    public void clickRunSearchButton() {
        selenium.click("//form[contains(@class, 'search-config')]//span[contains(@class,'submit')]//a[.='Search']");
        waitForPageToLoad();
    }

    public boolean hasSearchResults() {
    	return selenium.isElementPresent("//table[@class='list']/thead/tr");
    }
    
    public void expandSelectDisplayColumns() {
    	selenium.click("//div[@class='pageSection']//h2[contains(.,'Select Display Columns')]//a[1]");
    }

	public void expandSearchCriteria() {
    	selenium.click("//div[@class='pageSection']//h2[contains(.,'Search Settings')]//a[1]");
	}

	public void setSearchCriteria(AssetSearchCriteria criteria) {

		if (criteria.getRFIDNumber() != null) {
			selenium.type(criteriaInputXpath("RFID Number", "input"), criteria.getRFIDNumber());
		}
		if (criteria.getIdentifier() != null) {
			selenium.type(criteriaInputXpath("Serial Number", "input"), criteria.getIdentifier());
		}
		if (criteria.getOrderNumber() != null) {
			selenium.type(criteriaInputXpath("Order Number", "input"), criteria.getOrderNumber());
		}
		if (criteria.getPurchaseOrder() != null) {
			selenium.type(criteriaInputXpath("Purchase Order", "input"), criteria.getPurchaseOrder());
		}
		if (criteria.getAssignedTo() != null) {
			selenium.select(criteriaInputXpath("Assigned TO", "select"), criteria.getAssignedTo());
		}
		if (criteria.getReferenceNumber() != null) {
			selenium.type(criteriaInputXpath("Reference Number", "input"), criteria.getReferenceNumber());
		}
		if (criteria.getAssetStatus() != null) {
			selenium.select(criteriaInputXpath("Asset Status", "select"), criteria.getAssetStatus());
		}
		if (criteria.getAssetTypeGroup() != null) {
			selenium.select(criteriaInputXpath("AssetTypeGroup", "select"), criteria.getAssetTypeGroup());
            waitForAjax();
		}
		if (criteria.getAssetType() != null) {
			selenium.select(criteriaInputXpath("Asset Type", "select"), criteria.getAssetType());
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
        String parentXpath = "//div[@class='filters']//div[@class='field']";
		if (criteria.getFromDate() != null) {
			selenium.type(parentXpath+"//input[contains(@name,'fromDate:dateField')]", criteria.getFromDate());
		}
		if (criteria.getToDate() != null) {
			selenium.type(parentXpath+"//input[contains(@name,'toDate:dateField')]", criteria.getToDate());
		}
	}
    
    private String criteriaInputXpath(String criteriaName, String inputType) {
        String parentXpath = "//div[@class='filters']//div[@class='field']";
        return parentXpath + "//label[.='"+criteriaName+"']/../" + inputType;
    }

	public List<String> getResultColumnHeaders() {
		return collectTableHeaders();
	}
	
	public abstract List<String> getResultIdentifiers();

	public AssetPage clickResultIdentifier(String identifier) {
		selenium.click("//a[text()='" +identifier+"']");
		return new AssetPage(selenium);
	}

    public void selectAllItemsOnPage() {
        checkAndFireClick("//table[@class='list']//tr[1]//th[1]//input");
        waitForWicketAjax();
    }

    public void selectItemOnRow(int rowNumber) {
        checkAndFireClick("//table[@class='list']//tr[" + rowNumber + "]//td[1]//input");
        waitForWicketAjax();
    }

    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<String>();
        int numColumns = selenium.getXpathCount("//table[@class='list']//th").intValue() - 2;
        for (int i = 2; i <= numColumns + 1; i++) {
            columnNames.add(selenium.getText("xpath=(//table[@class='list']//th)["+i+"]"));
        }
        return columnNames;
    }

    public String getSortColumn() {
        return selenium.getText("//table[@class='list']//th[contains(@class, 'wicket_order') and not(contains(@class, 'wicket_orderNone'))]");
    }

    public SortDirection getSortDirection() {
        String sortedClass = selenium.getAttribute("//table[@class='list']//th[contains(@class, 'wicket_order') and not(contains(@class, 'wicket_orderNone'))]/@class");
        // Up arrow class means highest is at the top, so we're descending. down arrow means lowest at top, so ascending
        if (sortedClass.toLowerCase().contains("up")) {
            return SortDirection.ASC;
        } else if (sortedClass.toLowerCase().contains("down")) {
            return SortDirection.DESC;
        } else {
            throw new RuntimeException("Could not determine sort direction from class: " + sortedClass);
        }
    }

    public void clickSortColumn(String sortColumnName) {
        selenium.click("//table[@class='list']//th//a[.='"+sortColumnName+"']");
        waitForPageToLoad();
    }

    public String getValueInCell(int rowNumber, int colNumber) {
        return selenium.getText("//table[@class='list']//tbody//tr["+(rowNumber)+"]//td["+(colNumber+1)+"]");
    }

    public SaveSearchPage clickSaveSearch() {
        selenium.click("//a[.='Save Search']");
        return new SaveSearchPage(selenium);
    }

    public int getTotalResultsCount() {
        return selenium.getXpathCount("//table[@class='list']//tr").intValue() - 1;
    }

}
