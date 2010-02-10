package com.n4systems.exporting.beanutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

import com.n4systems.util.reflection.Reflector;


public class SerializationHandlerFactory {
	private static final long serialVersionUID = 1L;
	private static final Class<ExportField> EXPORT_FIELD = ExportField.class;

	public SerializationHandlerFactory() {}
	
	public SerializationHandler[] createSortedSerializationHandlers(Class<?> beanClass) throws InstantiationException {
		Field[] sortedFields = getSortedExportFields(beanClass);
		
		SerializationHandler[] handlers = new SerializationHandler[sortedFields.length];
		
		for (int i = 0; i < sortedFields.length; i++) {
			handlers[i] = createSerializationHandler(sortedFields[i]);
		}
		
		return handlers;
	}
	
	public SerializationHandler createSerializationHandler(Field field) throws InstantiationException {
		ExportField exportField = field.getAnnotation(ExportField.class);

		if (exportField == null) {
			return null;
		}
		
		SerializationHandler handler;
		Class<?> handlerClass = exportField.handler();
		try {
			Constructor<?> constructor = handlerClass.getConstructor(Field.class);

			handler = (SerializationHandler)constructor.newInstance(field);
			
		} catch (InstantiationException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			throw new InstantiationException("Could not instantiate [" + handlerClass + "], SerializationHandlers must have a constructor accepting the type Field");
		} catch (Exception e) {
			throw new InstantiationException("Could not instantiate [" + handlerClass + "]");
		}
		
		return handler;
	}
	
	private Field[] getSortedExportFields(Class<?> clazz) {
		// Grab all the export fields from this class and sort them be their order
		Field[] fields = Reflector.findAllFieldsWithAnnotation(clazz, EXPORT_FIELD);
		
		Arrays.sort(fields, new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				int order1 = f1.getAnnotation(EXPORT_FIELD).order();
				int order2 = f2.getAnnotation(EXPORT_FIELD).order();
				
				return (order1 == order2) ? 0 : ((order1 > order2) ? 1 : -1);
			}
		});
		
		return fields;
	}
}
