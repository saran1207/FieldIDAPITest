package com.n4systems.fieldid.viewhelpers.handlers;

import java.lang.reflect.Method;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.n4systems.fieldid.actions.search.ProductSearchAction;

/**
 * Factory for creating cell OutputHandlers.
 */
public class CellHandlerFactory {
	private Logger logger = Logger.getLogger(ProductSearchAction.class);
	
	private final String dateFormat;
	private final OutputHandler defaultHandler;
	private final String dateTimeFormat;
	private final TimeZone timeZone;
	
	public CellHandlerFactory(final String dateFormat, final String dateTimeFormat, final TimeZone timeZone) {
		this.dateFormat = dateFormat;
		this.dateTimeFormat = dateTimeFormat;
		this.timeZone = timeZone;
		
		// we register the default handler here so that multiple invocations of getHandler will receive the same instance 
		this.defaultHandler = new DefaultHandler(dateFormat);
	}
	
	/**
	 * Instantates the OutputHandler for a given class name.  If className is null, 
	 * zero length or if the requested class could not be instantated, returns an
	 * instance of {@link DefaultHandler}.  Also provides logic to inject the date format
	 * if a <code>public void setDateFormat(String dateFormat)</code> method exists on
	 * the handler class;
	 * @param className		Classname for this handler or null to get the default handler.
	 * @return				The OutputHandler defined by className or an instance of {@link DefaultHandler}
	 */
	public OutputHandler getHandler(String className) {
		OutputHandler handler;
		if(className != null && className.length() != 0) {
			try {
				handler = (OutputHandler)Class.forName(className).newInstance();

				// some handlers need access to the session date format.  If they have a 
				// setDateFormat(String) method, then we'll inject in the date format
				try {
					Method setFormatMethod = handler.getClass().getMethod("setDateFormat", String.class);
					setFormatMethod.invoke(handler, dateFormat);
				} catch(NoSuchMethodException nme) {
					// no problem here, just means this handler does not accept a date format
				}
				
				try {
					Method setFormatMethod = handler.getClass().getMethod("setDateTimeFormat", String.class);
					setFormatMethod.invoke(handler, dateTimeFormat);
					Method setTimeZoneMethod = handler.getClass().getMethod("setTimeZone", TimeZone.class);
					setTimeZoneMethod.invoke(handler, timeZone);
				} catch(NoSuchMethodException nme) {
					// no problem here, just means this handler does not accept a date time format
				}
				
				
				
			} catch(Exception e) {
				// if newInstance of the custom handler fails, use the default
				logger.error("Unable to register custom handler for class [" + className + "]", e);
				handler = defaultHandler;
			}
		} else {
			handler = defaultHandler;
		}
		
		return handler;
	}
}
