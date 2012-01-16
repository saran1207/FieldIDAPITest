package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;


public class ApiUnitOfMeasureCriteria extends ApiCriteria {
	private long primaryUnitId;
    private Long secondaryUnitId;
	    
	public ApiUnitOfMeasureCriteria(long primaryUnitId, Long secondaryUnitId) {
		super("UNITOFMEASURE");
		this.primaryUnitId = primaryUnitId;
		this.secondaryUnitId = secondaryUnitId;
	}

	public ApiUnitOfMeasureCriteria() {
		this(0, null);
	}

	public long getPrimaryUnitId() {
		return primaryUnitId;
	}

	public void setPrimaryUnitId(long primaryUnitId) {
		this.primaryUnitId = primaryUnitId;
	}

	public Long getSecondaryUnitId() {
		return secondaryUnitId;
	}

	public void setSecondaryUnitId(Long secondaryUnitId) {
		this.secondaryUnitId = secondaryUnitId;
	}
	
}
