package com.n4systems.export.converters;

import com.n4systems.model.parents.EntityWithOwner;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class EntityWithOwnerConverter<T extends EntityWithOwner> extends AbstractEntityConverter<T> {

	@Override
	protected void marshalEntity(T entity, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalEntity(entity, writer, context);
		
		context.convertAnother(entity.getOwner());
	}

}
