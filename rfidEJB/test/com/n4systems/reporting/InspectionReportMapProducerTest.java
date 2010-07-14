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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.builders.PredefinedLocationBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.user.User;
import com.n4systems.test.helpers.Asserts;
import com.n4systems.util.ReportMap;

public class InspectionReportMapProducerTest {

	private static final String FREEFORM_LOCATION = "FREEFORM";

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
	public void test_inspection_map_with_null_predefinedLocation_creation() {

		InspectionType inspectionType = anInspectionType().named("test").build();
		Product targetProduct = ProductBuilder.aProduct().build();
		Inspection targetInspection = anInspection().ofType(inspectionType).on(targetProduct).build();

		Location advancedLocation = new Location(null, FREEFORM_LOCATION);
		List<String> locationList = new ArrayList<String>();

		targetInspection.setAdvancedLocation(advancedLocation);

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocations", locationList);

		ReportMapProducer sut = new InspectionReportMapProducer(targetInspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspection_map_with_one_predefinedLocation_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Product targetProduct = ProductBuilder.aProduct().build();
		Inspection targetInspection = anInspection().ofType(inspectionType).on(targetProduct).build();

		Location advancedLocation = new Location(PredefinedLocationBuilder.aPredefinedLocation().build(), FREEFORM_LOCATION);
		List<String> locationList = new ArrayList<String>();
		locationList.add(advancedLocation.getPredefinedLocation().getName());

		targetInspection.setAdvancedLocation(advancedLocation);

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocations", locationList);

		ReportMapProducer sut = new InspectionReportMapProducer(targetInspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspection_map_with_two_predefinedLocations_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Product targetProduct = ProductBuilder.aProduct().build();
		Inspection targetInspection = anInspection().ofType(inspectionType).on(targetProduct).build();

		PredefinedLocation parent = PredefinedLocationBuilder.aPredefinedLocation().build();
		PredefinedLocation child = PredefinedLocationBuilder.aPredefinedLocation().build();
		child.setParent(parent);

		Location advancedLocation = new Location(child, FREEFORM_LOCATION);
		List<String> locationList = new ArrayList<String>();

		locationList.add(advancedLocation.getPredefinedLocation().getParent().getName());
		locationList.add(advancedLocation.getPredefinedLocation().getName());

		targetInspection.setAdvancedLocation(advancedLocation);

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocations", locationList);

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

		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object) "Unassigned")));
	}

	@Test
	public void should_have_assinged_user_as_first_name_last_name_when_an_assignment_to_a_user_was_done() {
		User namedEmployee = anEmployee().withFirstName("first").withLastName("last").build();
		Inspection inspection = anInspection().withAssignedToUpdate(assignAssetToUser(namedEmployee)).build();

		ReportMapProducer sut = new InspectionReportMapProducer(inspection, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object) "first last")));
	}

}
