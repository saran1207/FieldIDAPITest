package com.n4systems.fieldid.selenium.pages;

import java.util.List;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.ReportSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.reporting.SaveReportPage;
import com.thoughtworks.selenium.Selenium;

public class ReportingPage extends WicketEntitySearchPage {

	public ReportingPage(Selenium selenium) {
		super(selenium);
	}
	
	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
		setCheckBoxValue("//input[@id='chk_event_search_date_performed']", displayColumns.isDatePreformed());
		setCheckBoxValue("//input[@id='chk_event_search_eventtype']", displayColumns.isEventType());
		setCheckBoxValue("//input[@id='chk_event_search_customer']", displayColumns.isJobSiteName());
		setCheckBoxValue("//input[@id='chk_event_search_eventresult']", displayColumns.isResult());
		setCheckBoxValue("//input[@id='chk_event_search_eventbook']", displayColumns.isEventBook());
		setCheckBoxValue("//input[@id='chk_event_search_performed_by']", displayColumns.isPerformedBy());
		setCheckBoxValue("//input[@id='chk_event_search_division']", displayColumns.isDivision());
		setCheckBoxValue("//input[@id='chk_event_search_organization']", displayColumns.isOrganization());
		setCheckBoxValue("//input[@id='chk_event_search_comments']", displayColumns.isComments());
		setCheckBoxValue("//input[@id='chk_event_search_assignedto']", displayColumns.isAssignedTo());
		setCheckBoxValue("//input[@id='chk_event_search_location']", displayColumns.isLocation());
		
		setCheckBoxValue("//input[@id='chk_event_search_peakload']", displayColumns.isPeakLoad());
		setCheckBoxValue("//input[@id='chk_event_search_testduration']", displayColumns.isTestDuration());
		setCheckBoxValue("//input[@id='chk_event_search_peakloadduration']", displayColumns.isPeakLoadDuration());
		
		setCheckBoxValue("//input[@id='chk_event_search_identifier']", displayColumns.isIdentifier());
		setCheckBoxValue("//input[@id='chk_event_search_rfidnumber']", displayColumns.isRfidNumber());
		setCheckBoxValue("//input[@id='chk_event_search_referencenumber']", displayColumns.isReferenceNumber());
		setCheckBoxValue("//input[@id='chk_event_search_assettypegroup']", displayColumns.isAssetTypeGroup());
		setCheckBoxValue("//input[@id='chk_event_search_assettype']", displayColumns.isAssetType());
		setCheckBoxValue("//input[@id='chk_event_search_assetstatus']", displayColumns.isAssetStatus());
		setCheckBoxValue("//input[@id='chk_event_search_identified']", displayColumns.isDateIdentified());
		setCheckBoxValue("//input[@id='chk_event_search_identifiedby']", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//input[@id='chk_event_search_description']", displayColumns.isDescription());
		setCheckBoxValue("//input[@id='chk_event_search_partnumber']", displayColumns.isPartNumber());
		
		setCheckBoxValue("//input[@id='chk_event_search_order_description']", displayColumns.isOrderDescription());
		setCheckBoxValue("//input[@id='chk_event_search_order_number']", displayColumns.isOrderNumber());
		setCheckBoxValue("//input[@id='chk_event_search_purchaseorder']", displayColumns.isPurchaseOrder());
	}
	
	public void setSearchCriteria(ReportSearchCriteria criteria) {
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
			selenium.type("//input[@id='fromDate:dateField']", criteria.getFromDate());
		}
		if (criteria.getToDate() != null) {
			selenium.type("//input[@id='toDate:dateField']", criteria.getToDate());
		}
		if(criteria.getJob() != null) {
			selenium.select("//select[@name='jobContainer:job']", criteria.getJob());
		}
		if(criteria.getPerformedBy() != null) {
			selenium.select("//select[@name='eventDetailsCriteriaPanel:performedBy']", criteria.getPerformedBy());
		}
		if(criteria.getEventTypeGroup() != null) {
			selenium.select("//select[@name='eventDetailsCriteriaPanel:eventTypeGroup']", criteria.getEventTypeGroup());
		}
		if(criteria.getEventBook() != null) {
			selenium.select("//select[@name='eventDetailsCriteriaPanel:eventBook']", criteria.getEventBook());
		}
		if(criteria.getSafetyNetworkResults()) {
			setCheckBoxValue("//input[@name='includeNetworkResultsContainer:includeSafetyNetwork']", criteria.getSafetyNetworkResults());
		}
		if(criteria.getResult() != null) {
			selenium.select("//select[@name='eventDetailsCriteriaPanel:result']", criteria.getResult());
		}
	}
	
	public void clickViewEvent(String identifier){
		selenium.click("//table[@class='list']//td//a[.='" +identifier+"']/../../..//a[.='View']");
		waitForElementToBePresent("//iframe[@class='cboxIframe']");
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
    	selenium.select("//select[@name='filters:eventDetailsCriteriaPanel:containedPanel:result']", result);
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

}
