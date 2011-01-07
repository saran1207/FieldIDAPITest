package com.n4systems.ejb.impl;

import static com.n4systems.model.builders.EventBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Event;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ConfigContextRequiredTestCase;


public class CreateEventParameterBuilderTest extends ConfigContextRequiredTestCase {

	private static final ArrayList<EventScheduleBundle> EMPTY_SCHEDULEs = new ArrayList<EventScheduleBundle>();
	Long userId = 1L;
	Event event = anEvent().build();
	CreateEventParameterBuilder sut;
	
	@Before
	public void init() {
		 sut = new CreateEventParameterBuilder(event, userId);
	}
	
	@Test
	public void should_create_a_default_event_creation_parameter() throws Exception {
		CreateEventParameter defaultCreateInpsectionParameter = new CreateEventParameter(event, null, userId, null, null, true, EMPTY_SCHEDULEs, null);
		
		assertThat(sut.build(), is(equalTo(defaultCreateInpsectionParameter)));
	}
	
	
	@Test
	public void should_create_a_create_event_parameter_with_stop_the_inpsection_result_from_being_calculated() throws Exception {
		CreateEventParameter createInpsectionParameter = new CreateEventParameter(event, null, userId, null, null, false, EMPTY_SCHEDULEs, null);
		
		sut.doNotCalculateEventResult();
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_event_parameter_with_a_next_event_date() throws Exception {
		Date nextEventDate = new Date();
		List<EventScheduleBundle> schedules = ImmutableList.of(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate));
		CreateEventParameter createEventParameter = new CreateEventParameter(event, nextEventDate, userId, null, null, true, schedules, null);
		
		sut.withANextEventDate(nextEventDate);
		
		CreateEventParameter build = sut.build();
		assertThat(build, equalTo(createEventParameter));
	}
	
	@Test
	public void should_create_a_create_event_parameter_with_no_schedules_when_given_a_null_next_event_date() throws Exception {
		CreateEventParameter createEventParameter = new CreateEventParameter(event, null, userId, null, null, true, EMPTY_SCHEDULEs, null);
		
		sut.withANextEventDate(null);
		
		CreateEventParameter build = sut.build();
		assertThat(build, equalTo(createEventParameter));
	}
	
	
	@Test
	public void should_create_a_create_event_parameter_with_a_proof_test_file_uploaded() throws Exception {
		FileDataContainer proofTestData = new FileDataContainer();
		CreateEventParameter createEventParameter = new CreateEventParameter(event, null, userId, proofTestData, null, true, EMPTY_SCHEDULEs, null);
		
		sut.withProofTestFile(proofTestData);
		
		assertThat(sut.build(), equalTo(createEventParameter));
	}
	
	
	@Test
	public void should_create_a_create_event_parameter_with_uploaded_images() throws Exception {
		ArrayList<FileAttachment> uploadedImages = new ArrayList<FileAttachment>();
		CreateEventParameter createEventParameter = new CreateEventParameter(event, null, userId, null, uploadedImages, true, EMPTY_SCHEDULEs, null);
		
		sut.withUploadedImages(uploadedImages);
		
		assertThat(sut.build(), equalTo(createEventParameter));
	}
	
	
	@Test
	public void should_use_fluid_interface_add_multiple_things_to_the_create_event_parameter() throws Exception {
		Date nextEventDate = new Date();
		FileDataContainer proofTestData = new FileDataContainer();
		ArrayList<FileAttachment> uploadedImages = new ArrayList<FileAttachment>();
		List<EventScheduleBundle> schedules = ImmutableList.of(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate));
		
		CreateEventParameter expectedCreateEventParameter = new CreateEventParameter(event, nextEventDate, userId, proofTestData, uploadedImages, false, schedules, null);
		
		CreateEventParameter actualCreateEventParameter = new CreateEventParameterBuilder(event, userId)
																				.withUploadedImages(uploadedImages)
																				.withANextEventDate(nextEventDate)
																				.doNotCalculateEventResult()
																				.withProofTestFile(proofTestData)
																				.build();
																		
		assertThat(actualCreateEventParameter, equalTo(expectedCreateEventParameter));
	}
	
	
	@Test
	public void should_create_event_bundle_with_a_single_schedule_bundles_added() throws Exception {
		Date nextEventDate = new Date();
		
		CreateEventParameter createEventParameter = new CreateEventParameter(event, null, userId, null, null, true, ImmutableList.of(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate)), null);
		
		sut.addSchedule(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate));
		
		CreateEventParameter build = sut.build();
		assertThat(build, equalTo(createEventParameter));
	}
	
	@Test
	public void should_create_event_bundle_with_the_list_of_schedule_bundles_added() throws Exception {
		Date nextEventDate = new Date();
		
		List<EventScheduleBundle> schedules = ImmutableList.of(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate),
				new EventScheduleBundle(event.getAsset(), event.getType(), null, new Date(100000L)));
		
		CreateEventParameter createInpsectionParameter = new CreateEventParameter(event, null, userId, null, null, true, schedules, null);
		for (EventScheduleBundle bundle : schedules) {
			sut.addSchedule(bundle);
		}
		
		CreateEventParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
	@Test
	public void should_create_event_bundle_with_the_list_of_schedule_bundles_added_in_a_single_call() throws Exception {
		Date nextEventDate = new Date();
		
		List<EventScheduleBundle> schedules = ImmutableList.of(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate),
				new EventScheduleBundle(event.getAsset(), event.getType(), null, new Date(100000L)));
		
		CreateEventParameter createInpsectionParameter = new CreateEventParameter(event, null, userId, null, null, true, schedules, null);
		sut.addSchedules(schedules);
		
		
		CreateEventParameter build = sut.build();
		assertThat(build, equalTo(createInpsectionParameter));
	}
	
}
