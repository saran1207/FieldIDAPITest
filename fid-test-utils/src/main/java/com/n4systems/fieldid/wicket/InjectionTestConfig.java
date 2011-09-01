package com.n4systems.fieldid.wicket;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.injection.ConfigurableInjector;
import org.apache.wicket.injection.IFieldValueFactory;

public class InjectionTestConfig extends ConfigurableInjector {

	private Map<String, Object> mappings = new HashMap<String, Object>();

	@Override
	protected IFieldValueFactory getFieldValueFactory() {
		return new IFieldValueFactory() {
			
			public boolean supportsField(Field field) {
				return mappings.containsKey(field.getName());
			}
			
			public Object getFieldValue(Field field, Object fieldOwner) {
				if(!field.isAccessible()) {
					field.setAccessible(true);
				}
				return mappings.get(field.getName()); 
			}
		};
	}
	
	public static InjectionTestConfig make() {
		return new InjectionTestConfig();
	}

	public InjectionTestConfig wire(String fieldName, Object value) {
		mappings.put(fieldName, value);
		return this;
	}
}

