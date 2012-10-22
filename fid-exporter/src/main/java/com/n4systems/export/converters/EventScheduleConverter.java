package com.n4systems.export.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@Deprecated // TODO : WEB-2358  remove this.
public class EventScheduleConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
        throw new UnsupportedOperationException("EventSchedules no longer exist");
    }
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        throw new UnsupportedOperationException("EventSchedules no longer exist");
    }

}
