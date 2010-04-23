package com.n4systems.ejb.impl;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ConfigContextRequiredTestCase;


public class CreateInspectionParameterBuilderTest extends ConfigContextRequiredTestCase {

	private static final ArrayList<InspectionScheduleBundle> EMPTY_SCHEDULES = new ArrayList<InspectionScheduleBundle>();
	Long userId = 1L;
	Inspection inspection = anInspection().build();
	CreateInspectionParameterBuilder sut;
	
	@Before
	public void init() {
		 sut = new CreateInspectionParameterBuilder(inspection, userId);
	}
	
	@Test
	public void should_create_a_default_inspection_creation_parameter() throws Exception {
		CreateInspectionParameter defaultCreateInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, true, EMPTY_SCHEDULES);
		
		assertThat(sut.build(), is(equalTo(defaultCreateInpsectionParameter)));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_stop_the_inpsection_result_from_being_calculated() throws Exception {
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, false, EMPTY_SCHEDULES);
		
		sut.doNotCalculateInspectionResult();
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_a_next_inspection_date() throws Exception {
		Date nextInspectionDate = new Date();
		List<InspectionScheduleBundle> schedules = ImmutableList.of(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), nextInspectionDate));
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, nextInspectionDate, userId, null, null, true, schedules);
		
		sut.withANextInspectionDate(nextInspectionDate);
		
		CreateInspectionParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
	@Test
	public void should_create_a_create_inspection_parameter_with_no_schedules_when_given_a_null_next_inspection_date() throws Exception {
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, true, EMPTY_SCHEDULES);
		
		sut.withANextInspectionDate(null);
		
		CreateInspectionParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_a_proof_test_file_uploaded() throws Exception {
		FileDataContainer proofTestData = new FileDataContainer();
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, proofTestData, null, true, EMPTY_SCHEDULES);
		
		sut.withProofTestFile(proofTestData);
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_uploadedImages() throws Exception {
		ArrayList<FileAttachment> uploadedImages = new ArrayList<FileAttachment>();
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, uploadedImages, true, EMPTY_SCHEDULES);
		
		sut.withUploadedImages(uploadedImages);
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	
	@Test
	public void should_use_fluid_interface_add_multiple_things_to_the_create_inspection_parameter() throws Exception {
		Date nextInspectionDate = new Date();
		FileDataContainer proofTestData = new FileDataContainer();
		ArrayList<FileAttachment> uploadedImages = new ArrayList<FileAttachment>();
		List<InspectionScheduleBundle> schedules = ImmutableList.of(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), nextInspectionDate));
		
		CreateInspectionParameter expectedCreateInpsectionParameter = new CreateInspectionParameter(inspection, nextInspectionDate, userId, proofTestData, uploadedImages, false, schedules);
		
		CreateInspectionParameter actualCreateInspectionParameter = new CreateInspectionParameterBuilder(inspection, userId)
																				.withUploadedImages(uploadedImages)
																				.withANextInspectionDate(nextInspectionDate)
																				.doNotCalculateInspectionResult()
																				.withProofTestFile(proofTestData)
																				.build();
																		
		assertThat(actualCreateInspectionParameter, equalTo(expectedCreateInpsectionParameter));
	}
	
	
	@Test
	public void should_create_inspection_bundle_with_a_single_schedule_bundles_added() throws Exception {
		Date nextInspectionDate = new Date();
		
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, true, ImmutableList.of(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), nextInspectionDate)));
		
		sut.addSchedule(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), nextInspectionDate));
		
		CreateInspectionParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
	@Test
	public void should_create_inspection_bundle_with_the_list_of_schedule_bundles_added() throws Exception {
		Date nextInspectionDate = new Date();
		
		List<InspectionScheduleBundle> schedules = ImmutableList.of(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), nextInspectionDate), 
				new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), new Date(100000L)));
		
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, true, schedules);
		for (InspectionScheduleBundle bundle : schedules) {
			sut.addSchedule(bundle);
		}
		
		CreateInspectionParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
	@Test
	public void should_create_inspection_bundle_with_the_list_of_schedule_bundles_added_in_a_single_call() throws Exception {
		Date nextInspectionDate = new Date();
		
		List<InspectionScheduleBundle> schedules = ImmutableList.of(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), nextInspectionDate), 
				new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), new Date(100000L)));
		
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, true, schedules);
		sut.addSchedules(schedules);
		
		
		CreateInspectionParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
}
