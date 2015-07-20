package com.n4systems.fieldid.ws.v2.resources.setupdata.assettype.attributes;

public class ApiDateTimeAttribute extends ApiAttribute {
	private boolean includeTime;

	public ApiDateTimeAttribute(boolean includeTime) {
		super("datetime");
		this.includeTime = includeTime;
	}
	
	public ApiDateTimeAttribute() {
		this(false);
	}

	public boolean isIncludeTime() {
		return includeTime;
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}

}
