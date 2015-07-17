package com.n4systems.fieldid.ws.v2.resources.setupdata.autoattribute;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiAutoAttributeCriteria extends ApiReadonlyModel {
	private long assetTypeId;
	private List<Long> inputs = new ArrayList<>();
	private List<Long> outputs = new ArrayList<>();
	
	public long getAssetTypeId() {
		return assetTypeId;
	}
	
	public void setAssetTypeId(long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	
	public List<Long> getInputs() {
		return inputs;
	}

	public void setInputs(List<Long> inputs) {
		this.inputs = inputs;
	}
	
	public List<Long> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Long> outputs) {
		this.outputs = outputs;
	}	
}
