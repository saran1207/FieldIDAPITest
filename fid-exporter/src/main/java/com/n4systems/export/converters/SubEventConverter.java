package com.n4systems.export.converters;

import com.n4systems.model.SubEvent;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SubEventConverter extends AbstractEventConverter<SubEvent> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(SubEvent.class);
	}

	@Override
	protected void marshalEntity(SubEvent event, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.startNode("SubEvent");
        writeNode(writer, context, "AssetId", event.getAsset().getMobileGUID());
        super.marshalEntity(event, writer, context);
        writeNode(writer, context, "AssetStatus", event.getAssetStatus());
		writer.endNode();
	}
}
