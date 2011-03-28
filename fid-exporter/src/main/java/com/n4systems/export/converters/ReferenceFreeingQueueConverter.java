package com.n4systems.export.converters;

import java.util.Queue;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.NotImplementedException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ReferenceFreeingQueueConverter extends AbstractCollectionConverter {
	private final EntityManager em;
	
	public ReferenceFreeingQueueConverter(Mapper mapper, EntityManager em) {
		super(mapper);
		this.em = em;
	}

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return Queue.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		Queue<?> queue = (Queue<?>) source;
		
		Object item;
		while ((item = queue.poll()) != null) {
			writeItem(item, context, writer);
			em.detach(item);
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		throw new NotImplementedException();
	}

}
