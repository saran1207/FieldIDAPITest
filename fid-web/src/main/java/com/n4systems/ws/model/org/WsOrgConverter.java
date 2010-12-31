package com.n4systems.ws.model.org;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.ws.model.WsModelConverter;

public class WsOrgConverter extends WsModelConverter<BaseOrg, WsOrg> {

	@Override
	public WsOrg fromModel(BaseOrg model) {
		WsOrg wsModel = new WsOrg();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		wsModel.setActive(model.isActive());
		
		if (model.getSecondaryOrg() != null) 
			wsModel.setSecondaryId(model.getSecondaryOrg().getId());
		
		if (model.getCustomerOrg() != null)
			wsModel.setCustomerId(model.getCustomerOrg().getId());
		
		if (model.getDivisionOrg() != null)
			wsModel.setDivisionId(model.getDivisionOrg().getId());

		return wsModel;
	}

}
