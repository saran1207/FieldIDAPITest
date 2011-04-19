package com.n4systems.fieldid.selenium.schedule;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.schedules.EventSchedulePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.model.orgs.BaseOrg;

public class EventScheduleTest extends PageNavigatingTestCase<EventSchedulePage> {

	private static String COMPANY = "test1";
	private static String ASSET = "someAsset";
	private static String DATE = "12/12/12";
	private static String JOB_NAME = "hurf";

	@Override
	public void setupScenario(Scenario scenario) {

		scenario.primaryOrgFor(COMPANY).setExtendedFeatures(setOf(ExtendedFeature.ReadOnlyUser, ExtendedFeature.Projects));
		
		EventType evenType = scenario.anEventType().build();
		
        AssetType type = scenario.anAssetType()
        				.named("Gantry Crane - Cab Controlled")
        				.withEventTypes(evenType)
        				.build();

    	Asset asset = scenario.anAsset().ofType(type).withSerialNumber(ASSET).build();
        
        BaseOrg custOrg = scenario.aCustomerOrg()
						    .withParent(scenario.primaryOrgFor(COMPANY))
				    	    .withName("hurf")
				    	    .build();
    	
        Project eventJob= scenario.aJob().
							withProjectID("someid")
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
		addSchedule();
		
		assertTrue("Schedule wasn't successfully created", selenium.isElementPresent("//div[contains(.,'"+DATE+"')]"));
	}
	
	@Test
	public void remove_a_schedule(){
		addSchedule();
		selenium.click("//a[contains(.,'Remove')]");
		
		assertTrue("Schedule wasn't successfully removed", selenium.isElementPresent("//div[@id='schedulesBlankSlate']"));
	}
	
	@Test
	public void add_a_schedule_with_job(){
		page.clickAddSchedule();
		page.enterScheduleDate(DATE);
		page.selectEventType("some Name");
		page.selectJob("hurf");
		page.clickSave();
		
		assertTrue("Schedule wasn't successfully attached to a job", selenium.isElementPresent("//a[contains(.,"+JOB_NAME+")]"));
	}
	
	
	private void addSchedule(){
		page.clickAddSchedule();
		page.enterScheduleDate(DATE);
		page.selectEventType("some Name");
		page.clickSave();
	}
}
