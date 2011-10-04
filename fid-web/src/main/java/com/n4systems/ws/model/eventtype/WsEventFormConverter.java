package com.n4systems.ws.model.eventtype;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventFormConverter extends WsModelConverter<EventForm, WsEventForm> {
	private final WsModelConverter<CriteriaSection, WsCriteriaSection> sectionConverter;
	
	public WsEventFormConverter() {
		this(new WsCriteriaSectionConverter());
	}
	
	protected WsEventFormConverter(WsModelConverter<CriteriaSection, WsCriteriaSection> sectionConverter) {
		this.sectionConverter = sectionConverter;
	}
	
	@Override
	public WsEventForm fromModel(EventForm model) {
		WsEventForm wsModel = new WsEventForm();
		wsModel.setId(model.getId());
		wsModel.setSections(sectionConverter.fromModels(model.getSections()));
		wsModel.setUseScoreForResult(model.isUseScoreForResult());
		return wsModel;
	}
}