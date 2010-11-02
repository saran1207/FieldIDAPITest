package com.n4systems.ejb.impl;

import static org.hamcrest.Matchers.*;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.AssetBuilder;


public class InspectionScheduleBundleTest {

	Asset asset = AssetBuilder.anAsset().build();
	EventType eventType = EventTypeBuilder.anEventType().build();
	Project job = new Project();
	Date scheduleDate = new Date();
	
	@Test
	public void should_allow_the_creation_of_a_bundle() throws Exception {
		
		InspectionScheduleBundle sut = new InspectionScheduleBundle(asset, eventType, job, scheduleDate);
		
		Assert.assertThat(sut, notNullValue());
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_asset() throws Exception {
		
		new InspectionScheduleBundle(null, eventType, job, scheduleDate);
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_inspection_type() throws Exception {
		
		new InspectionScheduleBundle(asset, null, job, scheduleDate);
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_schedule_date() throws Exception {
		
		new InspectionScheduleBundle(asset, eventType, job, null);
		
	}
	
	@Test
	public void should_allow_null_for_job() throws Exception {
		new InspectionScheduleBundle(asset, eventType, null, scheduleDate);
		
	}
}
