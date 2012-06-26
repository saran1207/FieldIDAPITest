package com.n4systems.ws.model.eventtype;

import com.n4systems.model.EventStatus;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventStatusConverter extends WsModelConverter<EventStatus, WsEventStatus> {
	@Override
	public WsEventStatus fromModel(EventStatus model) {
		WsEventStatus wsModel = new WsEventStatus();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		wsModel.setActive(model.isActive());
		return wsModel;
	}
}
