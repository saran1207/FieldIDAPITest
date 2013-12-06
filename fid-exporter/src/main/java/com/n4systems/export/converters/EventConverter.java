package com.n4systems.export.converters;

import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class EventConverter extends AbstractEventConverter<ThingEvent> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Event.class);
	}

	@Override
	protected void marshalEntity(ThingEvent event, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.startNode("Event");
        writeNode(writer, context, "AssetId", event.getAsset().getMobileGUID());
		super.marshalEntity(event, writer, context);
        writeNode(writer, context, "AssetStatus", event.getAssetStatus());
		writeNode(writer, context, "ActiveState", event.getEntityState());
		writeNode(writer, context, "Date", event.getDate());
		writeNode(writer, context, "EventResult", event.getEventResult());
		writeUserNode(writer, context, "PerformedBy", event.getPerformedBy());
		context.convertAnother(event.getOwner());
		writeNode(writer, context, "AssignedTo", event.getAssignedTo());
		writeNode(writer, context, "Location", event.getAdvancedLocation());
		writeNode(writer, context, "EventBook", event.getBook());
		writeNode(writer, context, "ProofTestInfo", event.getProofTestInfo());
		writeIterable(writer, context, "SubEvents", event.getSubEvents(), new SubEventConverter());
		writer.endNode();
	}

}
