package com.n4systems.ws.model.eventtype;

import com.n4systems.model.State;
import com.n4systems.ws.model.WsModelConverter;

public class WsStateConverter extends WsModelConverter<State, WsState> {

	@Override
	public WsState fromModel(State model) {
		WsState wsModel = new WsState();
		wsModel.setId(model.getId());
		wsModel.setDisplayText(model.getDisplayText());
		wsModel.setButtonName(model.getButtonName());
		wsModel.setStatus(WsStatus.convert(model.getStatus()));
		return wsModel;
	}
	
}
