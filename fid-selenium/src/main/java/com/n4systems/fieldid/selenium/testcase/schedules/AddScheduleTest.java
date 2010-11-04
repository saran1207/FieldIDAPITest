package com.n4systems.fieldid.selenium.testcase.schedules;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.EventPage;

public class AddScheduleTest extends PageNavigatingTestCase<AssetPage>{
	
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
	
	String testDate = format.format(new Date());
	
	String testEventType = "Check In";
	
	String testJob = "001";
	
	@Before
	public void setUp() {
		if (page.checkScheduleExists(testDate, testEventType, testJob)) {
			page.clickRemoveSchdeule(testDate, testEventType, testJob);
		}
	}

	@Override
	protected AssetPage navigateToPage() {
		return startAsCompany("n4").login().search("Test Add Schedules");
	}

	@Test
	public void save_schedule_with_error() throws Exception {
		page.clickSchedulesTab();
		page.clickSaveSchedule();
		assertFalse(page.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void save_and_remove_schedule() throws Exception {
		page.clickSchedulesTab();
		page.setSchedule(testDate, testEventType, testJob);
		page.clickSaveSchedule();
		assertTrue(page.checkScheduleExists(testDate, testEventType, testJob));
		page.clickRemoveSchdeule(testDate, testEventType, testJob);
		assertFalse(page.checkScheduleExists(testDate, testEventType, testJob));
	}
	
	@Test
	public void edit_and_remove_schedule() throws Exception {
		page.clickSchedulesTab();
		page.setSchedule(testDate, testEventType, testJob);
		page.clickSaveSchedule();
		assertTrue(page.checkScheduleExists(testDate, testEventType, testJob));
		
		String newDate = getNewdate();
		page.clickEditSchedule(testDate, testEventType, testJob);
		page.editScheduleDate(testDate, testEventType, newDate);
		page.clickEditSaveSchedule(testEventType);
		assertTrue(page.checkScheduleExists(newDate, testEventType, testJob));
		
		page.clickRemoveSchdeule(newDate, testEventType, testJob);
		assertFalse(page.checkScheduleExists(newDate, testEventType, testJob));
	}
	
	@Test
	public void schedule_event_now_stop_progress_test() {
		page.clickSchedulesTab();
		page.setSchedule(testDate, testEventType, testJob);
		page.clickSaveSchedule();
		assertTrue(page.checkScheduleExists(testDate, testEventType, testJob));
		EventPage eventPage = page.clickInpectNow(testDate, testEventType, testJob);
		eventPage.clickAssetInformationTab();
		page.clickSchedulesTab();
		page.clickStopProgress(testDate, testEventType, testJob);
		
		page.clickRemoveSchdeule(testDate, testEventType, testJob);
		assertFalse(page.checkScheduleExists(testDate, testEventType, testJob));
	}
	private String getNewdate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		return format.format(cal.getTime());
	}
}
