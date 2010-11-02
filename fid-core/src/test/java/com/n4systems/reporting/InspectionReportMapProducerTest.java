package com.n4systems.reporting;

import static com.n4systems.model.builders.EventBuilder.*;
import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.SubInspectionBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.model.inspection.AssignedToUpdate.*;
import static com.n4systems.reporting.ReportMapEntryMatcher.*;
import static com.n4systems.reporting.mapbuilders.ReportField.*;
import static com.n4systems.test.helpers.Asserts.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import com.n4systems.model.builders.AssetBuilder;
import org.junit.Test;

import com.n4systems.model.InspectionType;
import com.n4systems.model.builders.PredefinedLocationBuilder;
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
		Event masterEvent = anEvent().build();
		SubEvent targetEvent = aSubInspection("bob").withType(inspectionType).build();

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", "bob");
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new SubInspectionReportMapProducer(targetEvent, masterEvent, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		Asserts.assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspetion_map_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(inspectionType).on(targetAsset).build();

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new InspectionReportMapProducer(targetEvent, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspection_map_with_null_predefinedLocation_creation() {

		InspectionType inspectionType = anInspectionType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(inspectionType).on(targetAsset).build();

		Location advancedLocation = new Location(null, FREEFORM_LOCATION);

		targetEvent.setAdvancedLocation(advancedLocation);

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocationFullName", advancedLocation.getFullName());

		ReportMapProducer sut = new InspectionReportMapProducer(targetEvent, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspection_map_with_one_predefinedLocation_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(inspectionType).on(targetAsset).build();

		Location advancedLocation = new Location(PredefinedLocationBuilder.aPredefinedLocation().build(), FREEFORM_LOCATION);

		targetEvent.setAdvancedLocation(advancedLocation);

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocationFullName", advancedLocation.getFullName());

		ReportMapProducer sut = new InspectionReportMapProducer(targetEvent, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspection_map_with_two_predefinedLocations_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(inspectionType).on(targetAsset).build();

		PredefinedLocation parent = PredefinedLocationBuilder.aPredefinedLocation().build();
		PredefinedLocation child = PredefinedLocationBuilder.aPredefinedLocation().build();
		child.setParent(parent);

		Location advancedLocation = new Location(child, FREEFORM_LOCATION);

		targetEvent.setAdvancedLocation(advancedLocation);

		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocationFullName", advancedLocation.getFullName());

		ReportMapProducer sut = new InspectionReportMapProducer(targetEvent, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void should_have_assinged_user_null_when_no_assignment_was_done_on_an_event() {
		Event event = anEvent().withNoAssignedToUpdate().build();

		ReportMapProducer sut = new InspectionReportMapProducer(event, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), nullValue()));
	}

	@Test
	public void should_have_assinged_user_unassigned_when_an_assignment_to_unassigned_was_done() {
		Event event = anEvent().withAssignedToUpdate(unassignAsset()).build();

		ReportMapProducer sut = new InspectionReportMapProducer(event, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object) "Unassigned")));
	}

	@Test
	public void should_have_assinged_user_as_first_name_last_name_when_an_assignment_to_a_user_was_done() {
		User namedEmployee = anEmployee().withFirstName("first").withLastName("last").build();
		Event event = anEvent().withAssignedToUpdate(assignAssetToUser(namedEmployee)).build();

		ReportMapProducer sut = new InspectionReportMapProducer(event, new DefaultedDateTimeDefiner());
		ReportMap<Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object) "first last")));
	}

}
