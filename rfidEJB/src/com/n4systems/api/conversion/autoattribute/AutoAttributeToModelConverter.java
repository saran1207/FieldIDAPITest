package com.n4systems.api.conversion.autoattribute;

import java.util.List;

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
		
		model.setTenant(criteria.getTenant());
		model.setCriteria(criteria);
		setupInputs(model, view, criteria);
		setupOutputs(model, view, criteria);
		
		return model;
	}

	private void setupOutputs(AutoAttributeDefinition model, AutoAttributeView view, AutoAttributeCriteria criteria) throws ConversionException {
		String optionValue;
		InfoOptionBean option;
		for (InfoFieldBean field: criteria.getOutputs()) {
			optionValue = view.getOutputs().get(field.getName());
			
			// output options may be null
			if (optionValue == null) {
				continue;
			}
			
			// try to resolve a static option first, then we'll create a dynamic one if it's not found.  
			// We do it this way to make sure the combo box static options resolve first
			option = findInfoOptionByName(optionValue, field.getInfoOptions());
			
			if (option == null) {
				if (!field.acceptsDyanmicInfoOption()) {
					// if we're not allowed to add dynamic options, and we couldn't resolve one, than it's an error
					throw new ConversionException(String.format("Static option [%s] not found for field [%d]", optionValue, field.getUniqueID()));
				} else {
					option = new InfoOptionBean();
					option.setInfoField(field);
					option.setName(optionValue);
					option.setStaticData(false);
				}
			}
			
			model.getOutputs().add(option);
		}
	}

	private void setupInputs(AutoAttributeDefinition model, AutoAttributeView view, AutoAttributeCriteria criteria) throws ConversionException {
		String optionValue;
		InfoOptionBean option;
		for (InfoFieldBean field: criteria.getInputs()) {
			optionValue = view.getInputs().get(field.getName());
			
			// we must have a value for every input
			if (optionValue == null) {
				throw new ConversionException("No Value found for input InfoField [" + field.getName() + "]");
			}
			
			option = findInfoOptionByName(optionValue, field.getInfoOptions());
			
			// we must also have a valid option
			if (option == null) {
				throw new ConversionException("No InfoOption found for input InfoField [" + field.getName() + "] and value [" + optionValue + "]");
			}
			
			model.getInputs().add(option);
		}
	}
	
	private InfoOptionBean findInfoOptionByName(String name, List<InfoOptionBean> options) {
		for (InfoOptionBean option: options) {
			if (name.equals(option.getName())) {
				return option;
			}
		}
		return null;
	}

	public AutoAttributeCriteria getCriteria() {
		return criteria;
	}

}
