package com.n4systems.fieldid.viewhelpers.handlers;

import org.apache.log4j.Logger;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.search.ProductSearchAction;

/**
 * Factory for creating cell OutputHandlers.
 */
public class CellHandlerFactory {
	private Logger logger = Logger.getLogger(ProductSearchAction.class);

	private final AbstractAction action;
	private final WebOutputHandler defaultHandler;
	
	public CellHandlerFactory(AbstractAction action) {
		this.action = action;
		
		// we register the default handler here so that multiple invocations of getHandler will receive the same instance 
		this.defaultHandler = new DefaultHandler(action);
	}
	
	/**
	 * Instantates the OutputHandler for a given class name.  If className is null, 
	 * zero length or if the requested class could not be instantated, returns an
	 * instance of {@link DefaultHandler}.
	 * @param className		Classname for this handler or null to get the default handler.
	 * @return				The OutputHandler defined by className or an instance of {@link DefaultHandler}
	 */
	public WebOutputHandler getHandler(String className) {
		WebOutputHandler handler;
		if(className != null && className.length() != 0) {
			try {
				handler = (WebOutputHandler)Class.forName(className).getDeclaredConstructor(AbstractAction.class).newInstance(action);
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
