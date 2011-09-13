package com.n4systems.ws.model.eventtype;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaType;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.NumberFieldCriteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SignatureCriteria;
import com.n4systems.model.State;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;
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
		if (model.getCriteriaType() == CriteriaType.ONE_CLICK) {
			wsModel = convertOneClickCriteria((OneClickCriteria)model);
		} else if (model.getCriteriaType() == CriteriaType.TEXT_FIELD) {
			wsModel = convertTextFieldCriteria((TextFieldCriteria)model);
		} else if (model.getCriteriaType() == CriteriaType.SELECT) {
			wsModel = convertSelectCriteria((SelectCriteria)model);
		} else if (model.getCriteriaType() == CriteriaType.UNIT_OF_MEASURE) {
			wsModel = convertUnitOfMeasureCriteria((UnitOfMeasureCriteria)model);
		} else if (model.getCriteriaType() == CriteriaType.COMBO_BOX) {
			wsModel = convertComboBoxCriteria((ComboBoxCriteria)model);
		} else if (model.getCriteriaType() == CriteriaType.SIGNATURE) {
			wsModel = convertSignatureCriteria((SignatureCriteria)model);
		} else if(model.getCriteriaType() == CriteriaType.DATE_FIELD) {
			wsModel = convertDateFieldCriteria((DateFieldCriteria)model);
		} else if(model.getCriteriaType() == CriteriaType.NUMBER_FIELD) {
			wsModel = convertNumberFieldCriteria((NumberFieldCriteria)model);
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
	
	private WsSignatureCriteria convertSignatureCriteria(SignatureCriteria model) {
		return new WsSignatureCriteria();
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
		WsSelectCriteria wsModel = new WsSelectCriteria();
		wsModel.setOptions(model.getOptions());
		return wsModel;
	}
	
	private WsUnitOfMeasureCriteria convertUnitOfMeasureCriteria(UnitOfMeasureCriteria model) {
		WsUnitOfMeasureCriteria wsModel = new WsUnitOfMeasureCriteria();
		wsModel.setPrimaryUnitId(model.getPrimaryUnit().getId());
		if (model.getSecondaryUnit() != null) {
			wsModel.setSecondaryUnitId(model.getSecondaryUnit().getId());
		}
		return wsModel;
	}
	
	private WsComboBoxCriteria convertComboBoxCriteria(ComboBoxCriteria model) {
		WsComboBoxCriteria wsModel = new WsComboBoxCriteria();
		wsModel.setOptions(model.getOptions());
		return wsModel;
	}
	
	private WsCriteria convertDateFieldCriteria(DateFieldCriteria model) {
		WsDateFieldCriteria wsModel = new WsDateFieldCriteria();
		wsModel.setIncludeTime(model.isIncludeTime());
		return wsModel;
	}
	
	private WsCriteria convertNumberFieldCriteria(NumberFieldCriteria model) {
		WsNumberFieldCriteria wsModel = new WsNumberFieldCriteria();
		wsModel.setDecimalPlaces(model.getDecimalPlaces());
		return wsModel;
	}
}
