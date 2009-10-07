package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.product.PublishedState;

public class PublishedProductHandler implements OutputHandler {

	public String handle(AbstractAction action, Long entityId, Object value) {
		return PublishedState.resolvePublishedState((Boolean)value).getPastTenseLabel();
	}

	public boolean isLabel() {
		return true;
	}

}
