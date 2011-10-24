package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
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
		// FIXME DD : add remove button stuff here...
		final WidgetType widgetType = widgetDefinition.getWidgetType();
		widget.addContent(createPanel(widgetType, widget.getContentId()));
		return widget;
	}
	
	private Panel createPanel(WidgetType type, String id) {
		return createPanel(type,id,true);
	}
	
	private Panel createPanel(WidgetType type, String id, boolean checkForLazy) {
		Class<? extends Panel> clazz = getWidgetPanelClass(type); 		
		if (checkForLazy && ChartablePanel.class.isAssignableFrom(clazz)) {
			return createLazyPanel(type, id);
		} else { 
			return createPanel(clazz, id);
		}
	}		
	
	private Panel createPanel(Class<? extends Panel> clazz, String id) {
		try {
			Constructor<? extends Panel> constructor = clazz.getDeclaredConstructor(String.class);
			return constructor.newInstance(id);			 		
		} catch (Exception e) {
			logger.error(e);
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " can not be created/instantiated");			
		}
	}

	private Panel createLazyPanel(final WidgetType type, String id) {
		profile("start ",type.toString());		
		return new AjaxLazyLoadPanel(id) {					
			@Override public Component getLazyLoadComponent(String markupId) {				
				try {
					profile("stop ",type.toString());		
					return createPanel(type, markupId, false);
				} catch (Exception e) {
					logger.warn("can't create panel for type " + type, e);
					return null;
				}
			}
			@Override public Component getLoadingComponent(String markupId) {
				String img = "<img style='padding:10px 0 270px 10px' alt=\"Loading...\" src=\"" + RequestCycle.get().urlFor(AbstractDefaultAjaxBehavior.INDICATOR) + "\"/>";				
				return new Label(markupId, img).setEscapeModelStrings(false);
			}
		};
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Panel> getWidgetPanelClass(WidgetType type) {
		Class<? extends Panel> clazz = config.get(type);
		if (clazz!=null) {
			return clazz;		// found it in config!
		}
		// otherwise lets make a best guess at it.
		String pkg = "com.n4systems.fieldid.wicket.components.dashboard.widgets.";
		String guessClassName = pkg + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, type.toString()) + "Panel";
		try {
			return (Class<? extends Panel>) Class.forName(guessClassName);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("the panel class " + clazz.getSimpleName() + " for the widget type " + type + " MUST have a single string argument constructor.   e.g.  FooPanel(String id);");			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("can't find class to construct widget of type " + type + ".  \n Either add it to configuration or have the widgetType and widget's panel follow convention.   (e.g. type=FOO_BAR, panel=FooBarPanel)");
		}
	}
	
	public void configure(Map<WidgetType, Class<? extends Widget>> config) {
		this.config = config;
	}
	
	private void profile(String action, String panel) {
		// FIXME DD : what level should this be to most useful??
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ");
		logger.warn("Widget : " + panel + " " + action + " at " + sdf.format(new Date())); 
	}

	

}
