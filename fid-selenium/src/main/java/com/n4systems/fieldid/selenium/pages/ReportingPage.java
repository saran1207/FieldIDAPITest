package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.components.EventViewPage;
import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.ReportSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.reporting.SaveReportPage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class ReportingPage extends WicketEntitySearchPage {

	public ReportingPage(Selenium selenium) {
		super(selenium);
	}

    public void setDisplayColumns(SearchDisplayColumns searchDisplayColumns) {
        setDisplayColumns(searchDisplayColumns, false, false, false, false);
    }

	public void setDisplayColumns(SearchDisplayColumns displayColumns, boolean isJobsEnabled, boolean isAssignedToEnabled,
                                  boolean isIntegrationEnabled, boolean isProofTestIntegrationEnabled) {

        setCheckBoxValue("//label[.='Event State']/../input", displayColumns.isEventState());
        setCheckBoxValue("//label[.='Score']/../input", displayColumns.isScore());
        setCheckBoxValue("//label[.='Due Date']/../input", displayColumns.isDueDate());
        setCheckBoxValue("//label[.='Date Performed']/../input", displayColumns.isDatePreformed());
        setCheckBoxValue("//label[.='Days Past Due']/../input", displayColumns.isDaysPastDue());
        setCheckBoxValue("//label[.='Event Type Group']/../input", displayColumns.isEventTypeGroup());
        setCheckBoxValue("//label[.='Event Type']/../input", displayColumns.isEventType());
        setCheckBoxValue("//label[.='Priority']/../input", displayColumns.isPriority());
        setCheckBoxValue("//label[.='Event Status']/../input", displayColumns.isStatus());
        setCheckBoxValue("//label[.='Result']/../input", displayColumns.isResult());
        setCheckBoxValue("//label[.='Event Book']/../input", displayColumns.isEventBook());
        setCheckBoxValue("//label[.='Assignee']/../input", displayColumns.isAssignee());
        setCheckBoxValue("//label[.='Performed By']/../input", displayColumns.isPerformedBy());
        setCheckBoxValue("//label[.='Comments']/../input", displayColumns.isComments());
        setCheckBoxValue("//label[.='Action Notes']/../input", displayColumns.isActionNotes());
        if (isJobsEnabled) {
            setCheckBoxValue("//label[.='Job ID']/../input", displayColumns.isJobId());
            setCheckBoxValue("//label[.='Job Name']/../input", displayColumns.isJobName());
        }
        setCheckBoxValue("//label[.='ID Number']/../input", displayColumns.isIdentifier());
        setCheckBoxValue("//label[.='RFID Number']/../input", displayColumns.isRfidNumber());
        setCheckBoxValue("//label[.='Reference Number']/../input", displayColumns.isReferenceNumber());
        setCheckBoxValue("//label[.='Date Identified']/../input", displayColumns.isDateIdentified());

        if (isAssignedToEnabled)
            setCheckBoxValue("//label[.='Assigned To']/../input", displayColumns.isAssignedTo());
        setCheckBoxValue("//label[.='Customer Name']/../input", displayColumns.isCustomer());
        setCheckBoxValue("//label[.='Division']/../input", displayColumns.isDivision());
        setCheckBoxValue("//label[.='Location']/../input", displayColumns.isLocation());
        setCheckBoxValue("//label[.='Organization']/../input", displayColumns.isOrganization());

        if (isIntegrationEnabled) {
            setCheckBoxValue("//label[.='Order Number']/../input", displayColumns.isOrderNumber());
            setCheckBoxValue("//label[.='Purchase Order']/../input", displayColumns.isPurchaseOrder());
        }

        setCheckBoxValue("//label[.='Asset Type Group']/../input", displayColumns.isAssetTypeGroup());
        setCheckBoxValue("//label[.='Asset Type']/../input", displayColumns.isAssetType());
        setCheckBoxValue("//label[.='Asset Status']/../input", displayColumns.isAssetStatus());
        setCheckBoxValue("//label[.='Identified By']/../input", displayColumns.isIdentifiedBy());
        setCheckBoxValue("//label[.='Description']/../input", displayColumns.isDescription());

        if (isProofTestIntegrationEnabled) {
            setCheckBoxValue("//label[.='Peak Load']/../input", displayColumns.isPeakLoad());
            setCheckBoxValue("//label[.='Test Duration']/../input", displayColumns.isTestDuration());
            setCheckBoxValue("//label[.='Peak Load Duration']/../input", displayColumns.isPeakLoadDuration());
        }
	}
	
	public void setSearchCriteria(ReportSearchCriteria criteria) {
		if (criteria.getRFIDNumber() != null) {
			selenium.type("//input[@name='filters:identifiersCriteriaPanel:containedPanel:rfidNumber']", criteria.getRFIDNumber());
		}
		if (criteria.getIdentifier() != null) {
			selenium.type("//input[@name='filters:identifiersCriteriaPanel:containedPanel:identifier']", criteria.getIdentifier());
		}
		if (criteria.getOrderNumber() != null) {
			selenium.type("//input[@name='filters:orderDetailsCriteriaPanel:containedPanel:orderNumber']", criteria.getOrderNumber());
		}
		if (criteria.getPurchaseOrder() != null) {
			selenium.type("//input[@name='filters:orderDetailsCriteriaPanel:containedPanel:purchaseOrder']", criteria.getPurchaseOrder());
		}
		if (criteria.getAssignedTo() != null) {
			selenium.select("//select[@name='filters:ownershipCriteriaPanel:containedPanel:assignedToContainer:assignedTo']", criteria.getAssignedTo());
		}
		if (criteria.getReferenceNumber() != null) {
			selenium.type("//input[@name='filters:identifiersCriteriaPanel:containedPanel:referenceNumber']", criteria.getReferenceNumber());
		}
		if (criteria.getAssetStatus() != null) {
			selenium.select("//select[@name='filters:assetDetailsCriteriaPanel:containedPanel:assetStatus']", criteria.getAssetStatus());
		}
		if (criteria.getAssetTypeGroup() != null) {
			selenium.select("//select[@name='filters:assetDetailsCriteriaPanel:containedPanel:assetTypeGroup']", criteria.getAssetTypeGroup());
            waitForAjax();
		}
		if (criteria.getAssetType() != null) {
			selenium.select("//select[@name='filters:assetDetailsCriteriaPanel:containedPanel:assetType']", criteria.getAssetType());
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
			selenium.type("//input[@name='filters:eventStatusAndDateRangePanel:containedPanel:completeRangePicker:fromDate:dateField']", criteria.getFromDate());
		}
		if (criteria.getToDate() != null) {
			selenium.type("//input[@name='filters:eventStatusAndDateRangePanel:containedPanel:completeRangePicker:toDate:dateField']", criteria.getToDate());
		}
		if(criteria.getJob() != null) {
			selenium.select("//select[@name='filters:eventDetailsCriteriaPanel:containedPanel:jobContainer:job']", criteria.getJob());
		}
		if(criteria.getPerformedBy() != null) {
			selenium.select("//select[@name='filters:eventDetailsCriteriaPanel:containedPanel:performedBy']", criteria.getPerformedBy());
		}
		if(criteria.getEventTypeGroup() != null) {
			selenium.select("//select[@name='filters:eventDetailsCriteriaPanel:containedPanel:eventTypeGroup']", criteria.getEventTypeGroup());
		}
		if(criteria.getEventBook() != null) {
			selenium.select("//select[@name='filters:eventDetailsCriteriaPanel:containedPanel:eventBook']", criteria.getEventBook());
		}
		if(criteria.getSafetyNetworkResults()) {
			setCheckBoxValue("//input[@name='filters:eventDetailsCriteriaPanel:containedPanel:includeNetworkResultsContainer:includeSafetyNetwork']", criteria.getSafetyNetworkResults());
		}
		if(criteria.getResult() != null) {
			selenium.select("//select[@name='filters:resolutionDetailsCriteriaPanel:containedPanel:result']", criteria.getResult());
		}
	}
	
	public EventViewPage clickViewEvent(String identifier){
		selenium.click("//table[@class='list']//td//a[.='" + identifier + "']/../../..//a[.='View']");
        return new EventViewPage(selenium);
	}
	
	public EventPage clickEditEvent(String identifier) {
		selenium.click("//table[@class='list']//td//a[.='" +identifier+"']/../../..//a[.='Edit']");
		return new EventPage(selenium);
	}
	
	public SaveReportPage clickSaveReport() {
		selenium.click("//a[.='Save']");
		return new SaveReportPage(selenium);
	}
	
	public void clickStartNewReport() {
		selenium.click("//a[.='Start New Report']");
		waitForPageToLoad();
	}
	
	public List<String> getSaveReportList() {
		return collectTableValuesUnderCellForCurrentPage(1, 2, "");
	}

	public void clickRunSavedReport(String reportName) {
		int i = getSaveReportList().indexOf(reportName) + 1;
		selenium.click("//table[@id='savedReportList']/tbody/tr[" + i + "]/td[1]/a");
		waitForPageToLoad();
	}
	
	public EventPage clickStartEventLink(){
    	selenium.click("//a[@class='dropDownLink'][1]");
    	selenium.click("//table[@class='list']//li/a[contains(.,'Start Event')][1]");
    	return new EventPage(selenium);
	}

	public MyAccountPage clickSaveReportsMore() {
		selenium.click("//a[.='more']");
		return new MyAccountPage(selenium);
	}
	
	public void clickPrintReport() {
	   	selenium.click("//a[@id='moreActions'][1]");
    	selenium.click("//li/a[contains(.,'Print Report')][1]");
	}
    
    @Override
	public List<String> getResultIdentifiers() {
		return collectTableValuesUnderCellForCurrentPage(1, 3, "a");
	}
    
    public List<String> getEventResults() {
    	return collectTableValuesUnderCellForCurrentPage(1, 10, "span");
    }
    
    @Override
    public void enterIdentifier(String identifier) {
        selenium.type("//input[@name='filters:identifiersCriteriaPanel:containedPanel:identifier']", identifier);
    }
    
    public void selectResult(String result) {
    	selenium.select("//select[@name='filters:resolutionDetailsCriteriaPanel:containedPanel:eventResult']", result);
    }
    
    public boolean isBlankSlate() {
    	return selenium.isElementPresent("//div[@class='initialMessage']");
    }
    
    public EventPage clickPerformFirstEventPage() {
    	selenium.click("//input[contains(@value, 'Perform your first event now')]");
    	return new EventPage(selenium);
    }
    
    public EventPage clickPerformMultiEventPage() {
    	selenium.click("//a[contains(., 'Perform an event on up to 250 assets')]");
    	return new EventPage(selenium);
    }
    
    public ImportPage clickImportFromExcel() {
    	selenium.click("//a[contains(., 'Import from Excel')]");
    	return new ImportPage(selenium);
    }

    public void selectAllItemsOnPage() {
        checkAndFireClick("//table[@class='list']//tr[1]//th[1]//input");
        waitForWicketAjax();
    }

	public AssetPage clickReportLinkForResult(int resultNumber) {
		selenium.click("//table[@class='list']//tbody//tr[" + resultNumber + "]/td//a[@class='identifierLink']");
		return new AssetPage(selenium);
	}

	public EventMassUpdatePage clickEventMassUpdate() {
		selenium.click("//a[contains(.,'Mass Update')]");
		return new EventMassUpdatePage(selenium);
	}

    public void selectOpenEvents() {
        selenium.select("//select[@name='filters:workflowStateSelect']", "Open");
        waitForWicketAjax();
    }

    public String getScheduledDateForResult(int resultNumber) {
        return selenium.getText("//table[@id='resultsTable']/tbody/tr["+resultNumber+"]/td[contains(@id, 'nextdate')]");
    }


}
