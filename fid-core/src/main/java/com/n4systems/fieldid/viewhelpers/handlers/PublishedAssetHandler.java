package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;

public class PublishedAssetHandler extends WebOutputHandler {

	public PublishedAssetHandler(TableGenerationContext action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		return PublishedState.resolvePublishedState((Boolean)value).getPastTenseString();
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}

}
