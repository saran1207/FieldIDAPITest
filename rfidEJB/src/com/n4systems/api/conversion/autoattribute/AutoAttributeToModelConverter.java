package com.n4systems.api.conversion.autoattribute;

import java.util.List;
import java.util.Map;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;

public class AutoAttributeToModelConverter implements ViewToModelConverter<AutoAttributeDefinition, AutoAttributeView> {
	private final AutoAttributeCriteria criteria;
	
	public AutoAttributeToModelConverter(AutoAttributeCriteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public AutoAttributeDefinition toModel(AutoAttributeView view) throws ConversionException {
		AutoAttributeDefinition model = new AutoAttributeDefinition();
		
		model.setCriteria(criteria);
		setupInputs(model, view, criteria);
		setupOutputs(model, view, criteria);
		
		return model;
	}

	private void setupOutputs(AutoAttributeDefinition model, AutoAttributeView view, AutoAttributeCriteria criteria) throws ConversionException {
		InfoFieldBean field;
		InfoOptionBean option;
		for (Map.Entry<String, String> outputEntry: view.getOutputs().entrySet()) {
			if (outputEntry.getValue() == null) {
				// output options can be blank
				continue;
			}
			
			field = findInfoFieldByName(outputEntry.getKey(), criteria.getOutputs());
			
			// try to resolve it first, then we'll create one if it's not found.  We do it this way to 
			// make sure the combo box static options resolve first
			option = findInfoOptionByName(outputEntry.getValue(), field.getInfoOptions());
			
			if (option == null) {
				if (!field.acceptsDyanmicInfoOption()) {
					throw new ConversionException(String.format("Could not resolve static option [%s] on info field [%d]", outputEntry.getValue(), field.getUniqueID()));
				} else {
					option = new InfoOptionBean();
					option.setInfoField(field);
					option.setName(outputEntry.getValue());
					option.setStaticData(false);
				}
				
				model.getOutputs().add(option);
			}
		}
	}

	private void setupInputs(AutoAttributeDefinition model, AutoAttributeView view, AutoAttributeCriteria criteria) {
		InfoFieldBean field;
		InfoOptionBean option;
		for (Map.Entry<String, String> inputEntry: view.getInputs().entrySet()) {
			field = findInfoFieldByName(inputEntry.getKey(), criteria.getInputs());
			option = findInfoOptionByName(inputEntry.getValue(), field.getInfoOptions());
			model.getInputs().add(option);
		}
	}

	private InfoFieldBean findInfoFieldByName(String name, List<InfoFieldBean> fields) {
		for (InfoFieldBean field: fields) {
			if (name.equals(field.getName())) {
				return field;
			}
		}
		return null;
	}
	
	private InfoOptionBean findInfoOptionByName(String name, List<InfoOptionBean> options) {
		for (InfoOptionBean option: options) {
			if (name.equals(option.getName())) {
				return option;
			}
		}
		return null;
	}

}
