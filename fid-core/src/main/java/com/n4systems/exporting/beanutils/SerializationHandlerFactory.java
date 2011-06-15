package com.n4systems.exporting.beanutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.n4systems.util.reflection.Reflector;


public class SerializationHandlerFactory {
	private static final long serialVersionUID = 1L;
	private static final Class<SerializableField> EXPORT_FIELD = SerializableField.class;

	private boolean skipImportOnlyFields = false;
	
	public SerializationHandlerFactory() {}

	// TODO DD : have static make method for these objects. e.g. makeExportingSerializationFactory();
	public SerializationHandlerFactory(boolean skipImportOnlyFields) {
		this.skipImportOnlyFields = skipImportOnlyFields;	// if true, skip fields that are marked importOnly 
	}
			
	public SerializationHandler[] createSortedSerializationHandlers(Class<?> beanClass) throws InstantiationException {
		List<Field> sortedFields = getSortedExportFields(beanClass);
		
		SerializationHandler[] handlers = new SerializationHandler[sortedFields.size()];

		int i = 0;
		for (Field sortedField:sortedFields) {
			handlers[i] = createSerializationHandler(sortedField);
			i++;
		}

		return handlers;
	}
	
	public SerializationHandler createSerializationHandler(Field field) throws InstantiationException {
		SerializableField exportField = field.getAnnotation(SerializableField.class);

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
	
	private List<Field> getSortedExportFields(Class<?> clazz) {		
		// Grab all the export fields from this class and sort them be their order
		Field[] fields = Reflector.findAllFields(clazz);
		
		Predicate<Field> exportFieldPredicate = new Predicate<Field>() {
			@Override
			public boolean apply(Field field) {
				SerializableField annotation = field.getAnnotation(SerializableField.class);						
				if (annotation != null) { 
					return annotation.importOnly()==false || skipImportOnlyFields==false;
				}
				return false;
			}			
		};
		Collection<Field> exportedFields = Collections2.filter(Arrays.asList(fields), exportFieldPredicate);

		Comparator<Field> orderComparator = new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				int order1 = f1.getAnnotation(EXPORT_FIELD).order();
				int order2 = f2.getAnnotation(EXPORT_FIELD).order();				
				return (order1 == order2) ? 0 : ((order1 > order2) ? 1 : -1);
			}
		};
		
		return Ordering.from(orderComparator).sortedCopy(exportedFields);		
	}
}
