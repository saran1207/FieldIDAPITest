package com.n4systems.fieldid.ws.v1.resources.autoattribute;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

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
