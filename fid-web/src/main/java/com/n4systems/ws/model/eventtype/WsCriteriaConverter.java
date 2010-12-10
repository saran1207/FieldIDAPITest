package com.n4systems.ws.model.eventtype;

import com.n4systems.model.Criteria;
import com.n4systems.model.State;
import com.n4systems.ws.model.WsModelConverter;

public class WsCriteriaConverter extends WsModelConverter<Criteria, WsCriteria> {
	private final WsModelConverter<State, WsState> stateConverter;
	
	public WsCriteriaConverter() {
		this(new WsStateConverter());
	}
	
	protected WsCriteriaConverter(WsModelConverter<State, WsState> stateConverter) {
		this.stateConverter = stateConverter;
	}
	
	@Override
	public WsCriteria fromModel(Criteria model) {
		WsCriteria wsModel = new WsCriteria();
		wsModel.setId(model.getId());
		wsModel.setDisplayText(model.getDisplayText());
		wsModel.setPrincipal(model.isPrincipal());
		wsModel.setRecommendations(model.getRecommendations());
		wsModel.setDeficiencies(model.getDeficiencies());
//		wsModel.setStates(stateConverter.fromModels(model.getStates().getStates()));
		return wsModel;
	}

}
