package com.n4systems.fieldid.selenium.testcase.schedules;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.schedules.EventSchedulePicker;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddScheduleTest extends PageNavigatingTestCase<AssetPage> {
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
	static final String TEST_DATE = DATE_FORMAT.format(new Date());
	static final String TEST_EVENT_TYPE = "Check In";
	static final String TEST_JOB = "001";
    static final String TEST_SERIAL_NUMBER = "Asset-001";

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.primaryOrgFor("test1").setExtendedFeatures(setOf(ExtendedFeature.Projects));
        scenario.aJob().withTitle(TEST_JOB).withProjectID(TEST_JOB).status("").build();

        EventType eventType = scenario.anEventType().named(TEST_EVENT_TYPE).build();
        AssetType assetType = scenario.anAssetType().withEventTypes(eventType).build();

        scenario.anAsset()
                .ofType(assetType)
                .withIdentifier(TEST_SERIAL_NUMBER)
                .build();
    }

	@Override
	protected AssetPage navigateToPage() {
		return startAsCompany("test1").login().search(TEST_SERIAL_NUMBER);
	}

	@Test
	public void save_schedule_with_error() throws Exception {
        EventSchedulePicker eventSchedulePicker = page.clickScheduleEvent();
        eventSchedulePicker.clickCreateSchedule();
		assertFalse(eventSchedulePicker.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void save_schedule() throws Exception {
        EventSchedulePicker eventSchedulePicker = page.clickScheduleEvent();
        eventSchedulePicker.setSchedule(TEST_DATE, TEST_EVENT_TYPE, TEST_JOB);
        eventSchedulePicker.clickCreateSchedule();
		assertTrue(page.checkScheduleExists(TEST_DATE, TEST_EVENT_TYPE, TEST_JOB));
	}
	
	@Test
	public void edit_schedule() throws Exception {
        EventSchedulePicker eventSchedulePicker = page.clickScheduleEvent();
        eventSchedulePicker.setSchedule(TEST_DATE, TEST_EVENT_TYPE, TEST_JOB);
        eventSchedulePicker.clickCreateSchedule();
        assertTrue(page.checkScheduleExists(TEST_DATE, TEST_EVENT_TYPE, TEST_JOB));
		
		String newDate = getNewDate();
        eventSchedulePicker = page.clickEditSchedule(TEST_DATE, TEST_EVENT_TYPE, TEST_JOB);
        eventSchedulePicker.setSchedule(newDate, TEST_EVENT_TYPE, TEST_JOB);
        eventSchedulePicker.clickSaveSchedule();
		assertTrue(page.checkScheduleExists(newDate, TEST_EVENT_TYPE, TEST_JOB));
	}

	private String getNewDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		return DATE_FORMAT.format(cal.getTime());
	}

}
