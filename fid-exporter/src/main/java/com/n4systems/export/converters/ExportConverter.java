package com.n4systems.export.converters;

import java.util.Iterator;

import com.n4systems.exceptions.NotImplementedException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class ExportConverter implements Converter {
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		throw new NotImplementedException();
	}
	
	protected void writeNode(HierarchicalStreamWriter writer, MarshallingContext context, String name, Object value) {
		writer.startNode(name);
		if (value != null) {
			context.convertAnother(value);
		}
		writer.endNode();
	}
	
	protected void writeIterable(HierarchicalStreamWriter writer, MarshallingContext context, String name, Iterable<?> value) {
		writer.startNode(name);
		if (value != null) {
			for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
				context.convertAnother(iter.next());
			}
		}
		writer.endNode();
	}
	
	
}
