package com.n4systems.ws.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class WsModelConverter<M, W> {
	public abstract W fromModel(M model);

	public List<W> fromModels(Collection<M> models) {
		List<W> wsModels = new ArrayList<W>();
		for (M model: models) {
			wsModels.add(fromModel(model));
		}
		return wsModels;
	}
	
}
