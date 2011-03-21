package com.n4systems.fieldid.selenium.pages.event;

import com.n4systems.fieldid.selenium.pages.MassUpdatePage;
import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class EventMassUpdatePage extends  MassUpdatePage<ReportingSearchResultsPage>{

	public EventMassUpdatePage(Selenium selenium) {
		 super(selenium, ReportingSearchResultsPage.class);
	}
	
	  public void checkMassDelete(){
	    	selenium.check("//input[@id='check_delete']");
	        selenium.fireEvent("//input[@id='check_delete']", "change");
	    	waitForAjax();
	    }
	  
}
