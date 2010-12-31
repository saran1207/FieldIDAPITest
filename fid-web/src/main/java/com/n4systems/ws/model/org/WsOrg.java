package com.n4systems.ws.model.org;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsOrg extends WsModel {
	private String name;
	private boolean active;
	private Long secondaryId;
	private Long customerId;
	private Long divisionId;
	
	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="Active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@XmlElement(name="SecondaryId")
	public Long getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(Long secondaryId) {
		this.secondaryId = secondaryId;
	}

	@XmlElement(name="CustomerId")
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@XmlElement(name="DivisionId")
	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

}
