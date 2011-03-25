package com.n4systems.export.converters;

import com.n4systems.model.Deficiency;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeficiencyConverter extends ObservationConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Deficiency.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.startNode("Deficiency");
		super.marshal(source, writer, context);
		writer.endNode();
	}

}