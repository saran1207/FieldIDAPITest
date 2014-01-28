package com.n4systems.reporting;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.model.*;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;

import java.io.File;
import java.util.Date;

public class EventReportMapProducer extends AbsractEventReportMapProducer {
	private static final String UNASSIGNED_USER_NAME = "Unassigned";

	private final ThingEvent event;
    private EventService eventService;

    public EventReportMapProducer(ThingEvent event, DateTimeDefinition dateTimeDefinition, S3Service s3Service, EventService eventService, LastEventDateService lastEventDateService) {
		super(dateTimeDefinition, s3Service, lastEventDateService);
		this.event = event;
        this.eventService = eventService;
    }

	@Override
	protected void eventParameter() {
        ThingEvent event = (ThingEvent) this.getEvent();

        addNextAndPreviousData(event);
		add("productLabel", null);
		add("location", event.getAdvancedLocation().getFreeformLocation());
		add("predefinedLocationFullName", event.getAdvancedLocation().getFullName());
		add("inspectionBook", (event.getBook() != null) ? event.getBook().getName() : null);
		add("inspectionResult", event.getEventResult().getDisplayName());
        add("eventStatus", event.getEventStatus() != null ? event.getEventStatus().getDisplayName() : "");
        add("productStatus", event.getAsset().getAssetStatus()!=null ? event.getAsset().getAssetStatus().getName() : "");
		add("proofTestInfo", addProofTestInfoParams(event));

        add("latitude", event.getGpsLocation() != null ? event.getGpsLocation().getLatitude() : "");
        add("longitude", event.getGpsLocation() != null ? event.getGpsLocation().getLongitude() : "");

		add("assignedUserName", assignedUserName());

		add("priority", event.getPriority() != null ? event.getPriority().getName() : "");
		add("assignee", event.getAssignee() != null ? event.getAssignee().getFullName() : "");

		fillInDate(event);
	}

    private void addNextAndPreviousData(ThingEvent event) {
        Event nextOpenEvent = eventService.findNextOpenEventOfSameType(event);
        Event nextEvent = eventService.findNextOpenOrCompletedEventOfSameType(event);
        Event previousEvent = eventService.findPreviousEventOfSameType(event);

        add("nextOpenEventByTypeDueDate", nextOpenEvent == null ? null : DateHelper.convertToUserTimeZone(nextOpenEvent.getDueDate(), dateTimeDefinition.getTimeZone()));

        add("nextEventByTypeDueDate", nextEvent == null ? null : DateHelper.convertToUserTimeZone(nextEvent.getDueDate(), dateTimeDefinition.getTimeZone()));
        add("nextEventByTypeDate", nextEvent == null ? null : DateHelper.convertToUserTimeZone(nextEvent.getDate(), dateTimeDefinition.getTimeZone()));

        add("lastEventByTypeDueDate", previousEvent == null ? null : DateHelper.convertToUserTimeZone(previousEvent.getDueDate(), dateTimeDefinition.getTimeZone()));
        add("lastEventByTypeDate", previousEvent == null ? null : DateHelper.convertToUserTimeZone(previousEvent.getDate(), dateTimeDefinition.getTimeZone()));
    }

	private String assignedUserName() {
		if (!event.hasAssignToUpdate()) {
			return null;
		} else if (event.getAssignedTo().isUnassigned()) {
			return UNASSIGNED_USER_NAME;
		} else {
			return event.getAssignedTo().getAssignedUser().getUserLabel();
		}

	}

	private void fillInDate(Event event) {
		String performedDateAsString = formatDate(event.getDate(), true);
		Date performedDateTimeZoneShifted = DateHelper.convertToUserTimeZone(event.getDate(), dateTimeDefinition.getTimeZone());

		add("inspectionDate", performedDateAsString);
		add("datePerformed", performedDateAsString);

		add("datePerformed_date", performedDateTimeZoneShifted);
		add("inspectionDate_date", performedDateTimeZoneShifted);
	}

	@Override
	protected File imagePath(FileAttachment imageAttachment) {
		return PathHandler.getEventAttachmentFile((Event) getEvent(), imageAttachment);
	}

	@Override
	protected AbstractEvent<ThingEventType,Asset> getEvent() {
		return event;
	}
}
