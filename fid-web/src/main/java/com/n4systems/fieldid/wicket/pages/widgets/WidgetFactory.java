package com.n4systems.fieldid.wicket.pages.widgets;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.CaseFormat;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;

@SuppressWarnings("serial")
public class WidgetFactory implements Serializable {
	
	private static final Logger logger = Logger.getLogger(WidgetFactory.class);
	
	private Map<WidgetType, Class<? extends Widget>> config = new HashMap<WidgetType, Class<? extends Widget>>();


	public Widget createWidget(final WidgetDefinition widgetDefinition) {
		return createWidget(widgetDefinition, "widget", true);
	}		
	
	public Widget createWidget(final WidgetDefinition widgetDefinition, String id, boolean checkForLazy) {
		Class<? extends Widget> clazz = getPanelClass(widgetDefinition.getWidgetType());
		return createPanel(clazz, widgetDefinition, id);
	}
	
	private Widget createPanel(Class<? extends Widget> clazz, WidgetDefinition widgetDefinition, String id) {		
		try {
			Constructor<? extends Widget> constructor = clazz.getDeclaredConstructor(String.class, WidgetDefinition.class);
			return constructor.newInstance(id, widgetDefinition);			 		
		} catch (Exception e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " can not be created/instantiated");			
		}
	}


	@SuppressWarnings("unchecked")
	private Class<? extends Widget> getPanelClass(WidgetType type) {
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
