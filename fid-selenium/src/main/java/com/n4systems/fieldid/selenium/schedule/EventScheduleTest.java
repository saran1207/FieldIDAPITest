package com.n4systems.fieldid.selenium.schedule;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.ibm.icu.text.SimpleDateFormat;
import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.schedules.EventSchedulePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.BaseOrg;

public class EventScheduleTest extends PageNavigatingTestCase<EventSchedulePage> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	private static String COMPANY = "test1";
	private static String ASSET = "someAsset";
	private static String DATE = "12/12/12";
	private static String JOB_NAME = "hurf";

	@Override
	public void setupScenario(Scenario scenario) {

		scenario.primaryOrgFor(COMPANY).setExtendedFeatures(setOf(ExtendedFeature.Projects));
		
		EventType evenType = scenario.anEventType().build();
		
        AssetType type = scenario.anAssetType()
        				.named("Gantry Crane - Cab Controlled")
        				.withEventTypes(evenType)
        				.build();

    	scenario.anAsset().ofType(type).withSerialNumber(ASSET).build();
        
        BaseOrg custOrg = scenario.aCustomerOrg()
						    .withParent(scenario.primaryOrgFor(COMPANY))
				    	    .withName("hurf")
				    	    .build();
    	
        scenario.aJob()
                .withProjectID("someid")
                .withTitle(JOB_NAME)
                .status("")
                .withOwner(custOrg)
                .build();
			
	}

	@Override
	protected EventSchedulePage navigateToPage() {
		return startAsCompany(COMPANY).systemLogin().search(ASSET).clickSchedulesTab();
	}

	@Test
	public void add_new_schedule(){
		addSchedule(DATE);
		
		List<String> dates = page.getScheduleDates();

		assertEquals("Schedule wasn't successfully created", 1, dates.size());
		assertEquals(DATE, dates.get(0));
	}
	
	@Test
	public void remove_a_schedule(){
		addSchedule(DATE);

		List<String> dates = page.getScheduleDates();

		assertEquals(1, dates.size());
		assertEquals(DATE, dates.get(0));
		
		page.removeSchedule(DATE);
		
		assertFalse("Schedule wasn't successfully removed", page.hasSchedules());
	}
	
	@Test
	public void add_new_schedules_list_is_sorted() throws Exception {
		String today = dateFormat.format(new Date());
		String nextweek = dateFormat.format(DateUtils.addDays(new Date(), 7));

		addSchedule(DATE);
		addSchedule(today);
		addSchedule(nextweek);
		
		List<String> dates = page.getScheduleDates();
		
		assertEquals(3, dates.size());
		assertEquals(today, dates.get(0));
		assertEquals(nextweek, dates.get(1));
		assertEquals(DATE, dates.get(2));
		
		page.removeSchedule(nextweek);
		
		dates = page.getScheduleDates();
		
		assertEquals(2, dates.size());
		assertEquals(today, dates.get(0));
		assertEquals(DATE, dates.get(1));		
	}
	
	@Test
	public void add_a_schedule_with_job(){
		page.clickAddSchedule();
		page.enterScheduleDate(DATE);
		page.selectEventType("some Name");
		page.selectJob("hurf");
		page.clickSave();
		
		List<String> dates = page.getScheduleDates();

		assertEquals(1, dates.size());
		assertEquals(DATE, dates.get(0));

		assertEquals("Schedule wasn't successfully attached to a job", "hurf", page.getScheduleJob(DATE));
	}
	
	
	private void addSchedule(String date){
		page.clickAddSchedule();
		page.enterScheduleDate(date);
		page.selectEventType("some Name");
		page.clickSave();
	}
}
