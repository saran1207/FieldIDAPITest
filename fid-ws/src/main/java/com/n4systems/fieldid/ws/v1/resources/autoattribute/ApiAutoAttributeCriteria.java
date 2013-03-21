package com.n4systems.fieldid.ws.v1.resources.autoattribute;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiAutoAttributeCriteria extends ApiReadonlyModel {
	private long assetTypeId;
	private List<Long> inputs = new ArrayList<Long>();
	
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
}
