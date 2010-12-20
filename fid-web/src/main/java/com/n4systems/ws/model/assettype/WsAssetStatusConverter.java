package com.n4systems.ws.model.assettype;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ws.model.WsModelConverter;

public class WsAssetStatusConverter extends WsModelConverter<AssetStatus, WsAssetStatus> {

	@Override
	public WsAssetStatus fromModel(AssetStatus model) {
		WsAssetStatus wsModel = new WsAssetStatus();
		wsModel.setId(model.getUniqueID());
		wsModel.setName(model.getName());
		return wsModel;
	}

}
