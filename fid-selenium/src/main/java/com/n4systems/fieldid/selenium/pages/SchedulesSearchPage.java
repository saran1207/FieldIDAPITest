package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.datatypes.SchedulesSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class SchedulesSearchPage extends EntitySearchPage<SchedulesSearchResultsPage> {

    public SchedulesSearchPage(Selenium selenium) {
        super(selenium, SchedulesSearchResultsPage.class);
    }

	@Override
	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
		setCheckBoxValue("//input[@id='chk_inspection_schedule_customer']", displayColumns.isJobSiteName());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_division']", displayColumns.isDivision());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_location']", displayColumns.isLocation());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_nextdate']", displayColumns.isScheduledDate());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_organization']", displayColumns.isOrganization());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_status']", displayColumns.isStatus());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_dayspastdue']", displayColumns.isDaysPastDue());
		
		setCheckBoxValue("//input[@id='chk_inspection_schedule_inspectiontype']", displayColumns.isInpectionType());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_lastdate']", displayColumns.isLastInspectionDate());
		
		setCheckBoxValue("//input[@id='chk_inspection_schedule_serialnumber']", displayColumns.isSerialNumber());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_rfidnumber']", displayColumns.isRfidNumber());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_referencenumber']", displayColumns.isReferenceNumber());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_assettypegroup']", displayColumns.isAssetTypeGroup());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_assettype']", displayColumns.isAssetType());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_assetstatus']", displayColumns.isAssetStatus());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_assignedto']", displayColumns.isAssignedTo());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_identified']", displayColumns.isDateIdentified());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_identifiedby']", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_description']", displayColumns.isDescription());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_partnumber']", displayColumns.isPartNumber());
		
		setCheckBoxValue("//input[@id='chk_inspection_schedule_order_description']", displayColumns.isOrderDescription());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_order_number']", displayColumns.isOrderNumber());
		setCheckBoxValue("//input[@id='chk_inspection_schedule_purchaseorder']", displayColumns.isPurchaseOrder());
	}

	public void setSearchCriteria(SchedulesSearchCriteria criteria) {
		super.setSearchCriteria(criteria);
		if (criteria.getScheduleStatus() != null) {
			selenium.select("//input[@id='reportForm_criteria_status']", criteria.getScheduleStatus());
		}
		if (criteria.getEventTypeGroup() != null) {
			selenium.select("//input[@id='reportForm_criteria_inspectionType']", criteria.getEventTypeGroup());
		}
		if (criteria.getJob() != null) {
			selenium.select("//input[@id='reportForm_criteria_job']", criteria.getJob());
		}
	}

    @Override
	public List<String> getResultSerialNumbers() {
		return collectTableValuesUnderCellForCurrentPage(2, 1, "a");
	}

}
