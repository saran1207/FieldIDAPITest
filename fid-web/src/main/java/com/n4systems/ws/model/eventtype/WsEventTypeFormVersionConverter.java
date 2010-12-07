package com.n4systems.ws.model.eventtype;

import com.n4systems.model.eventtype.EventTypeFormVersion;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeFormVersionConverter extends WsModelConverter<EventTypeFormVersion, WsEventTypeFormVersion> {

	@Override
	public WsEventTypeFormVersion fromModel(EventTypeFormVersion model) {
		WsEventTypeFormVersion wsModel = new WsEventTypeFormVersion();
		wsModel.setId(model.getId());
		wsModel.setVersion(model.getVersion());
		return wsModel;
	}

}
