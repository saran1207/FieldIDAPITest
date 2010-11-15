package com.n4systems.fieldid.selenium.pages.schedules;

import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.EventPage;
import com.n4systems.fieldid.selenium.pages.search.SearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class SchedulesSearchResultsPage extends SearchResultsPage {

	public SchedulesSearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    public SchedulesMassUpdatePage clickMassUpdate() {
        selenium.click("//a[.='Mass Update']");
        return new SchedulesMassUpdatePage(selenium);
    }

    public int getTotalResultsCount() {
        return selenium.getXpathCount("//table[@id='resultsTable']//tr").intValue() - 1;
    }

    public String getScheduledDateForResult(int resultNumber) {
        int rowNumber = resultNumber + 1;
        return selenium.getText("//table[@id='resultsTable']//tr["+rowNumber+"]/td[contains(@id, 'nextdate')]");
    }
    
    public EventPage clickStartEventLink(){
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'Start Event')][1]");
    	return new EventPage(selenium);
    }
    
    public AssetPage clickViewSchedulesLink(){
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'View Schedules')][1]");
    	return new AssetPage(selenium);
    }
    
    public AssetPage clickEditSchedulesLink(){
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'Edit Schedule')][1]");
    	return new AssetPage(selenium);
    }
    
    public AssetPage clickViewAssetLink(){
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'View Asset')][1]");
    	return new AssetPage(selenium);
    }

    public AssetPage clickEditAssetLink(){
    	selenium.click("//ul[starts-with(@id,'moreActions')]/li/a[contains(.,'Edit Asset')][1]");
    	return new AssetPage(selenium);
    }
}
