package com.n4systems.export.converters;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class EventScheduleConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(EventSchedule.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		EventSchedule schedule = (EventSchedule) source;
		writeNode(writer, context, "Status", toFriendlyName(schedule.getStatus()));
		writeNode(writer, context, "ScheduleDate", schedule.getNextDate());
		writeNode(writer, context, "CompletedDate", schedule.getCompletedDate());
		writeNode(writer, context, "EventType", schedule.getEventType());
		writeNode(writer, context, "Location", schedule.getAdvancedLocation());
		writeNode(writer, context, "Job", schedule.getProject());
	}

	private String toFriendlyName(ScheduleStatus status) {
		switch (status) {
			case COMPLETED:
				return "Completed";
			case IN_PROGRESS:
				return "In Progress";
			case SCHEDULED:
				return "Scheduled";
			default:
				return "";
		}
	}
}
