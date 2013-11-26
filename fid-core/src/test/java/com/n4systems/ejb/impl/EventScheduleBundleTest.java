package com.n4systems.ejb.impl;

import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.notNullValue;


public class EventScheduleBundleTest {

	Asset asset = AssetBuilder.anAsset().build();
	ThingEventType eventType = EventTypeBuilder.anEventType().build();
	Project job = new Project();
	Date scheduleDate = new Date();
	
	@Test
	public void should_allow_the_creation_of_a_bundle() throws Exception {
		
		EventScheduleBundle sut = new EventScheduleBundle(asset, eventType, job, scheduleDate);
		
		Assert.assertThat(sut, notNullValue());
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_asset() throws Exception {
		
		new EventScheduleBundle(null, eventType, job, scheduleDate);
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_event_type() throws Exception {
		
		new EventScheduleBundle(asset, null, job, scheduleDate);
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_schedule_date() throws Exception {
		
		new EventScheduleBundle(asset, eventType, job, null);
		
	}
	
	@Test
	public void should_allow_null_for_job() throws Exception {
		new EventScheduleBundle(asset, eventType, null, scheduleDate);
		
	}
}
