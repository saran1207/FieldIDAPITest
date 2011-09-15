package com.n4systems.ws.model.eventtype;

import com.n4systems.model.Score;
import com.n4systems.ws.model.WsModelConverter;

public class WsScoreConverter extends WsModelConverter<Score, WsScore> {

	@Override
	public WsScore fromModel(Score model) {
		WsScore wsModel = new WsScore();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		wsModel.setValue(model.getValue());
		wsModel.setNotApplicable(model.isNa());		
		return wsModel;
	}

}
