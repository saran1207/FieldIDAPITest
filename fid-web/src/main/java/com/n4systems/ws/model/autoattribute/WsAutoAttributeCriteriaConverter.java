package com.n4systems.ws.model.autoattribute;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.ws.model.WsModelConverter;

public class WsAutoAttributeCriteriaConverter extends WsModelConverter<AutoAttributeCriteria, WsAutoAttributeCriteria> {
	private final WsModelConverter<AutoAttributeDefinition, WsAutoAttributeDefinition> definitionConverter;
	
	public WsAutoAttributeCriteriaConverter() {
		this(new WsAutoAttributeDefinitionConverter());
	}
	
	protected WsAutoAttributeCriteriaConverter(WsModelConverter<AutoAttributeDefinition, WsAutoAttributeDefinition> definitionConverter) {
		this.definitionConverter = definitionConverter;
	}
	
	@Override
	public WsAutoAttributeCriteria fromModel(AutoAttributeCriteria model) {
		WsAutoAttributeCriteria wsModel = new WsAutoAttributeCriteria();
		wsModel.setAssetTypeId(model.getAssetType().getId());
		
		for (InfoFieldBean field : model.getInputs()) {
			wsModel.getInputs().add(field.getUniqueID());
		}

		wsModel.setDefinitions(definitionConverter.fromModels(model.getDefinitions()));
		return wsModel;
	}
}
