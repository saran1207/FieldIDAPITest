package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class EmailNotificationPage extends FieldIDPage {

	public EmailNotificationPage(Selenium selenium) {
		super(selenium);
		if (!checkOnEmailNotificationsPage()) {
			fail("Expected to be on email notifications page!");
		}
	}

	private boolean checkOnEmailNotificationsPage() {
		return selenium.isElementPresent("//input[@id='notificationSettingCreate_view_name']");
	}
	
	public void setName(String name){
		selenium.type("//input[@id='notificationSettingCreate_view_name']", name);
	}
	
	public void setUpcomingSchedulesDates(String from, String to){
		selenium.check("//input[@id='includeUpcoming']");
		selenium.select("//select[@id='periodStart']", from);
		selenium.select("//select[@id='periodEnd']", to);
	}
	
	public void checkOverdue(){
		selenium.check("//input[@id='notificationSettingCreate_view_includeOverdue']");
	}
	
	public void checkFailed(){
		selenium.check("//input[@id='notificationSettingCreate_view_includeFailed']");
	}
	
	//Filters
	
	public void setGroup(){
		
	}
	
	public void setType(){
		
	}
	
	public void setStatus(){
		
	}
	
	public void setEmail(String email){
		
	}
	
	public void setDeliveryOptions(String frequency, boolean sendIfNoEventsOrSchedulesFound){
		
	}
	
	public void clickSave(){
		selenium.click("//input[@id='notificationSettingCreate_label_save']");
	}

}
