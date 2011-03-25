package com.n4systems.export.converters;

import com.n4systems.model.Project;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ProjectConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Project.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		Project project = (Project) source;
		
		writeNode(writer, context, "Id", project.getProjectID());
		writeNode(writer, context, "Name", project.getName());
	}

}
