package com.n4systems.api.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.api.validation.validators.AutoAttributeInputsValidator;
import com.n4systems.api.validation.validators.AutoAttributeOutputsValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.exporting.beanutils.MapSerializationHandler;
import com.n4systems.exporting.beanutils.SerializableField;


public class AutoAttributeView extends ExternalModelView {
	private static final long serialVersionUID = 1L;
	
	@SerializableField(title = "I:", order = 10, handler = MapSerializationHandler.class, validators = {NotNullValidator.class, AutoAttributeInputsValidator.class})
	private Map<String, String> inputs = new LinkedHashMap<String, String>();

	@SerializableField(title = "O:", order = 20, handler = MapSerializationHandler.class, validators = {NotNullValidator.class, AutoAttributeOutputsValidator.class})
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
