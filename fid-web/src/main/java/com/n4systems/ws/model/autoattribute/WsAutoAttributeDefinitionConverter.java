package com.n4systems.ws.model.autoattribute;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.assettype.WsInfoOption;
import com.n4systems.ws.model.assettype.WsInfoOptionConverter;
import rfid.ejb.entity.InfoOptionBean;

public class WsAutoAttributeDefinitionConverter extends WsModelConverter<AutoAttributeDefinition, WsAutoAttributeDefinition> {
	private final WsModelConverter<InfoOptionBean, WsInfoOption> infoOptionConverter;
	
	public WsAutoAttributeDefinitionConverter() {
		this(new WsInfoOptionConverter());
	}
	
	protected WsAutoAttributeDefinitionConverter(WsModelConverter<InfoOptionBean, WsInfoOption> infoOptionConverter) {
		this.infoOptionConverter = infoOptionConverter;
	}
	
	@Override
	public WsAutoAttributeDefinition fromModel(AutoAttributeDefinition model) {
		WsAutoAttributeDefinition wsModel = new WsAutoAttributeDefinition();
		wsModel.setId(model.getId());
		wsModel.setCriteriaId(model.getCriteria().getId());
		
		for (InfoOptionBean option: model.getInputs()) {
			wsModel.getInputs().add(option.getUniqueID());
		}
		
		wsModel.setOutputs(infoOptionConverter.fromModels(model.getOutputs()));
		return wsModel;
	}

}
