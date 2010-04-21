package com.n4systems.ejb.impl;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ConfigContextRequiredTestCase;


public class CreateInspectionParameterBuilderTest extends ConfigContextRequiredTestCase {

	Long userId = 1L;
	Inspection inspection = anInspection().build();
	CreateInspectionParameterBuilder sut;
	
	@Before
	public void init() {
		 sut = new CreateInspectionParameterBuilder(inspection, userId);
	}
	
	@Test
	public void should_create_a_default_inspection_creation_parameter() throws Exception {
		CreateInspectionParameter defaultCreateInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, true);
		
		assertThat(sut.build(), is(equalTo(defaultCreateInpsectionParameter)));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_stop_the_inpsection_result_from_being_calculated() throws Exception {
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, null, false);
		
		sut.doNotCalculateInspectionResult();
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_a_next_inspection_date() throws Exception {
		Date nextInspectionDate = new Date();
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, nextInspectionDate, userId, null, null, true);
		
		sut.withANextInspectionDate(nextInspectionDate);
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_a_proof_test_file_uploaded() throws Exception {

		FileDataContainer proofTestData = new FileDataContainer();
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, proofTestData, null, true);
		
		sut.withProofTestFile(proofTestData);
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	@Test
	public void should_create_a_create_inspection_parameter_with_uploadedImages() throws Exception {
		ArrayList<FileAttachment> uploadedImages = new ArrayList<FileAttachment>();
		CreateInspectionParameter createInpsectionParameter = new CreateInspectionParameter(inspection, null, userId, null, uploadedImages, true);
		
		sut.withUploadedImages(uploadedImages);
		
		assertThat(sut.build(), equalTo(createInpsectionParameter));
	}
	
	
	
	@Test
	public void should_use_fluid_interface_add_multiple_things_to_the_create_inspection_parameter() throws Exception {
		Date nextInspectionDate = new Date();
		FileDataContainer proofTestData = new FileDataContainer();
		ArrayList<FileAttachment> uploadedImages = new ArrayList<FileAttachment>();
		
		CreateInspectionParameter expectedCreateInpsectionParameter = new CreateInspectionParameter(inspection, nextInspectionDate, userId, proofTestData, uploadedImages, false);
		
		CreateInspectionParameter actualCreateInspectionParameter = new CreateInspectionParameterBuilder(inspection, userId)
																				.withUploadedImages(uploadedImages)
																				.withANextInspectionDate(nextInspectionDate)
																				.doNotCalculateInspectionResult()
																				.withProofTestFile(proofTestData)
																				.build();
																		
		assertThat(actualCreateInspectionParameter, equalTo(expectedCreateInpsectionParameter));
	}
	
}
