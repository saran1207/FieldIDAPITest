package com.n4systems.fieldid.ws.v2.resources;

import java.util.Date;

public class ApiSortedModelHeader extends ApiModelHeader {
	private final Object sortValue;

	public ApiSortedModelHeader(String sid, Date modified, Object sortValue) {
		super(sid, modified);
		this.sortValue = sortValue;
	}

	public Object getSortValue() {
		return sortValue;
	}

}
