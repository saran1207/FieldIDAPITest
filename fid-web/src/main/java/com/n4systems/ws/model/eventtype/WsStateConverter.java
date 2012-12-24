package com.n4systems.ws.model.eventtype;

import com.n4systems.model.Button;
import com.n4systems.ws.model.WsModelConverter;

public class WsStateConverter extends WsModelConverter<Button, WsState> {

	@Override
	public WsState fromModel(Button model) {
		WsState wsModel = new WsState();
		wsModel.setId(model.getId());
		wsModel.setDisplayText(model.getDisplayText());
		wsModel.setButtonName(model.getButtonName());
		wsModel.setStatus(WsStatus.convert(model.getEventResult()));
		wsModel.setActive(!model.isRetired());
		return wsModel;
	}
	
}
