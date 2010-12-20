package com.n4systems.ws.model.unitofmeasure;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.ws.model.WsModelConverter;

public class WsUnitOfMeasureConverter extends WsModelConverter<UnitOfMeasure, WsUnitOfMeasure> {

	@Override
	public WsUnitOfMeasure fromModel(UnitOfMeasure model) {
		WsUnitOfMeasure wsModel = new WsUnitOfMeasure();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		wsModel.setShortName(model.getShortName());
		wsModel.setType(model.getType());
		wsModel.setSelectable(model.isSelectable());
		
		if (model.getChild() != null) {
			wsModel.setChild(fromModel(model.getChild()));
		}
		
		return wsModel;
	}

}
