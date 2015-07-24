package com.n4systems.fieldid.ws.v2.resources.setupdata.autoattribute;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiAutoAttribute extends ApiReadOnlyModel {
	private Long assetTypeId;
	private List<ApiAutoAttributeDefinition> defs = new ArrayList<>();

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public List<ApiAutoAttributeDefinition> getDefs() {
		return defs;
	}

	public void setDefs(List<ApiAutoAttributeDefinition> defs) {
		this.defs = defs;
	}

}
