package com.n4systems.api.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.api.validation.validators.AutoAttributeInputsValidator;
import com.n4systems.api.validation.validators.AutoAttributeOutputsValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.exporting.beanutils.AutoAttributeMapSerializationHandler;
import com.n4systems.exporting.beanutils.ExportField;


public class AutoAttributeView extends ExternalModelView {
	private static final long serialVersionUID = 1L;
	
	@ExportField(title = "I:", order = 10, handler = AutoAttributeMapSerializationHandler.class, validators = {NotNullValidator.class, AutoAttributeInputsValidator.class})
	private Map<String, String> inputs = new LinkedHashMap<String, String>();

	@ExportField(title = "O:", order = 20, handler = AutoAttributeMapSerializationHandler.class, validators = {NotNullValidator.class, AutoAttributeOutputsValidator.class})
	private Map<String, String> outputs = new LinkedHashMap<String, String>();
	
	public AutoAttributeView() {}

	public Map<String, String> getInputs() {
		return inputs;
	}

	public void setInputs(Map<String, String> inputs) {
		this.inputs = inputs;
	}

	public Map<String, String> getOutputs() {
		return outputs;
	}

	public void setOutputs(Map<String, String> outputs) {
		this.outputs = outputs;
	}

	@Override
	public String getGlobalId() {
		// Auto Attributes never get updated
		return null;
	}

	@Override
	public void setGlobalId(String globalId) {}
}
