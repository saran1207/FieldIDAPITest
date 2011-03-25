package com.n4systems.export.converters;

import com.n4systems.model.Recommendation;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RecommendationConverter extends ObservationConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Recommendation.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.startNode("Recommendation");
		super.marshal(source, writer, context);
		writer.endNode();
	}

}
