package com.n4systems.ws.model.lastmod;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.ws.model.WsModelConverter;

public class WsLastModifiedConverter extends WsModelConverter<LastModified, WsLastModified> {

	@Override
	public WsLastModified fromModel(LastModified model) {
		WsLastModified wsModel = new WsLastModified();
		wsModel.setId(model.getId());
		wsModel.setModified(model.getModified());
		return wsModel;
	}

}
