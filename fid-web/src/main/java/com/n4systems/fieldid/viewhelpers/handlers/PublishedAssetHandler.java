package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.asset.PublishedState;

public class PublishedAssetHandler extends WebOutputHandler {

	protected PublishedAssetHandler(AbstractAction action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		return action.getText(PublishedState.resolvePublishedState((Boolean)value).getPastTenseLabel());
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}

}
