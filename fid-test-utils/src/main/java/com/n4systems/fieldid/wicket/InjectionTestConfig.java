package com.n4systems.fieldid.wicket;

import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.Injector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class InjectionTestConfig extends Injector {

	private Map<String, Object> mappings = new HashMap<String, Object>();
    private IFieldValueFactory factory = createFieldValueFactory();

    @Override
    public void inject(Object object) {
        inject(object, factory);
    }

	public static InjectionTestConfig make() {
		return new InjectionTestConfig();
	}

	public InjectionTestConfig wire(String fieldName, Object value) {
		mappings.put(fieldName, value);
		return this;
	}

    public IFieldValueFactory getFieldValueFactory() {
        return factory;
    }

    private IFieldValueFactory createFieldValueFactory() {
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

}

