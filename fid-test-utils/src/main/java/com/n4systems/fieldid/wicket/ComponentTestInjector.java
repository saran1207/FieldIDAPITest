package com.n4systems.fieldid.wicket;

import java.lang.reflect.Field;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.injection.web.InjectorHolder;

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
		InjectorHolder.setInjector(injectConfig);
		InjectorHolder.getInjector().inject(component);
	}

	public static ComponentTestInjector make() {
		ComponentTestInjector injector = new ComponentTestInjector();
		return injector;
	}

	public void inject(Application application) {
		Field[] declaredFields = application.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field appField = declaredFields[i];
			if(injectConfig.getFieldValueFactory().supportsField(appField)) {
				Object fieldValue = injectConfig.getFieldValueFactory().getFieldValue(appField, application);
				
				try {
					appField.set(application, fieldValue);
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				} 
			}
		}
	}

}
