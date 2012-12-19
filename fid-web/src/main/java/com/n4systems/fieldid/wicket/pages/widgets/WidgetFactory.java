package com.n4systems.fieldid.wicket.pages.widgets;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.CaseFormat;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;

@SuppressWarnings("serial")
public class WidgetFactory implements Serializable {
	
	public static final String WIDGET_ID = "widget";

	private static final Logger logger = Logger.getLogger(WidgetFactory.class);
	
	private Map<WidgetType, Class<? extends Widget>> config = new HashMap<WidgetType, Class<? extends Widget>>();


	public Widget createWidget(final WidgetDefinition widgetDefinition) {
		return createWidget(widgetDefinition, WIDGET_ID, true);
	}		
	
	public Widget createWidget(final WidgetDefinition widgetDefinition, String id, boolean checkForLazy) {
		Class<? extends Widget> clazz = getWidgetClass(widgetDefinition.getWidgetType());
		return createWidget(clazz, widgetDefinition, id);
	}
	
	private Widget createWidget(Class<? extends Widget> clazz, WidgetDefinition widgetDefinition, String id) {		
		try {
			Constructor<? extends Widget> constructor = clazz.getDeclaredConstructor(String.class, WidgetDefinition.class);
			return constructor.newInstance(id, widgetDefinition);
		} catch (InvocationTargetException e) {
			logger.error(e.getTargetException());
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " can not be created/instantiated. " + e.getTargetException().getMessage(), e.getTargetException());
		} catch (IllegalArgumentException e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " failed because of illegal arguments " + e.getMessage());
		} catch (InstantiationException e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " failed due to instantiation exception " + e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " can not be created/instantiated due to illegal access " + e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " failed when attempting to create instance. " + e.getMessage());
		} 
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Widget> getWidgetClass(WidgetType type) {
		Class<? extends Widget> clazz = config.get(type);
		if (clazz!=null) {
			return clazz;		// found it in config!
		}
		// ...otherwise lets make a best guess at it.
		String pkg = "com.n4systems.fieldid.wicket.pages.widgets.";
		String guessClassName = pkg + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, type.toString()) + "Widget";
		try {
			return (Class<? extends Widget>) Class.forName(guessClassName);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("the panel class " + guessClassName + " for the widget type " + type + " MUST have a single string argument constructor.   e.g.  FooPanel(String id);");			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("can't find class to construct widget of type " + type + " with class " + guessClassName + ".  \n Either add it to configuration or have the widgetType and widget's panel follow convention.   (e.g. type=FOO_BAR, panel=FooBarPanel)");
		}
	}
	
	public void configure(Map<WidgetType, Class<? extends Widget>> config) {
		this.config = config;
	}
	

}
