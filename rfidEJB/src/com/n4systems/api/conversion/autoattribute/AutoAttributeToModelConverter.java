package com.n4systems.api.conversion.autoattribute;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.infooption.InfoOptionConversionException;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.persistence.Transaction;

public class AutoAttributeToModelConverter implements ViewToModelConverter<AutoAttributeDefinition, AutoAttributeView> {
	private final AutoAttributeCriteria criteria;
	private final InfoOptionMapConverter optionConverter;
	
	public AutoAttributeToModelConverter(AutoAttributeCriteria criteria) {
		this(criteria, new InfoOptionMapConverter());
	}
	
	public AutoAttributeToModelConverter(AutoAttributeCriteria criteria, InfoOptionMapConverter infoOptionConverter) {
		this.criteria = criteria;
		this.optionConverter = infoOptionConverter;
	}
	
	@Override
	public AutoAttributeDefinition toModel(AutoAttributeView view, Transaction transaction) throws ConversionException {
		AutoAttributeDefinition model = new AutoAttributeDefinition();
		model.setTenant(criteria.getTenant());
		model.setCriteria(criteria);
		
		try {
			model.getInputs().addAll(optionConverter.convertAutoAttributeInputs(view.getInputs(), criteria));
			model.getOutputs().addAll(optionConverter.convertAutoAttributeOutputs(view.getOutputs(), criteria));
		} catch(InfoOptionConversionException e) {
			throw new ConversionException("Unable to convert info options", e);
		}
		
		return model;
	}

	public AutoAttributeCriteria getCriteria() {
		return criteria;
	}

}
