package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.search.CustomizableSearchAction;

/**
 * An interface defining a converter for TableView based output cells.
 * Used in {@link CustomizableSearchAction} to convert cells from the
 * TableView into the appropriate output fields. <p/>
 * Handlers may optionally define a <code>public void setDateFormat(String dateFormat)</code>
 * method which allows for date format injection.
 * @see CustomizableSearchAction
 * @see CellHandlerFactory
 * @see DefaultHandler
 */
public interface OutputHandler {
	/**
	 * Converts value 
	 * @param entityId
	 * @param value
	 * @return			A display
	 */
	public String handle(AbstractAction action, Long entityId, Object value);
	
	public boolean isLabel();
		
}
