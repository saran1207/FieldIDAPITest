package com.n4systems.fieldid.service.download;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.views.ExcelOutputHandler;

/**
 * A converter for TableView based output cells.
 * Used in {@link CustomizableSearchAction} to convert cells from the
 * TableView into the appropriate output fields. <p/>
 * @see CustomizableSearchAction
 * @see com.n4systems.fieldid.service.download.CellHandlerFactory
 * @see DefaultHandler
 */
public abstract class WebOutputHandler implements ExcelOutputHandler {
	
	protected final TableGenerationContext contextProvider;
	
	protected WebOutputHandler(TableGenerationContext contextProvider) {
		this.contextProvider = contextProvider;
	}
	
	public abstract String handleWeb(Long entityId, Object value);

    protected String getAbsoluteUrl() {
        String absoluteUrl = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://";
        absoluteUrl += contextProvider.getOwner().getTenant().getName() + ".";
        absoluteUrl += ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_DOMAIN);
        return absoluteUrl;
    }

}
