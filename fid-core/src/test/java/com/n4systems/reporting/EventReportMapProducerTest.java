package com.n4systems.reporting;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.*;
import com.n4systems.model.builders.*;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.user.User;
import com.n4systems.test.helpers.Asserts;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static com.n4systems.model.builders.SubEventBuilder.aSubEvent;
import static com.n4systems.model.builders.UserBuilder.anEmployee;
import static com.n4systems.model.event.AssignedToUpdate.assignAssetToUser;
import static com.n4systems.model.event.AssignedToUpdate.unassignAsset;
import static com.n4systems.reporting.ReportMapEntryMatcher.hasReportEntry;
import static com.n4systems.test.helpers.Asserts.assertConatainsExpectedValues;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class EventReportMapProducerTest {

	private static final String FREEFORM_LOCATION = "FREEFORM";

	@Test
	public void test_sub_event_map_creation() {
		EventType eventType = anEventType().named("test").build();
		Event masterEvent = anEvent().build();
		SubEvent targetEvent = aSubEvent("bob").withType(eventType).build();

		Map<String, Object> expectedReportMap = new HashMap<String, Object>();
		expectedReportMap.put("productLabel", "bob");
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new SubEventReportMapProducer(targetEvent, masterEvent, new DefaultedDateTimeDefiner(), null);
		Map<String, Object> actualReportMap = sut.produceMap();

		Asserts.assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_event_map_creation() {
		EventType eventType = anEventType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(eventType).on(targetAsset).build();

		Map<String, Object> expectedReportMap = new HashMap<String, Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new EventReportMapProducer(targetEvent, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_event_map_with_null_predefinedLocation_creation() {

		EventType eventType = anEventType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(eventType).on(targetAsset).build();

		Location advancedLocation = new Location(null, FREEFORM_LOCATION);

		targetEvent.setAdvancedLocation(advancedLocation);

		Map<String, Object> expectedReportMap = new HashMap<String, Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocationFullName", advancedLocation.getFullName());

		ReportMapProducer sut = new EventReportMapProducer(targetEvent, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_event_map_with_one_predefinedLocation_creation() {
		EventType eventType = anEventType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(eventType).on(targetAsset).build();

		Location advancedLocation = new Location(PredefinedLocationBuilder.aPredefinedLocation().build(), FREEFORM_LOCATION);

		targetEvent.setAdvancedLocation(advancedLocation);

		Map<String, Object> expectedReportMap = new HashMap<String, Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocationFullName", advancedLocation.getFullName());

		ReportMapProducer sut = new EventReportMapProducer(targetEvent, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_event_map_with_two_predefinedLocations_creation() {
		EventType eventType = anEventType().named("test").build();
		Asset targetAsset = AssetBuilder.anAsset().build();
		Event targetEvent = anEvent().ofType(eventType).on(targetAsset).build();

		PredefinedLocation parent = PredefinedLocationBuilder.aPredefinedLocation().build();
		PredefinedLocation child = PredefinedLocationBuilder.aPredefinedLocation().build();
		child.setParent(parent);

		Location advancedLocation = new Location(child, FREEFORM_LOCATION);

		targetEvent.setAdvancedLocation(advancedLocation);

		Map<String, Object> expectedReportMap = new HashMap<String, Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		expectedReportMap.put("predefinedLocationFullName", advancedLocation.getFullName());

		ReportMapProducer sut = new EventReportMapProducer(targetEvent, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void should_have_assigned_user_null_when_no_assignment_was_done_on_an_event() {
		Event event = anEvent().withNoAssignedToUpdate().build();

		ReportMapProducer sut = new EventReportMapProducer(event, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo("assignedUserName"), nullValue()));
	}

	@Test
	public void should_have_assigned_user_unassigned_when_an_assignment_to_unassigned_was_done() {
		Event event = anEvent().withAssignedToUpdate(unassignAsset()).build();

		ReportMapProducer sut = new EventReportMapProducer(event, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo("assignedUserName"), equalTo((Object) "Unassigned")));
	}

	@Test
	public void should_have_assigned_user_as_first_name_last_name_when_an_assignment_to_a_user_was_done() {
		User namedEmployee = anEmployee().withFirstName("first").withLastName("last").build();
		Event event = anEvent().withAssignedToUpdate(assignAssetToUser(namedEmployee)).build();

		ReportMapProducer sut = new EventReportMapProducer(event, new DefaultedDateTimeDefiner(), null, createMockEventService());
		Map<String, Object> actualReportMap = sut.produceMap();

		assertThat(actualReportMap, hasReportEntry(equalTo("assignedUserName"), equalTo((Object) "first last")));
	}

    @Test
    public void test_criteria_help() {
        OneClickCriteriaResult criteriaResult = OneClickCriteriaResultBuilder.aCriteriaResult().state(StateBuilder.aState().build()).build();
        String instructionsHtml = "<body><p>invalidHtml<img src=\"http://www.foo.bar\">IMAGE</img></body>";
        String expected = "invalidHtmlIMAGE";
        OneClickCriteria oneClickCriteria = OneClickCriteriaBuilder.aCriteria().withDisplayText("hello").withStateSet(StateSetBuilder.aStateSet().build()).withDisplayText("").withInstructions(instructionsHtml).build();
        criteriaResult.setCriteria(oneClickCriteria);

        CriteriaSection[] sections = {
                CriteriaSectionBuilder.aCriteriaSection().withTitle("section").
                        withCriteria(oneClickCriteria).build(),
        };
        EventForm eventForm = EventFormBuilder.anEventForm().withSections(sections).build();
        EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();
        Event event = anEvent().withCriteriaResults(criteriaResult).ofType(eventType).build();
        event.setEventForm(event.getType().getEventForm());

        ReportMapProducer sut = new EventReportMapProducer(event, new DefaultedDateTimeDefiner(), null, createMockEventService());
        Map<String, Object> actualReportMap = sut.produceMap();

        //temporary hack...this is not a very testable architecture for now so i'm just going to look for expected results in map via toString() search.
        //  a correct way would do proper deep map navigation and get specific result.  there are things to mock out, dependencies to fix, and constants to create
        //  in order to do this correctly.
        assert(actualReportMap.toString().contains(expected));
    }

    private EventService createMockEventService() {
        Event event = new Event();
        EventService mockEventService = createMock(EventService.class);
        expect(mockEventService.findNextOpenEventOfSameType(anyObject(Event.class))).andReturn(event);
        expect(mockEventService.findNextOpenOrCompletedEventOfSameType(anyObject(Event.class))).andReturn(event);
        expect(mockEventService.findPreviousEventOfSameType(anyObject(Event.class))).andReturn(event);
        replay(mockEventService);
        return mockEventService;
    }

}
