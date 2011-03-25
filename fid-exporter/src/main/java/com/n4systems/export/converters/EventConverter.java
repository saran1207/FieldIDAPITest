package com.n4systems.export.converters;

import com.n4systems.model.Event;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class EventConverter extends AbstractEventConverter<Event> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Event.class);
	}

	@Override
	protected void marshalEntity(Event event, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.startNode("Event");
		super.marshalEntity(event, writer, context);
		writeNode(writer, context, "ActiveState", event.getEntityState());
		writeNode(writer, context, "Date", event.getDate());
		writeNode(writer, context, "EventResult", event.getStatus());
		writeUserNode(writer, context, "PerformedBy", event.getPerformedBy());
		context.convertAnother(event.getOwner());
		writeNode(writer, context, "AssignedTo", event.getAssignedTo());
		writeNode(writer, context, "Location", event.getAdvancedLocation());
		writeNode(writer, context, "EventBook", event.getBook());
		writeNode(writer, context, "ProofTestInfo", event.getProofTestInfo());
		writeNode(writer, context, "Schedule", event.getSchedule());
		writeIterable(writer, context, "SubEvents", event.getSubEvents());
		writer.endNode();
	}

}
