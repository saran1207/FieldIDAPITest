package com.n4systems.ws.model.assettype;


import com.n4systems.model.AssetStatus;
import com.n4systems.ws.model.WsModelConverter;

public class WsAssetStatusConverter extends WsModelConverter<AssetStatus, WsAssetStatus> {

	@Override
	public WsAssetStatus fromModel(AssetStatus model) {
		WsAssetStatus wsModel = new WsAssetStatus();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		return wsModel;
	}

}
