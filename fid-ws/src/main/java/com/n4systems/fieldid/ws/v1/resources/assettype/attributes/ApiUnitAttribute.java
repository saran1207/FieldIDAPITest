package com.n4systems.fieldid.ws.v1.resources.assettype.attributes;


public class ApiUnitAttribute extends ApiAttribute {
	private Long defaultUnitId;

	public ApiUnitAttribute(Long defaultUnitId) {
		super("unit");
		this.defaultUnitId = defaultUnitId;
	}
	
	public ApiUnitAttribute() {
		this(null);
	}

	public Long getDefaultUnitId() {
		return defaultUnitId;
	}

	public void setDefaultUnitId(Long defaultUnitId) {
		this.defaultUnitId = defaultUnitId;
	}

}
