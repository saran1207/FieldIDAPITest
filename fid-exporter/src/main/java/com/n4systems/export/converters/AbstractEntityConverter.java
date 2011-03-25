package com.n4systems.export.converters;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.user.User;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class AbstractEntityConverter<T extends AbstractEntity> extends ExportConverter {

	@SuppressWarnings("unchecked")
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		marshalEntity((T) source, writer, context);
	}
	
	protected void marshalEntity(T entity, HierarchicalStreamWriter writer, MarshallingContext context) {
		writeNode(writer, context, "CreatedDate",		entity.getCreated());
		writeNode(writer, context, "LastModifiedDate",	entity.getModified());
		writeUserNode(writer, context, "CreatedBy", entity.getCreatedBy());
		writeUserNode(writer, context, "ModifiedBy", entity.getModifiedBy());
	}
	
	protected void writeUserNode(HierarchicalStreamWriter writer, MarshallingContext context, String fieldName, User user) {
		/*
		 * These are generally lazy loaded which causes a resolves-to attribute in the results
		 * if we try and serialize the object itself.  To avoid this, we can use the name instead
		 */
		writer.startNode(fieldName);
		if (user != null) {
			context.convertAnother(user.getFullName());
		}
		writer.endNode();
	}
}
