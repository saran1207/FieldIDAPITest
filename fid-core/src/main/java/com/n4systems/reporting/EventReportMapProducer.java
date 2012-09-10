package com.n4systems.reporting;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;

import java.io.File;
import java.util.Date;

public class EventReportMapProducer extends AbsractEventReportMapProducer {
	private static final String UNASSIGNED_USER_NAME = "Unassigned";

	private final Event event;

	public EventReportMapProducer(Event event, DateTimeDefinition dateTimeDefinition, S3Service s3Service) {
		super(dateTimeDefinition, s3Service);
		this.event = event;
	}

	@Override
	protected void eventParameter() {
		Event event = (Event) this.getEvent();
		add("productLabel", null);
		add("location", event.getAdvancedLocation().getFreeformLocation());
		add("predefinedLocationFullName", event.getAdvancedLocation().getFullName());
		add("inspectionBook", (event.getBook() != null) ? event.getBook().getName() : null);
		add("inspectionResult", event.getStatus().getDisplayName());
		add("proofTestInfo", addProofTestInfoParams(event));

        add("latitude", event.getGpsLocation() != null ? event.getGpsLocation().getLatitude() : "");
        add("longitude", event.getGpsLocation() != null ? event.getGpsLocation().getLongitude() : "");

		add("assignedUserName", assignedUserName());

		add("priority", event.getPriority() != null ? event.getPriority().getName() : "");
		add("assignee", event.getAssignee() != null ? event.getAssignee().getFullName() : "");

		fillInDate(event);
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
	protected AbstractEvent getEvent() {
		return event;
	}
}
