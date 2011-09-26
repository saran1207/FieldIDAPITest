package com.n4systems.exporting.beanutils;

import java.util.HashMap;
import java.util.Map;


public class ExportMapUnmarshaler<T> {
	private final Class<T> beanClass;
	private final SerializationHandlerFactory handlerFactory;
	private final String[] titles;
	
	// initialize the handlers lazily so we don't have to throw
	// exceptions out of the constructor
	private Map<String, SerializationHandler> titleHandlerMap;
	
	public ExportMapUnmarshaler(Class<T> beanClass, String[] titles) {
		this(beanClass, titles, new SerializationHandlerFactory());
	}
	
	public ExportMapUnmarshaler(Class<T> beanClass, String[] titles, SerializationHandlerFactory handlerFactory) {
		this.beanClass = beanClass;
		this.titles = titles;
		this.handlerFactory = handlerFactory;
	}

	private void buildTitleFieldMap() throws MarshalingException {
		if (titleHandlerMap != null) {
			return;
		}
		
		try {
			SerializationHandler[] handlers = handlerFactory.createSortedSerializationHandlers(beanClass);
			titleHandlerMap = new HashMap<String, SerializationHandler>();
			
			// for each title, we will ask each registered serialization handler, if this field
			// belongs to it.
			for (String title: titles) {
				for (SerializationHandler handler: handlers) {
					if (handler.handlesField(title)) {
						titleHandlerMap.put(title, handler);
						break;
					}
				}
				
				// we should check if no handler was registered for this title.  That that case
				// we need to throw something
				if (!titleHandlerMap.containsKey(title)) {
					throw new InvalidTitleException(title);
				}
			}
		} catch (InstantiationException e) {
			throw new MarshalingException("Unable to create field handlers", e);
		}
	}
	
	public T toBean(Map<String, Object> row) throws MarshalingException {
		buildTitleFieldMap();
		
		T bean = newBean();
		
		Object value;
		SerializationHandler<?> handler;
		for (String title: titleHandlerMap.keySet()) {
			value = row.get(title);
			handler = titleHandlerMap.get(title);
			
			handler.unmarshal(bean, title, value);
		}

        for (SerializationHandler serializationHandler : titleHandlerMap.values()) {
            serializationHandler.resetState();
        }

		return bean;
	}
	
	private T newBean() throws MarshalingException {
		try {
			return beanClass.newInstance();
		} catch (Exception e) {
			throw new MarshalingException("Could not instantiate [" + beanClass.getName() + "]");
		}
	}
}
