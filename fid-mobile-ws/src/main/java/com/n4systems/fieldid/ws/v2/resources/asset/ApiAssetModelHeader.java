package com.n4systems.fieldid.ws.v2.resources.asset;

import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;

import java.util.Date;

public class ApiAssetModelHeader extends ApiModelHeader {
	private String identifier;

	public ApiAssetModelHeader(String sid, Date modified, String identifier) {
		super(sid, modified);
		this.identifier = identifier;
	}

	public ApiAssetModelHeader() {}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
