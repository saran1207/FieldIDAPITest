package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.google.common.base.CaseFormat;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;

@SuppressWarnings("serial")
public class WidgetFactory implements Serializable {
	
	private static final Logger logger = Logger.getLogger(WidgetFactory.class);
	
	private Map<WidgetType, Class<? extends Widget>> config = new HashMap<WidgetType, Class<? extends Widget>>();


	public Widget createWidget(final WidgetDefinition widgetDefinition) {
		Widget widget = new Widget("widget", new PropertyModel<String>(widgetDefinition, "name"));
		final WidgetType widgetType = widgetDefinition.getWidgetType();
		profile("start ",widgetType);
		widget.addContent(new AjaxLazyLoadPanel(widget.getContentId()) {					
			@Override public Component getLazyLoadComponent(String markupId) {				
				Panel panel = createPanel(widgetType, markupId);
				profile("start ",widgetType);
				return panel;
			}	
		});
		return widget;
	}

	private Panel createPanel(WidgetType type, String id) {
		Class<? extends Widget> clazz = getWidgetPanelClass(type); 		
		Constructor<? extends Widget> constructor;
		try {
			constructor = clazz.getDeclaredConstructor(String.class);
			return constructor.newInstance(id);
		} catch (Exception e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " for the widget type " + type + " can not be created/instantiated");			
		}
	}		
	
	@SuppressWarnings("unchecked")
	private Class<? extends Widget> getWidgetPanelClass(WidgetType type) {
		Class<? extends Widget> clazz = config.get(type);
		if (clazz!=null) {
			return clazz;		// found it in config!
		}
		// otherwise lets make a best guess at it.
		String pkg = "com.n4systems.fieldid.wicket.components.dashboard.widgets.";
		String guessClassName = pkg + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, type.toString()) + "Panel";
		try {
			return (Class<? extends Widget>) Class.forName(guessClassName);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " for the widget type " + type + " MUST have a single string argument constructor.   e.g.  FooPanel(String id);");			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("can't find class to construct widget of type " + type + ".  \n Either add it to configuration or have the widgetType and widget's panel follow convention.   (e.g. type=FOO_BAR, panel=FooBarPanel)");
		}
	}

	public void configure(Map<WidgetType, Class<? extends Widget>> config) {
		this.config = config;
	}
	
	private void profile(String action, WidgetType widgetType) {
		// FIXME DD : what level should this be to most useful??
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		logger.warn("Widget : " + widgetType + " " + action + " at " + sdf.format(new Date())); 
	}

	

}
