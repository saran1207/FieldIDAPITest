package com.n4systems.fieldid.ws.v2.resources.setupdata.org;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;

public class ApiOrg extends ApiReadOnlyModel2 {
	private String name;
	private Long parentId;
	private Long secondaryId;
	private Long customerId;
	private Long divisionId;
	private String address;
	private byte[] image;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(Long secondaryId) {
		this.secondaryId = secondaryId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
