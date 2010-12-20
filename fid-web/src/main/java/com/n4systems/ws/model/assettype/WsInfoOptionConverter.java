package com.n4systems.ws.model.assettype;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ws.model.WsModelConverter;

public class WsInfoOptionConverter extends WsModelConverter<InfoOptionBean, WsInfoOption> {

	@Override
	public WsInfoOption fromModel(InfoOptionBean model) {
		WsInfoOption wsModel = new WsInfoOption();
		wsModel.setId(model.getUniqueID());
		wsModel.setInfoFieldId(model.getInfoField().getUniqueID());
		wsModel.setName(model.getName());
		wsModel.setStaticData(model.isStaticData());
		return wsModel;
	}

}
