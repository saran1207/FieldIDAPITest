package com.n4systems.webservice.dto;

public class AbstractExternalOrgServiceDTO extends AbstractBaseOrgServiceDTO {
	
	private Long parentId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
