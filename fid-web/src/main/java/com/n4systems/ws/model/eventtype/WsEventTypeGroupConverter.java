package com.n4systems.ws.model.eventtype;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeGroupConverter extends WsModelConverter<EventTypeGroup, WsEventTypeGroup> {

	@Override
	public WsEventTypeGroup fromModel(EventTypeGroup model) {
		WsEventTypeGroup wsModel = new WsEventTypeGroup();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		return wsModel;
	}

}
