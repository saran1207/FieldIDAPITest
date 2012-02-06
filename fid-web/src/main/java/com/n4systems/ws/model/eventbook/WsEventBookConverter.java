package com.n4systems.ws.model.eventbook;

import com.n4systems.model.EventBook;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.org.WsOrg;
import com.n4systems.ws.model.org.WsOrgConverter;

public class WsEventBookConverter extends WsModelConverter<EventBook, WsEventBook> {
	private final WsModelConverter<BaseOrg, WsOrg> orgConverter;

	public WsEventBookConverter() {
		this (new WsOrgConverter());
	}
	
	protected WsEventBookConverter(WsModelConverter<BaseOrg, WsOrg> orgConverter) {
		this.orgConverter = orgConverter;
	}

	@Override
	public WsEventBook fromModel(EventBook model) {
		WsEventBook wsModel = new WsEventBook();
		wsModel.setId(model.getMobileId());
		wsModel.setName(model.getName());
		wsModel.setOpen(model.isOpen());
		wsModel.setOwner(orgConverter.fromModel(model.getOwner()));
		wsModel.setActive(model.isActive());
		return wsModel;
	}

}
