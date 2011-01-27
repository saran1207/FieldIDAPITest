package com.n4systems.ws.model.eventtype;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.State;
import com.n4systems.model.TextFieldCriteria;
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
		WsCriteria wsModel;
		if (model.isOneClickCriteria()) {
			wsModel = convertOneClickCriteria((OneClickCriteria)model);
		} else if (model.isTextFieldCriteria()) {
			wsModel = convertTextFieldCriteria((TextFieldCriteria)model);
		} else if (model.isSelectCriteria()) {
			wsModel = convertSelectCriteria((SelectCriteria)model);
		} else {
			throw new NotImplementedException("Conversion not implemented for Criteria type: " + model.getClass().getName());
		}
		convertBaseFields(model, wsModel);
		return wsModel;
	}
	
	private void convertBaseFields(Criteria model, WsCriteria wsModel) {
		wsModel.setId(model.getId());
		wsModel.setDisplayText(model.getDisplayText());
		wsModel.setRecommendations(model.getRecommendations());
		wsModel.setDeficiencies(model.getDeficiencies());
		wsModel.setRetired(model.isRetired());
	}
	
	private WsOneClickCriteria convertOneClickCriteria(OneClickCriteria model) {
		WsOneClickCriteria wsModel = new WsOneClickCriteria();
		wsModel.setStates(stateConverter.fromModels(model.getStates().getStates()));
        wsModel.setPrincipal(model.isPrincipal());
		return wsModel;
	}
	
	private WsTextFieldCriteria convertTextFieldCriteria(TextFieldCriteria model) {
		return new WsTextFieldCriteria();
	}

	private WsSelectCriteria convertSelectCriteria(SelectCriteria model) {
		return new WsSelectCriteria();
	}
}
