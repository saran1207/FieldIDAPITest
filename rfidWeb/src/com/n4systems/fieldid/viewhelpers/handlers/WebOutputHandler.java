package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.search.CustomizableSearchAction;
import com.n4systems.util.views.ExcelOutputHandler;

/**
 * A converter for TableView based output cells.
 * Used in {@link CustomizableSearchAction} to convert cells from the
 * TableView into the appropriate output fields. <p/>
 * @see CustomizableSearchAction
 * @see CellHandlerFactory
 * @see DefaultHandler
 */
public abstract class WebOutputHandler implements ExcelOutputHandler {
	
	protected final AbstractAction action;
	
	protected WebOutputHandler(AbstractAction action) {
		this.action = action;
	}
	
	public abstract String handleWeb(Long entityId, Object value);
}
