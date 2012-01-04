package com.n4systems.fieldid.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.injection.Injector;

import java.lang.reflect.Field;

public class ComponentTestInjector {

	private InjectionTestConfig injectConfig;
	
	private ComponentTestInjector(){
		this.injectConfig = InjectionTestConfig.make();			
	}
	
	public ComponentTestInjector wire(String fieldName, Object fieldValue) {
		injectConfig.wire(fieldName, fieldValue);
		return this;
	}
	
	public void inject(Component component) {
        injectConfig.inject(component);
	}

	public static ComponentTestInjector make() {
		ComponentTestInjector injector = new ComponentTestInjector();
		return injector;
	}

	public void inject(Application application) {
		Field[] declaredFields = application.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field appField = declaredFields[i];
			if (injectConfig.getFieldValueFactory().supportsField(appField)) {
				Object fieldValue = injectConfig.getFieldValueFactory().getFieldValue(appField, application);
				
				try {
					appField.set(application, fieldValue);
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				} 
			}
		}
	}

    public Injector getInjector() {
        return injectConfig;
    }

}
