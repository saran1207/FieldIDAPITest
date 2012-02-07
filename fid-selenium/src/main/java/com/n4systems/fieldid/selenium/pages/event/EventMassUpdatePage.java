package com.n4systems.fieldid.selenium.pages.event;

import com.n4systems.fieldid.selenium.pages.MassUpdatePage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.thoughtworks.selenium.Selenium;

public class EventMassUpdatePage extends MassUpdatePage<ReportingPage> {

	public EventMassUpdatePage(Selenium selenium) {
        super(selenium, ReportingPage.class);
	}
	
    public void checkMassDelete(){
        selenium.check("//input[@id='check_delete']");
        selenium.fireEvent("//input[@id='check_delete']", "change");
        waitForAjax();
    }
	  
}
