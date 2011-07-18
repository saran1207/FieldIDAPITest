package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.asset.PublishedState;
import com.n4systems.fieldid.utils.WebContextProvider;

public class PublishedAssetHandler extends WebOutputHandler {

	protected PublishedAssetHandler(WebContextProvider action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		return contextProvider.getText(PublishedState.resolvePublishedState((Boolean)value).getPastTenseLabel());
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}

}
