package com.n4systems.fieldid.selenium.pages.schedules;

import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.InspectPage;
import com.thoughtworks.selenium.Selenium;

public class SchedulesSearchResultsPage extends FieldIDPage {

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
    
    public InspectPage clickStartEventLink(){
    	selenium.click("//a[@id='moreActions'][1]");
    	selenium.click("//li/a[contains(.,'Start Event')][1]");
    	return new InspectPage(selenium);
    }
    
    public AssetPage clickViewSchedulesLink(){
     	selenium.click("//a[@id='moreActions'][1]");
    	selenium.click("//li/a[contains(.,'View Schedules')][1]");
    	return new AssetPage(selenium);
    }
    
    public AssetPage clickEditSchedulesLink(){
     	selenium.click("//a[@id='moreActions'][1]");
    	selenium.click("//li/a[contains(.,'Edit Schedule')][1]");
    	return new AssetPage(selenium);
    }
    
    public AssetPage clickViewAssetLink(){
     	selenium.click("//a[@id='moreActions'][1]");
    	selenium.click("//li/a[contains(.,'View Asset')][1]");
    	return new AssetPage(selenium);
    }

    public AssetPage clickEditAssetLink(){
     	selenium.click("//a[@id='moreActions'][1]");
    	selenium.click("//li/a[contains(.,'Edit Asset')][1]");
    	return new AssetPage(selenium);
    }
}
