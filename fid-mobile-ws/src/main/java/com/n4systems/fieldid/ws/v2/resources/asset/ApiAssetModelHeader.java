package com.n4systems.fieldid.ws.v2.resources.asset;

import com.n4systems.fieldid.ws.v2.resources.ApiSortedModelHeader;

import java.util.Date;

public class ApiAssetModelHeader extends ApiSortedModelHeader {
	private final String identifier;

	public ApiAssetModelHeader(String sid, Date modified, Object sortValue, String identifier) {
		super(sid, modified, sortValue);
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
