package com.n4systems.ws.model.eventtype;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.ws.model.WsModelConverter;

public class WsCriteriaSectionConverter extends WsModelConverter<CriteriaSection, WsCriteriaSection> {
	private final WsModelConverter<Criteria, WsCriteria> criteriaConverter;
	
	public WsCriteriaSectionConverter() {
		this(new WsCriteriaConverter());
	}
	
	protected WsCriteriaSectionConverter(WsModelConverter<Criteria, WsCriteria> criteriaConverter) {
		this.criteriaConverter = criteriaConverter;
	}
	
	@Override
	public WsCriteriaSection fromModel(CriteriaSection model) {
		WsCriteriaSection wsModel = new WsCriteriaSection();
		wsModel.setId(model.getId());
		wsModel.setTitle(model.getTitle());
		wsModel.setCriteria(criteriaConverter.fromModels(model.getCriteria()));
		return wsModel;
	}

}
