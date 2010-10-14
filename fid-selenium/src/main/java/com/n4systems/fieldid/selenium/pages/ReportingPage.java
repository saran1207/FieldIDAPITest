package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.datatypes.ReportSearchCriteria;
import com.n4systems.fieldid.selenium.reporting.page.ReportingSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class ReportingPage extends SearchPage {

	public ReportingPage(Selenium selenium) {
		super(selenium);
	}
	
    public ReportingSearchResultsPage clickRunSearchButton() {
        selenium.click("//input[@type='submit' and @value='Run']");
        return new ReportingSearchResultsPage(selenium);
    }

	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
		setCheckBoxValue("//input[@id='chk_inspection_search_date_performed']", displayColumns.isDatePreformed());
		setCheckBoxValue("//input[@id='chk_inspection_search_inspectiontype']", displayColumns.isInpectionType());
		setCheckBoxValue("//input[@id='chk_inspection_search_customer']", displayColumns.isJobSiteName());
		setCheckBoxValue("//input[@id='chk_inspection_search_inspectionresult']", displayColumns.isResult());
		setCheckBoxValue("//input[@id='chk_inspection_search_inspectionbook']", displayColumns.isInspectionBook());
		setCheckBoxValue("//input[@id='chk_inspection_search_performed_by']", displayColumns.isPerformedBy());
		setCheckBoxValue("//input[@id='chk_inspection_search_division']", displayColumns.isDivision());
		setCheckBoxValue("//input[@id='chk_inspection_search_organization']", displayColumns.isOrganization());
		setCheckBoxValue("//input[@id='chk_inspection_search_comments']", displayColumns.isComments());
		setCheckBoxValue("//input[@id='chk_inspection_search_assignedto']", displayColumns.isAssignedTo());
		setCheckBoxValue("//input[@id='chk_inspection_search_location']", displayColumns.isLocation());
		
		setCheckBoxValue("//input[@id='chk_inspection_search_peakload']", displayColumns.isPeakLoad());
		setCheckBoxValue("//input[@id='chk_inspection_search_testduration']", displayColumns.isTestDuration());
		setCheckBoxValue("//input[@id='chk_inspection_search_peakloadduration']", displayColumns.isPeakLoadDuration());
		
		setCheckBoxValue("//input[@id='chk_inspection_search_serialnumber']", displayColumns.isSerialNumber());
		setCheckBoxValue("//input[@id='chk_inspection_search_rfidnumber']", displayColumns.isRfidNumber());
		setCheckBoxValue("//input[@id='chk_inspection_search_referencenumber']", displayColumns.isReferenceNumber());
		setCheckBoxValue("//input[@id='chk_inspection_search_producttypegroup']", displayColumns.isProductTypeGroup());
		setCheckBoxValue("//input[@id='chk_inspection_search_producttype']", displayColumns.isProductType());
		setCheckBoxValue("//input[@id='chk_inspection_search_productstatus']", displayColumns.isProductStatus());
		setCheckBoxValue("//input[@id='chk_inspection_search_identified']", displayColumns.isDateIdentified());
		setCheckBoxValue("//input[@id='chk_inspection_search_identifiedby']", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//input[@id='chk_inspection_search_description']", displayColumns.isDescription());
		setCheckBoxValue("//input[@id='chk_inspection_search_partnumber']", displayColumns.isPartNumber());
		
		setCheckBoxValue("//input[@id='chk_inspection_search_order_description']", displayColumns.isOrderDescription());
		setCheckBoxValue("//input[@id='chk_inspection_search_order_number']", displayColumns.isOrderNumber());
		setCheckBoxValue("//input[@id='chk_inspection_search_purchaseorder']", displayColumns.isPurchaseOrder());
	}

	public void setSearchCriteria(ReportSearchCriteria criteria) {
		super.setSearchCriteria(criteria);
		if(criteria.getJob() != null) {
			selenium.select("//select[@id='reportForm_criteria_job']", criteria.getJob());
		}
		if(criteria.getPerformedBy() != null) {
			selenium.select("//select[@id='reportForm_criteria_performedBy']", criteria.getPerformedBy());
		}
		if(criteria.getEventTypeGroup() != null) {
			selenium.select("//select[@id='reportForm_criteria_inspectionTypeGroup']", criteria.getEventTypeGroup());
		}
		if(criteria.getInspectionBook() != null) {
			selenium.select("//select[@id='reportForm_criteria_inspectionBook']", criteria.getInspectionBook());
		}
		if(criteria.getSafetyNetworkResults()) {
			setCheckBoxValue("//input[@id='reportForm_criteria_includeNetworkResults']", criteria.getSafetyNetworkResults());
		}
		if(criteria.getResult() != null) {
			selenium.select("//select[@id='reportForm_criteria_status']", criteria.getResult());
		}
	}
	
	public void clickViewInspection(String serialNumber){
		selenium.click("//table[@class='list']//a[text()='" +serialNumber+"']/../..//a[contains(text(), 'View')]/..//a");	
		waitForElementToBePresent("//iframe[@id='lightviewContent']");
	}
}
