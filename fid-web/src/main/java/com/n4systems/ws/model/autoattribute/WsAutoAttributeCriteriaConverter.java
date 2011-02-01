package com.n4systems.ws.model.autoattribute;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.ws.model.WsModelConverter;

public class WsAutoAttributeCriteriaConverter extends WsModelConverter<AutoAttributeCriteria, WsAutoAttributeCriteria> {
	
	@Override
	public WsAutoAttributeCriteria fromModel(AutoAttributeCriteria model) {
		WsAutoAttributeCriteria wsModel = new WsAutoAttributeCriteria();
		wsModel.setId(model.getId());
		wsModel.setAssetTypeId(model.getAssetType().getId());
		
		for (InfoFieldBean field : model.getInputs()) {
			wsModel.getInputs().add(field.getUniqueID());
		}
		return wsModel;
	}
}
