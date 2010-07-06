package com.n4systems.reporting;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.SubInspectionBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.model.inspection.AssignedToUpdate.*;
import static com.n4systems.reporting.ReportMapEntryMatcher.*;
import static com.n4systems.reporting.mapbuilders.ReportField.*;
import static com.n4systems.test.helpers.Asserts.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.user.User;
import com.n4systems.test.helpers.Asserts;
import com.n4systems.util.ReportMap;

public class InspectionReportMapProducerTest {

	@Test
	public void test_sub_inspetion_map_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Inspection masterInspection = anInspection().build();
		SubInspection targetInspection = aSubInspection("bob").withType(inspectionType).build();
		
		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", "bob");
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new SubInspectionReportMapProducer(targetInspection, masterInspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		Asserts.assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspetion_map_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Product targetProduct = ProductBuilder.aProduct().build();
		Inspection targetInspection = anInspection().ofType(inspectionType).on(targetProduct).build();
		
		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new InspectionReportMapProducer(targetInspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}
	
	
	@Test
	public void should_have_assinged_user_null_when_no_assignment_was_done_on_an_event() {
		Inspection inspection = anInspection().withNoAssignedToUpdate().build();
		
		
		ReportMapProducer sut = new InspectionReportMapProducer(inspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), nullValue()));
	}
	
	@Test
	public void should_have_assinged_user_unassigned_when_an_assignment_to_unassigned_was_done() {
		Inspection inspection = anInspection().withAssignedToUpdate(unassignAsset()).build();
		
		
		ReportMapProducer sut = new InspectionReportMapProducer(inspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object)"Unassigned")));
	}
	

	

	@Test
	public void should_have_assinged_user_as_first_name_last_name_when_an_assignment_to_a_user_was_done() {
		User namedEmployee = anEmployee().withFirstName("first").withLastName("last").build();
		Inspection inspection = anInspection().withAssignedToUpdate(assignAssetToUser(namedEmployee)).build();
		
		
		ReportMapProducer sut = new InspectionReportMapProducer(inspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object)"first last")));
	}
	
	
	
}
