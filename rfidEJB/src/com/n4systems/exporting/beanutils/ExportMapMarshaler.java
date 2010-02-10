package com.n4systems.exporting.beanutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportMapMarshaler<T> {
	private final Class<T> beanClass;
	private final SerializationHandlerFactory handlerFactory;
	
	// initialize the handlers lazily so we don't have to throw
	// exceptions out of the constructor
	private SerializationHandler[] handlers;
	
	public ExportMapMarshaler(Class<T> beanClass) {
		this(beanClass, new SerializationHandlerFactory());
	}
	
	public ExportMapMarshaler(Class<T> beanClass, SerializationHandlerFactory handlerFactory) {
		this.beanClass = beanClass;
		this.handlerFactory = handlerFactory;	
	}

	private void initHandlers(Class<T> beanClass, SerializationHandlerFactory handlerFactory) throws MarshalingException {
		if (handlers == null) {
			try {
				handlers = handlerFactory.createSortedSerializationHandlers(beanClass);
			} catch (InstantiationException e) {
				throw new MarshalingException("Unable to create field handlers", e);
			}
		}
	}
	
	public String[] getTitles(T bean) throws MarshalingException {
		initHandlers(beanClass, handlerFactory);
		
		List<String> titles = new ArrayList<String>();
		
		for (SerializationHandler handler: handlers) {
			titles.addAll(handler.getTitles(bean));
		}
		
		return titles.toArray(new String[titles.size()]);
	}
	
	public Map<String, String> toBeanMap(T bean) throws MarshalingException {
		initHandlers(beanClass, handlerFactory);
		
		Map<String, String> map = new HashMap<String, String>();
		
		for (SerializationHandler handler: handlers) {
			map.putAll(handler.marshal(bean));
		}
		
		return map;
	}
}
