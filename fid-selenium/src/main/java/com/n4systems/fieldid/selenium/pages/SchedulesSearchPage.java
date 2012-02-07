package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.datatypes.SchedulesSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesMassUpdatePage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class SchedulesSearchPage extends EntitySearchPage {

    public SchedulesSearchPage(Selenium selenium) {
        super(selenium);
    }

	@Override
	public void setDisplayColumns(SearchDisplayColumns displayColumns) {
		setCheckBoxValue("//input[@id='chk_event_schedule_customer']", displayColumns.isJobSiteName());
		setCheckBoxValue("//input[@id='chk_event_schedule_division']", displayColumns.isDivision());
		setCheckBoxValue("//input[@id='chk_event_schedule_location']", displayColumns.isLocation());
		setCheckBoxValue("//input[@id='chk_event_schedule_nextdate']", displayColumns.isScheduledDate());
		setCheckBoxValue("//input[@id='chk_event_schedule_organization']", displayColumns.isOrganization());
		setCheckBoxValue("//input[@id='chk_event_schedule_status']", displayColumns.isStatus());
		setCheckBoxValue("//input[@id='chk_event_schedule_dayspastdue']", displayColumns.isDaysPastDue());
		
		setCheckBoxValue("//input[@id='chk_event_schedule_eventtype']", displayColumns.isEventType());
		setCheckBoxValue("//input[@id='chk_event_schedule_lastdate']", displayColumns.isLastEventDate());
		
		setCheckBoxValue("//input[@id='chk_event_schedule_identifier']", displayColumns.isIdentifier());
		setCheckBoxValue("//input[@id='chk_event_schedule_rfidnumber']", displayColumns.isRfidNumber());
		setCheckBoxValue("//input[@id='chk_event_schedule_referencenumber']", displayColumns.isReferenceNumber());
		setCheckBoxValue("//input[@id='chk_event_schedule_assettypegroup']", displayColumns.isAssetTypeGroup());
		setCheckBoxValue("//input[@id='chk_event_schedule_assettype']", displayColumns.isAssetType());
		setCheckBoxValue("//input[@id='chk_event_schedule_assetstatus']", displayColumns.isAssetStatus());
		setCheckBoxValue("//input[@id='chk_event_schedule_assignedto']", displayColumns.isAssignedTo());
		setCheckBoxValue("//input[@id='chk_event_schedule_identified']", displayColumns.isDateIdentified());
		setCheckBoxValue("//input[@id='chk_event_schedule_identifiedby']", displayColumns.isIdentifiedBy());
		setCheckBoxValue("//input[@id='chk_event_schedule_description']", displayColumns.isDescription());
		setCheckBoxValue("//input[@id='chk_event_schedule_partnumber']", displayColumns.isPartNumber());
		
		setCheckBoxValue("//input[@id='chk_event_schedule_order_description']", displayColumns.isOrderDescription());
		setCheckBoxValue("//input[@id='chk_event_schedule_order_number']", displayColumns.isOrderNumber());
		setCheckBoxValue("//input[@id='chk_event_schedule_purchaseorder']", displayColumns.isPurchaseOrder());
	}

	public void setSearchCriteria(SchedulesSearchCriteria criteria) {
		super.setSearchCriteria(criteria);
		if (criteria.getScheduleStatus() != null) {
			selenium.select("//input[@id='reportForm_criteria_status']", criteria.getScheduleStatus());
		}
		if (criteria.getEventTypeGroup() != null) {
			selenium.select("//input[@id='reportForm_criteria_eventType']", criteria.getEventTypeGroup());
		}
		if (criteria.getJob() != null) {
			selenium.select("//input[@id='reportForm_criteria_job']", criteria.getJob());
		}
	}

    @Override
	public List<String> getResultIdentifiers() {
		return collectTableValuesUnderCellForCurrentPage(2, 1, "a");
	}
    
    public boolean isBlankSlate() {
    	return selenium.isElementPresent("//div[@class='initialMessage']");
    }
    
    public IdentifyPage clickIdentifyAnAssetNow() {
    	selenium.click("//input[contains(@value, 'Identify an asset now')]");
    	return new IdentifyPage(selenium);
    }

    public int getTotalResultsCount() {
        return selenium.getXpathCount("//table[@id='resultsTable']//tbody//tr").intValue();
    }

    public String getScheduledDateForResult(int resultNumber) {
        return selenium.getText("//table[@id='resultsTable']/tbody/tr["+resultNumber+"]/td[contains(@id, 'nextdate')]");
    }

    public EventPage clickStartEventLink() {
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'Start Event')][1]");
    	return new EventPage(selenium);
    }

    public AssetPage clickViewSchedulesLink() {
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'View Schedules')][1]");
    	return new AssetPage(selenium);
    }

    public AssetPage clickEditSchedulesLink() {
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'Edit Schedule')][1]");
    	return new AssetPage(selenium);
    }

    public AssetPage clickViewAssetLink() {
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'View Asset')][1]");
    	return new AssetPage(selenium);
    }

    public AssetPage clickEditAssetLink() {
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'Edit Asset')][1]");
    	return new AssetPage(selenium);
    }

    public SchedulesMassUpdatePage clickMassUpdate() {
        selenium.click("//a[.='Mass Update']");
        return new SchedulesMassUpdatePage(selenium);
    }

}
