package com.n4systems.exporting.beanutils;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExportMapMarshaller<T> {
	private final Class<T> beanClass;
	private final SerializationHandlerFactory handlerFactory;
	
	// initialize the handlers lazily so we don't have to throw
	// exceptions out of the constructor
	private SerializationHandler<?>[] handlers;
	
	public ExportMapMarshaller(Class<T> beanClass) {
		this(beanClass, new SerializationHandlerFactory());
	}
	
	public ExportMapMarshaller(Class<T> beanClass, SerializationHandlerFactory handlerFactory) {
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
	
	public Map<String, Object> toBeanMap(T bean) throws MarshalingException {
		initHandlers(beanClass, handlerFactory);
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for (SerializationHandler<?> handler: handlers) {
			map.putAll(handler.marshal(bean));
		}
		
		return map;
	}
}
