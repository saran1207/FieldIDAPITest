package com.n4systems.export.converters;

import com.n4systems.model.Observation;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class ObservationConverter extends ExportConverter {

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		Observation observation = (Observation) source;
		
		writer.addAttribute("State", observation.getState().getDisplayName());
		writer.setValue(observation.getText());
	}

}
