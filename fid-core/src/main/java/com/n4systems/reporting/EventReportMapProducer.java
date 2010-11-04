package com.n4systems.reporting;

import java.io.File;
import java.util.Date;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.reporting.mapbuilders.ReportField;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;

public class EventReportMapProducer extends AbsractEventReportMapProducer {
	private static final String UNASSIGNED_USER_NAME = "Unassigned";

	private final Event event;

	public EventReportMapProducer(Event event, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
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

		add(ReportField.ASSIGNED_USER.getParamKey(), assignedUserName());

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
