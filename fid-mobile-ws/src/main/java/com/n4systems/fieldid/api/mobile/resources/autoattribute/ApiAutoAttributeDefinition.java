package com.n4systems.fieldid.api.mobile.resources.autoattribute;

import com.n4systems.fieldid.api.mobile.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.api.mobile.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiAutoAttributeDefinition extends ApiReadonlyModel {
	private long criteriaId;
	private List<Long> inputs = new ArrayList<Long>();
	private List<ApiAttributeValue> outputs = new ArrayList<ApiAttributeValue>();
	
	public long getCriteriaId() {
		return criteriaId;
	}
	
	public void setCriteriaId(long criteriaId) {
		this.criteriaId = criteriaId;
	}
	
	public List<Long> getInputs() {
		return inputs;
	}

	public void setInputs(List<Long> inputs) {
		this.inputs = inputs;
	}	

	public List<ApiAttributeValue> getOutputs() {
		return outputs;
	}
	
	public void setOutputs(List<ApiAttributeValue> outputs) {
		this.outputs = outputs;
	}
}
