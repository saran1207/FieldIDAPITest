package com.n4systems.fieldid.ws.v2.resources.setupdata.autoattribute;

import com.n4systems.fieldid.ws.v2.resources.customerdata.asset.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiAutoAttributeDefinition extends ApiReadonlyModel {
	private long criteriaId;
	private List<Long> inputs = new ArrayList<Long>();
	private List<ApiAttributeValue> outputs = new ArrayList<>();
	
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
