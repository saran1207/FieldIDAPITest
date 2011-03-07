package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsUnitOfMeasureCriteria extends WsCriteria {
	private long primaryUnitId;
    private Long secondaryUnitId;
	
	public WsUnitOfMeasureCriteria() {
		setCriteriaType("UNITOFMEASURE");
	}

	@XmlElement(name="PrimaryUnitId")
	public long getPrimaryUnitId() {
		return primaryUnitId;
	}

	public void setPrimaryUnitId(long primaryUnitId) {
		this.primaryUnitId = primaryUnitId;
	}

	@XmlElement(name="SecondaryUnitId")
	public Long getSecondaryUnitId() {
		return secondaryUnitId;
	}

	public void setSecondaryUnitId(Long secondaryUnitId) {
		this.secondaryUnitId = secondaryUnitId;
	}
	
}
