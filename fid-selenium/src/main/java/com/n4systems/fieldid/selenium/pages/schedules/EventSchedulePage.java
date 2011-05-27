package com.n4systems.fieldid.selenium.pages.schedules;

import static org.junit.Assert.fail;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventSchedulePage extends FieldIDPage{


	public EventSchedulePage(Selenium selenium) {
		super(selenium);
		if (!checkOnEventSchedulePage()) {
			fail("Expected to be on event schedule page!");
		}
	}

	private boolean checkOnEventSchedulePage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//a[@id='newScheduleButton']");
	}
	
	public void clickAddSchedule(){
		selenium.click("//div/a[@id='newScheduleButton']");
	}
	
	public void clickSave(){
		selenium.click("//div[@class='formAction']/input[@type='submit']");
		waitForAjax();
	}
	
	public void selectEventType(String eventType){
		selenium.select("//select[@name='type']","label="+eventType );
	}
	
	public void enterScheduleDate(String date){
		selenium.type("//input[@name='nextDate']", date);
	}

	public void selectJob(String jobName) {
		selenium.select("//select[@name='project']","label="+jobName );
	}
	
	public List<String> getScheduleDates() {
		return collectTableValuesUnderCellForCurrentPage(2, 1, "/div[@class='scheduleDate']");
	}
	
	public void removeSchedule(String scheduleDate) {
		selenium.click("//div[.='"+ scheduleDate + "']/../..//a[.='Remove']");
	}
	
	public String getScheduleJob(String scheduleDate) {
		return selenium.getText("//div[.='"+ scheduleDate + "']/../..//td[contains(@id, 'jobName')]");
	}
	
	public boolean hasSchedules() {
		return selenium.isElementPresent("table[@class='list']");
	}
}
