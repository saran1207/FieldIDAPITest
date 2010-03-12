package com.n4systems.api.conversion.autoattribute;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.infooption.InfoOptionMapConverter;

public class AutoAttributeToViewConverter implements ModelToViewConverter<AutoAttributeDefinition, AutoAttributeView> {
	private final InfoOptionMapConverter optionConverter;

	public AutoAttributeToViewConverter() {
		this(new InfoOptionMapConverter());
	}
	
	public AutoAttributeToViewConverter(InfoOptionMapConverter optionConverter) {
		this.optionConverter = optionConverter;
	}
	
	@Override
	public AutoAttributeView toView(AutoAttributeDefinition model) throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().putAll(optionConverter.toMap(model.getInputs()));
		view.getOutputs().putAll(optionConverter.toMap(model.getOutputs()));
		return view;
	}
}
