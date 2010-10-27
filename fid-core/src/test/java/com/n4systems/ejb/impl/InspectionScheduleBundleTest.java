package com.n4systems.ejb.impl;

import static org.hamcrest.Matchers.*;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.AssetBuilder;


public class InspectionScheduleBundleTest {

	Asset asset = AssetBuilder.anAsset().build();
	InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
	Project job = new Project();
	Date scheduleDate = new Date();
	
	@Test
	public void should_allow_the_creation_of_a_bundle() throws Exception {
		
		InspectionScheduleBundle sut = new InspectionScheduleBundle(asset, inspectionType, job, scheduleDate);
		
		Assert.assertThat(sut, notNullValue());
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_asset() throws Exception {
		
		new InspectionScheduleBundle(null, inspectionType, job, scheduleDate);
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_inspection_type() throws Exception {
		
		new InspectionScheduleBundle(asset, null, job, scheduleDate);
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_not_allow_null_for_schedule_date() throws Exception {
		
		new InspectionScheduleBundle(asset, inspectionType, job, null);
		
	}
	
	@Test
	public void should_allow_null_for_job() throws Exception {
		new InspectionScheduleBundle(asset, inspectionType, null, scheduleDate);
		
	}
}
